package jjabtwitter.member.integration;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import jjabtwitter.IntegrationFixture;
import jjabtwitter.member.application.dto.JoinRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static jjabtwitter.global.exception.ExceptionInformation.MEMBER_CUSTOM_ID_DUPLICATE;
import static jjabtwitter.global.exception.ExceptionInformation.MEMBER_NICKNAME_INVALID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class MemberIntegrationTest extends IntegrationFixture {

    @Test
    void 회원가입_정상작동_201() {
        final JoinRequest joinRequest = new JoinRequest("customId", "password123$", "nickname");

        assertThat(회원가입(joinRequest).statusCode())
                .isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    void 회원가입_닉네임_예외_400() {
        final JoinRequest joinRequest = new JoinRequest("c", "password123$", "nickname");

        final ExtractableResponse<Response> response = 회원가입(joinRequest);

        assertSoftly(
                SoftAssertions -> {
                    assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
                    assertThat(response.jsonPath().get("errorCode").equals(MEMBER_NICKNAME_INVALID.getCode())).isTrue();
                });
    }

    @Test
    void 회원가입_아이디_중복_400() {
        final JoinRequest joinRequest = new JoinRequest("customId", "password123$", "nickname");

        회원가입(joinRequest);
        final ExtractableResponse<Response> response = 회원가입(joinRequest);

        assertSoftly(
                SoftAssertions -> {
                    assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
                    assertThat(response.jsonPath().get("errorCode").equals(MEMBER_CUSTOM_ID_DUPLICATE.getCode())).isTrue();
                });
    }
}
