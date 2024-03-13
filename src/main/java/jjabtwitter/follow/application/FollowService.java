package jjabtwitter.follow.application;

import jjabtwitter.follow.application.dto.FollowerMemberResponses;
import jjabtwitter.follow.application.dto.FollowingMemberResponses;
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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public int unfollowMember(final MemberId followerId, final Long followingId) {
        return followRepository.deleteOneByFollowerIdAndFollowingId(followerId.id(), followingId);
    }

    public FollowingMemberResponses getFollowingMembers(final MemberId followerId) {
        final Member follower = findMember(followerId.id());

        // TODO: 페이지네이션
        final List<Follow> allFollowingMembers = followRepository.findAllFollowingMembers(follower.getId());
        final Set<Long> followBack = findFollowBack(follower.getId(), allFollowingMembers);
        return FollowingMemberResponses.of(allFollowingMembers, followBack);
    }

    private Set<Long> findFollowBack(final Long followerId, final List<Follow> allFollowingMembers) {
        final List<Long> allFollowingMemberIds = allFollowingMembers.stream()
                .map(follow -> follow.getFollowing().getId())
                .toList();
        return new HashSet<>(followRepository.findFollowBack(allFollowingMemberIds, followerId));
    }

    public FollowerMemberResponses getFollowerMembers(final MemberId followeeId) {
        final Member followee = findMember(followeeId.id());

        final List<Follow> allFollowerMembers = followRepository.findAllFollowerMembers(followee.getId());
        final Set<Long> followingInFollowers = findFollowing(followee.getId(), allFollowerMembers);
        return FollowerMemberResponses.of(allFollowerMembers, followingInFollowers);
    }

    private Set<Long> findFollowing(final Long followeeId, final List<Follow> allFollowerMembers) {
        final List<Long> allFollowerMemberIds = allFollowerMembers.stream()
                .map(follow -> follow.getFollower().getId())
                .toList();
        return new HashSet<>(followRepository.findFollowing(allFollowerMemberIds, followeeId));
    }
}
