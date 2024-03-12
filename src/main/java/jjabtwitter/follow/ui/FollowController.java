package jjabtwitter.follow.ui;

import jjabtwitter.follow.application.FollowService;
import jjabtwitter.member.application.dto.MemberId;
import jjabtwitter.member.ui.Auth;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
}
