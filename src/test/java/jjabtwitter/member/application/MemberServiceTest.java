package jjabtwitter.member.application;

import jjabtwitter.global.exception.ClientException;
import jjabtwitter.member.application.dto.JoinRequest;
import jjabtwitter.member.domain.Member;
import jjabtwitter.support.IntegrationTest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SuppressWarnings("NonAsciiCharacters")
@IntegrationTest
class MemberServiceTest {

    private final MemberService memberService;

    public MemberServiceTest(final MemberService memberService) {
        this.memberService = memberService;
    }

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

    // TODO: 비밀번호 암호화 해서 저장되는 것도 테스트 해야할지...

}
