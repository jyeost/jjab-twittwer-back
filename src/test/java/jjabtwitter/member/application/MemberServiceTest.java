package jjabtwitter.member.application;

import jjabtwitter.global.exception.ClientException;
import jjabtwitter.member.application.dto.JoinRequest;
import jjabtwitter.member.application.dto.MemberId;
import jjabtwitter.member.domain.Member;
import jjabtwitter.member.repository.MemberRepository;
import jjabtwitter.support.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static jjabtwitter.global.exception.ExceptionInformation.MEMBER_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SuppressWarnings("NonAsciiCharacters")
@IntegrationTest
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void 회원가입_정상동작() {
        final JoinRequest joinRequest = new JoinRequest("customId", "password1!", "nickname");
        final Member member = memberService.joinMember(joinRequest);

        assertThat(member.getCustomId()).isEqualTo("customId");
    }

    @Test
    void 회원아이디는_중복으로_가입할_수_없다() {
        final JoinRequest joinRequest = new JoinRequest("customId", "password1!", "nickname");
        memberService.joinMember(joinRequest);

        assertThatThrownBy(() -> memberService.joinMember(joinRequest))
                .isExactlyInstanceOf(ClientException.class);
    }

    @Test
    void 탈퇴한_회원은_조회할_수_없다() {
        // given
        final JoinRequest joinRequest = new JoinRequest("customId", "password1!", "nickname");
        final Member member = memberService.joinMember(joinRequest);

        // when
        memberService.withdraw(new MemberId(member.getId()));

        // then
        assertThat(memberRepository.findById(member.getId())).isEmpty();
    }

    @Test
    void 이미_탈퇴한_회원을_또_탈퇴할_수_없다() {
        // given
        final JoinRequest joinRequest = new JoinRequest("customId", "password1!", "nickname");
        final Member member = memberService.joinMember(joinRequest);
        final MemberId memberId = new MemberId(member.getId());

        // when
        memberService.withdraw(memberId);

        // then
        assertThatThrownBy(() -> memberService.withdraw(memberId))
                .isExactlyInstanceOf(ClientException.class)
                .hasMessage(MEMBER_NOT_FOUND.getMessage());
    }
}
