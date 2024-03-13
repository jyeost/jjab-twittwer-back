package jjabtwitter.follow.application.dto;

public record FollowingMemberResponse(
        Long id,
        Long memberId,
        String customId,
        String nickName,
        String profileImageInfo,
        boolean followBack
) {

}
