package jjabtwitter.post.application;

import jjabtwitter.follow.repository.FollowRepository;
import jjabtwitter.global.exception.ClientException;
import jjabtwitter.global.exception.ExceptionInformation;
import jjabtwitter.image.ImageUploadService;
import jjabtwitter.member.application.dto.MemberId;
import jjabtwitter.member.domain.Member;
import jjabtwitter.member.repository.MemberRepository;
import jjabtwitter.post.application.dto.PostRequest;
import jjabtwitter.post.domain.Post;
import jjabtwitter.post.domain.PostImages;
import jjabtwitter.post.repository.PostImageRepository;
import jjabtwitter.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final ImageUploadService imageUploadService;
    private final PostImageRepository postImageRepository;
    private final FollowRepository followRepository;

    public Long createPost(final PostRequest postRequest, final MemberId memberId) {
        final Member member = memberRepository.findById(memberId.id())
                .orElseThrow(() -> new ClientException(ExceptionInformation.MEMBER_NOT_FOUND));

        final Post post = Post.create(postRequest.content(), member);
        postRepository.save(post);

        if (isImagesEmpty(postRequest.images())) {
            return post.getId();
        }

        post.validateImageCount(postRequest.images().size());
        final List<String> uploadedImages = imageUploadService.uploadAll(postRequest.images());
        final PostImages postImages = PostImages.create(uploadedImages, post);
        postImageRepository.saveAll(postImages.getPostImages());

        return post.getId();
    }

    public List<Post> getPosts(final MemberId memberId) {
        final Member member = memberRepository.findById(memberId.id())
                .orElseThrow(() -> new ClientException(ExceptionInformation.MEMBER_NOT_FOUND));

        List<Long> memberIds = followRepository.findAllFollowingMembers(memberId.id())
                .stream()
                .map(follow -> follow.getFollowing().getId())
                .collect(Collectors.toCollection(ArrayList::new));

        memberIds.add(memberId.id());

        List<Member> members = memberRepository.findByIdIn(memberIds);


        return postRepository.findByMemberIn(members);
    }

    private boolean isImagesEmpty(final List<MultipartFile> images) {
        return Objects.isNull(images) || images.isEmpty() || isDummy(images);
    }

    private boolean isDummy(final List<MultipartFile> images) {
        return images.get(0).isEmpty();
    }
}
