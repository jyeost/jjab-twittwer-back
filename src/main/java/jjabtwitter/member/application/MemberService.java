package jjabtwitter.member.application;

import jjabtwitter.global.exception.ClientException;
import jjabtwitter.member.application.dto.JoinRequest;
import jjabtwitter.member.application.dto.LoginRequest;
import jjabtwitter.member.application.dto.MemberId;
import jjabtwitter.member.domain.CustomId;
import jjabtwitter.member.domain.Member;
import jjabtwitter.member.domain.Password;
import jjabtwitter.member.domain.PasswordEncoder;
import jjabtwitter.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static jjabtwitter.global.exception.ExceptionInformation.*;

@RequiredArgsConstructor
@Transactional
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    public Member joinMember(final JoinRequest joinRequest) {
        final Member member = Member.create(
                joinRequest.customId(),
                joinRequest.nickname(),
                joinRequest.password()
        );

        validateIsIdDuplicated(joinRequest.customId());
        member.encrypt(passwordEncoder);

        return memberRepository.save(member);
    }

    private void validateIsIdDuplicated(final String customId) {
        if (memberRepository.existsByCustomId(CustomId.create(customId))) {
            throw new ClientException(MEMBER_CUSTOM_ID_DUPLICATE);
        }
    }

    public Member login(final LoginRequest loginRequest) {
        final CustomId customId = CustomId.create(loginRequest.customId());
        final Password password = Password.create(loginRequest.password());
        password.encrypt(passwordEncoder);
        return memberRepository.findByCustomIdAndPassword(customId, password)
                .orElseThrow(() -> new ClientException(LOGIN_FAIL));
    }

    public void withdraw(final MemberId memberId) {
        final Member member = memberRepository.findById(memberId.id())
                .orElseThrow(() -> new ClientException(MEMBER_NOT_FOUND));
        member.withdraw();

        //TODO: 회원 탈퇴시, 회원이 작성한 글과 팔로우 목록을 삭제할까?
    }
}
