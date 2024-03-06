package jjabtwitter.member.ui;

import jakarta.servlet.http.HttpSession;
import jjabtwitter.member.application.MemberService;
import jjabtwitter.member.application.dto.JoinRequest;
import jjabtwitter.member.application.dto.LoginRequest;
import jjabtwitter.member.domain.LoginInfo;
import jjabtwitter.member.domain.Member;
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

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody final LoginRequest loginRequest, final HttpSession httpSession) {

        final Member member = memberService.login(loginRequest);
        httpSession.setAttribute("loginIfo", new LoginInfo(member.getId()));
        httpSession.setMaxInactiveInterval(10000);

        return ResponseEntity.ok().build();
    }
}
