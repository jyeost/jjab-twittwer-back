package jjabtwitter.follow.application.dto;

import jjabtwitter.follow.domain.Follow;

import java.util.List;
import java.util.Set;

public class FollowerMemberResponses {
    List<FollowerMemberResponse> followerMembers;

    public FollowerMemberResponses(final List<FollowerMemberResponse> followerMembers) {
        this.followerMembers = followerMembers;
    }

    public static FollowerMemberResponses of(final List<Follow> allFollowerMembers, final Set<Long> followingInFollowers) {
        final List<FollowerMemberResponse> followerMemberResponses = allFollowerMembers.stream()
                .map(follow -> new FollowerMemberResponse(
                        follow.getId(),
                        follow.getFollower().getId(),
                        follow.getFollower().getCustomId(),
                        follow.getFollower().getNickName(),
                        follow.getFollower().getProfileImageInfo(),
                        followingInFollowers.contains(follow.getFollower().getId())
                ))
                .toList();
        return new FollowerMemberResponses(followerMemberResponses);
    }

    public List<FollowerMemberResponse> getFollowerMembers() {
        return followerMembers;
    }
}
