package jjabtwitter.follow.ui;

import jjabtwitter.follow.application.FollowService;
import jjabtwitter.follow.application.dto.FollowerMemberResponses;
import jjabtwitter.follow.application.dto.FollowingMemberResponses;
import jjabtwitter.member.application.dto.MemberId;
import jjabtwitter.member.ui.Auth;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class FollowController {

    private final FollowService followService;

    @PostMapping("/follow/members/{followingId}")
    public ResponseEntity<Void> followMember(@Auth MemberId followerId, @PathVariable Long followingId) {
        followService.followMember(followerId, followingId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/unfollow/members/{followingId}")
    public ResponseEntity<Void> unfollowMember(@Auth MemberId followerId, @PathVariable Long followingId) {
        followService.unfollowMember(followerId, followingId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/following/members")
    public ResponseEntity<FollowingMemberResponses> showFollowingMembers(@Auth MemberId followerId) {
        final FollowingMemberResponses followingMembers = followService.getFollowingMembers(followerId);
        return ResponseEntity.ok().body(followingMembers);
    }

    @GetMapping("/follower/members")
    public ResponseEntity<FollowerMemberResponses> showFollowerMembers(@Auth MemberId followerId) {
        final FollowerMemberResponses followerMembers = followService.getFollowerMembers(followerId);
        return ResponseEntity.ok().body(followerMembers);
    }
}
