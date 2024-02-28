package jjabtwitter.member.ui;

import jjabtwitter.member.application.MemberService;
import jjabtwitter.member.application.dto.JoinRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/join")
    public ResponseEntity<Void> join(@RequestBody final JoinRequest joinRequest) {
        memberService.joinMember(joinRequest);
        return ResponseEntity.created(URI.create("/login")).build();
    }
}
