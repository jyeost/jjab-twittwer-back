package jjabtwitter.follow.application.dto;

public record FollowerMemberResponse(
        Long id,
        Long memberId,
        String customId,
        String nickName,
        String profileImageInfo,
        boolean followed
) {
}
