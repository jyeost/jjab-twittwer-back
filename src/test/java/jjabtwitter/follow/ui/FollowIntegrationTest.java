package jjabtwitter.follow.ui;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import jjabtwitter.support.IntegrationFixture;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static jjabtwitter.global.exception.ExceptionInformation.FOLLOW_ALREADY_EXIST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SuppressWarnings("NonAsciiCharacters")
class FollowIntegrationTest extends IntegrationFixture {

    @Test
    void 좋아요_정상동작_확인_200() {
        // given
        final String followerSessionId = 로그인_API();
        final Long followingId = 회원가입();

        // when
        final int statusCode = RestAssured.given()
                .sessionId(followerSessionId)
                .when()
                .post("/follow/members/" + followingId)
                .then()
                .extract()
                .statusCode();

        // then
        assertThat(statusCode).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void 로그인하지_않은_사용자는_팔로우_신청을_할_수_없다_401() {
        // given
        final Long followingId = 회원가입();

        // when
        final int statusCode = RestAssured.given()
                .when()
                .post("/follow/members/" + followingId)
                .then()
                .extract()
                .statusCode();

        // then
        assertThat(statusCode).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    void 이미_팔로우_한_이용자는_다시_팔로우_신청할_수_없다() {
        // given
        final String followerSessionId = 로그인_API();
        final Long followingId = 회원가입();

        RestAssured.given()
                .sessionId(followerSessionId)
                .when()
                .post("/follow/members/" + followingId);

        // when
        final ExtractableResponse<Response> 팔로우_요청_응답 = RestAssured.given()
                .sessionId(followerSessionId)
                .when()
                .post("/follow/members/" + followingId)
                .then()
                .extract();

        // then
        assertSoftly(
                SoftAssertions -> {
                    assertThat(팔로우_요청_응답.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
                    assertThat(팔로우_요청_응답.jsonPath().getInt("errorCode")).isEqualTo(FOLLOW_ALREADY_EXIST.getCode());
                });
    }

}
