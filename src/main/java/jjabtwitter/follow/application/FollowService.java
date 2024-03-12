package jjabtwitter.follow.application;

import jjabtwitter.follow.domain.Follow;
import jjabtwitter.follow.repository.FollowRepository;
import jjabtwitter.global.exception.ClientException;
import jjabtwitter.global.exception.ExceptionInformation;
import jjabtwitter.member.application.dto.MemberId;
import jjabtwitter.member.domain.Member;
import jjabtwitter.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static jjabtwitter.global.exception.ExceptionInformation.FOLLOW_ALREADY_EXIST;
import static jjabtwitter.global.exception.ExceptionInformation.FOLLOW_SELF_INVALID;

@RequiredArgsConstructor
@Transactional
@Service
public class FollowService {

    private final MemberRepository memberRepository;

    private final FollowRepository followRepository;

    public Follow followMember(final MemberId followerId, final Long followingId) {
        final Member follower = findMember(followerId.id());
        final Member following = findMember(followingId);

        validateIsExist(follower, following);
        validateIsDifferentMember(follower, following);

        final Follow follow = Follow.create(follower, following);
        return followRepository.save(follow);
    }

    private Member findMember(final Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new ClientException(ExceptionInformation.MEMBER_NOT_FOUND));
    }

    private void validateIsExist(final Member follower, final Member following) {
        if (followRepository.existsByFollowerAndFollowing(follower, following)) {
            throw new ClientException(FOLLOW_ALREADY_EXIST);
        }
    }

    private void validateIsDifferentMember(final Member follower, final Member following) {
        if (follower.equals(following)) {
            throw new ClientException(FOLLOW_SELF_INVALID);
        }
    }
}
