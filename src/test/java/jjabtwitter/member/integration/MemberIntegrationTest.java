package jjabtwitter.member.integration;

import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import jjabtwitter.IntegrationFixture;
import jjabtwitter.member.application.dto.JoinRequest;
import jjabtwitter.member.application.dto.LoginRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

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

    @Test
    void 정상_로그인_200() {
        // given
        회원가입(new JoinRequest("customId", "password123$", "nickname"));
        final LoginRequest loginRequest = new LoginRequest("customId", "password123$");

        // when
        final ExtractableResponse<Response> response = RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(loginRequest)
                .when()
                .post("/login")
                .then()
                .log().all()
                .extract();

        // then
        assertSoftly(
                SoftAssertions -> {
                    assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
                    assertThat(response.header("Set-Cookie")).contains("JSESSIONID");
                });
//
//        List<Header> cookies = response.headers().getList("Set-Cookie");
//
//        // JSESSIONID 값을 저장할 변수
//        String jsessionId = "";
//
//        // 모든 쿠키를 확인하면서 JSESSIONID를 찾음
//        for (Header cookie : cookies) {
//            if (cookie.getValue().contains("JSESSIONID")) {
//                // JSESSIONID를 가져옴
//                jsessionId = cookie.getValue().split(";")[0].split("=")[1];
//                break;
//            }
//        }
//
//        RestAssured
//                .given()
//                .log().all()
//                .header("Cookie", jsessionId)
//                .when()
//                .get("/yaya")
//                .then()
//                .log().all()
//                .extract();
    }
}
