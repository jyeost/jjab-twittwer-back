package jjabtwitter.post.application;

import io.restassured.http.ContentType;
import jakarta.persistence.EntityManager;
import jjabtwitter.follow.application.FollowService;
import jjabtwitter.follow.domain.Follow;
import jjabtwitter.global.exception.ClientException;
import jjabtwitter.member.application.dto.MemberId;
import jjabtwitter.post.application.dto.PostRequest;
import jjabtwitter.post.domain.Post;
import jjabtwitter.post.domain.PostImage;
import jjabtwitter.support.IntegrationTest;
import jjabtwitter.support.MemberTestSupport;
import jjabtwitter.support.TestFileCleaner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import static jjabtwitter.global.exception.ExceptionInformation.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SuppressWarnings("NonAsciiCharacters")
@IntegrationTest
class PostServiceTest implements TestFileCleaner {

    static final MockMultipartFile images;
    static {
        try {
            images = new MockMultipartFile("images", "test1.jpg", "image/jpg", new FileInputStream("src/test/resources/uploadtest/image/test1.jpg"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Autowired
    PostService postService;

    @Autowired
    MemberTestSupport memberSupport;


    @Autowired
    FollowService followService;

    private MemberId 글쓴이;

    @BeforeEach
    void 글쓴이가_회원가입_후_로그인한다() {
        final Long id = memberSupport.create()
                .build()
                .getId();
        글쓴이 = new MemberId(id);
    }

    @Test
    void 사진_없는_게시글_작성_정상작동() {
        final PostRequest 새게시물 = new PostRequest("content", null);
        final Long postId = postService.createPost(새게시물, 글쓴이);

        assertThat(postId).isNotNull();
    }

    @Test
    void 사진_있는_게시글_작성_정상작동(@Autowired EntityManager entityManager) {
        final PostRequest 새게시물 = new PostRequest("content", List.of(images, images, images, images));
        final Long postId = postService.createPost(새게시물, 글쓴이);

        final List<PostImage> postImages = entityManager.createQuery(
                        "select i from PostImage i where i.post.id = :postId", PostImage.class)
                .setParameter("postId", postId)
                .getResultList();

        assertThat(postId).isNotNull();
        assertThat(postImages).hasSize(4);
    }

    @Test
    void 사진을_5개첨부하면_게시글_작성에_실패한다() {
        final PostRequest 새게시물 = new PostRequest("content", List.of(images, images, images, images, images));

        assertThatThrownBy(() -> postService.createPost(새게시물, 글쓴이))
                .isExactlyInstanceOf(ClientException.class)
                .hasMessage(POST_IMAGE_COUNT_INVALID.getMessage());
    }

    @Test
    void 사진중_사진이_아닌_파일이_포함되면_게시글_작성에_실패한다() throws IOException {
        MockMultipartFile txt = new MockMultipartFile("images", "test5.txt", ContentType.TEXT.name(), new FileInputStream("src/test/resources/uploadtest/image/test5.txt"));
        final PostRequest 새게시물 = new PostRequest("content", List.of(images, images, txt, images));

        assertThatThrownBy(() -> postService.createPost(새게시물, 글쓴이))
                .isExactlyInstanceOf(ClientException.class)
                .hasMessage(IMAGE_EXTENSION_INVALID.getMessage());
    }

    @Test
    void 게시글_내용이_비었다면_게시글_작성에_실패한다() {
        final PostRequest 새게시물 = new PostRequest(" ", null);

        assertThatThrownBy(() -> postService.createPost(새게시물, 글쓴이))
                .isExactlyInstanceOf(ClientException.class)
                .hasMessage(POST_CONTENT_LENGTH_INVALID.getMessage());
    }


    @Test
    void 게시글을_작성한_후_조회한다(){
        final PostRequest 새게시물 = new PostRequest("content", null);
        final Long postId = postService.createPost(새게시물, 글쓴이);
        final Long postId2 = postService.createPost(새게시물, 글쓴이);

        final List<Post> posts = postService.getPosts(글쓴이);

        assertThat(posts).isNotNull();
        assertThat(posts).hasSize(2);
    }

    @Test
    void 작성자와_팔로우한_사람들의_게시글을_조회한다(){
        final PostRequest 새게시물 = new PostRequest("content", null);
        final Long postId = postService.createPost(새게시물, 글쓴이);

        Long 팔로잉_id = memberSupport.create().build().getId();
        final Follow follow = followService.followMember(글쓴이, 팔로잉_id);

        postService.createPost(새게시물, new MemberId(팔로잉_id));

        final List<Post> posts = postService.getPosts(글쓴이);

        assertThat(posts).isNotNull();
        assertThat(posts).hasSize(2);
    }

}
