package jjabtwitter.member.ui;

import jakarta.servlet.http.HttpServletRequest;
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

import static jjabtwitter.member.ui.SessionConst.SESSION;

@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/join")
    public ResponseEntity<Void> join(@RequestBody final JoinRequest joinRequest) {
        memberService.joinMember(joinRequest);
        return ResponseEntity.created(URI.create("/join")).build();
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody final LoginRequest loginRequest, final HttpServletRequest httpRequest) {

        final Member member = memberService.login(loginRequest);
        final HttpSession session = httpRequest.getSession();
        session.setAttribute(SESSION.getKey(), new LoginInfo(member.getId()));
        session.setMaxInactiveInterval(SESSION.getValidatedTime());

        return ResponseEntity.ok().build();
    }
}
