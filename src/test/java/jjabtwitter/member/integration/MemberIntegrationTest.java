package jjabtwitter.member.integration;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import jjabtwitter.member.application.dto.LoginRequest;
import jjabtwitter.support.IntegrationFixture;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static jjabtwitter.global.exception.ExceptionInformation.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SuppressWarnings("NonAsciiCharacters")
class MemberIntegrationTest extends IntegrationFixture {

    @Test
    void 회원가입_정상작동_201() {
        assertThat(회원가입_API("customId", "password123$", "nickname").statusCode())
                .isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    void 회원가입_아이디_예외_400() {
        final ExtractableResponse<Response> response = 회원가입_API("c", "password123$", "nickname");

        assertSoftly(
                SoftAssertions -> {
                    assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
                    assertThat(response.jsonPath().getInt("errorCode")).isEqualTo(MEMBER_CUSTOM_ID_INVALID.getCode());

                });
    }

    @Test
    void 회원가입_닉네임_예외_400() {
        final ExtractableResponse<Response> response = 회원가입_API("customId", "password123$", "");

        assertSoftly(
                SoftAssertions -> {
                    assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
                    assertThat(response.jsonPath().getInt("errorCode")).isEqualTo(MEMBER_NICKNAME_INVALID.getCode());

                });
    }

    @Test
    void 회원가입_아이디_중복_400() {
        회원가입_API("customId", "password123$", "nickname");
        final ExtractableResponse<Response> response = 회원가입_API("customId", "password123$", "nickname");

        assertSoftly(
                SoftAssertions -> {
                    assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
                    assertThat(response.jsonPath().getInt("errorCode")).isEqualTo(MEMBER_CUSTOM_ID_DUPLICATE.getCode());
                });
    }

    @Test
    void 정상_로그인_200() {
        // given
        회원가입_API("customId", "password123$", "nickname");
        final LoginRequest loginRequest = new LoginRequest("customId", "password123$");

        // when
        final ExtractableResponse<Response> response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(loginRequest)
                .when()
                .post("/login")
                .then()
                .extract();

        // then
        assertSoftly(
                SoftAssertions -> {
                    assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
                    assertThat(response.header("Set-Cookie")).contains("JSESSIONID");
                });
    }

    @Test
    void 비번이_틀린_경우_로그인_실패_401() {
        // given
        회원가입_API("customId", "password123$", "nickname");
        final LoginRequest loginRequest = new LoginRequest("customId", "password123!");

        // when
        final ExtractableResponse<Response> response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(loginRequest)
                .when()
                .post("/login")
                .then()
                .extract();

        // then
        assertSoftly(
                SoftAssertions -> {
                    assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
                    assertThat(response.header("Set-Cookie")).isNull();
                    assertThat(response.jsonPath().getInt("errorCode")).isEqualTo(LOGIN_FAIL.getCode());
                });
    }

    @Test
    void 아이디가_틀린_경우_로그인_실패_401() {
        // given
        회원가입_API("customId", "password123$", "nickname");
        final LoginRequest loginRequest = new LoginRequest("customId0", "password123$");

        // when
        final ExtractableResponse<Response> response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(loginRequest)
                .when()
                .post("/login")
                .then()
                .extract();

        // then
        assertSoftly(
                SoftAssertions -> {
                    assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
                    assertThat(response.header("Set-Cookie")).isNull();
                    assertThat(response.jsonPath().getInt("errorCode")).isEqualTo(LOGIN_FAIL.getCode());
                });
    }
}
