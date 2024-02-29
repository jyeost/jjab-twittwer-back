package jjabtwitter.member.application;

import jjabtwitter.global.exception.ClientException;
import jjabtwitter.global.exception.ExceptionInformation;
import jjabtwitter.member.application.dto.JoinRequest;
import jjabtwitter.member.domain.CustomId;
import jjabtwitter.member.domain.Member;
import jjabtwitter.member.domain.PasswordEncoder;
import jjabtwitter.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            throw new ClientException(ExceptionInformation.MEMBER_CUSTOM_ID_DUPLICATE);
        }
    }
}
