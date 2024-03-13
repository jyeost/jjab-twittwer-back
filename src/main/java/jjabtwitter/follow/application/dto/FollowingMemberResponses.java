package jjabtwitter.follow.application.dto;

import jjabtwitter.follow.domain.Follow;

import java.util.List;
import java.util.Set;

public class FollowingMemberResponses {
    List<FollowingMemberResponse> followingMembers;

    public FollowingMemberResponses(final List<FollowingMemberResponse> followingMembers) {
        this.followingMembers = followingMembers;
    }

    public static FollowingMemberResponses of(final List<Follow> allFollowingMembers, final Set<Long> followBack) {
        final List<FollowingMemberResponse> followingMemberResponses = allFollowingMembers.stream()
                .map(follow -> new FollowingMemberResponse(
                        follow.getId(),
                        follow.getFollowing().getId(),
                        follow.getFollowing().getCustomId(),
                        follow.getFollowing().getNickName(),
                        follow.getFollowing().getProfileImageInfo(),
                        followBack.contains(follow.getFollowing().getId())
                ))
                .toList();
        return new FollowingMemberResponses(followingMemberResponses);
    }

    public List<FollowingMemberResponse> getFollowingMembers() {
        return followingMembers;
    }
}
