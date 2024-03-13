package jjabtwitter.follow.ui;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import jjabtwitter.member.domain.Member;
import jjabtwitter.support.IntegrationFixture;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static jjabtwitter.global.exception.ExceptionInformation.FOLLOW_ALREADY_EXIST;
import static jjabtwitter.support.MemberTestSupport.DEFAULT_PASSWORD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;

@SuppressWarnings("NonAsciiCharacters")
class FollowIntegrationTest extends IntegrationFixture {

    private static void 팔로우(final String followerSessionId, final Long followingId) {
        RestAssured.given()
                .sessionId(followerSessionId)
                .when()
                .post("/follow/members/" + followingId);
    }

    @Test
    void 로그인하지_않은_사용자는_팔로우_신청을_할_수_없다_401() {
        // given
        final Long followingId = 회원가입_아이디();

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
    void 팔로우_정상동작_확인_200() {
        // given
        final String followerSessionId = 로그인_API();
        final Long followingId = 회원가입_아이디();

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
    void 이미_팔로우_한_이용자는_다시_팔로우_신청할_수_없다() {
        // given
        final String followerSessionId = 로그인_API();
        final Long followingId = 회원가입_아이디();
        팔로우(followerSessionId, followingId);

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

    @Test
    void 언팔로우_정상동작_확인_200() {
        // given
        final String followerSessionId = 로그인_API();
        final Long followingId = 회원가입_아이디();
        팔로우(followerSessionId, followingId);

        // when
        final int statusCode = RestAssured.given()
                .sessionId(followerSessionId)
                .when()
                .post("/unfollow/members/" + followingId)
                .then()
                .extract()
                .statusCode();

        // then
        assertThat(statusCode).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void 팔로잉_목록_가져오기() {
        // given
        final String followerSessionId = 로그인_API();
        final Long followingId = 회원가입_아이디();
        final Long followingId2 = 회원가입_아이디();
        final Long followingId3 = 회원가입_아이디();

        // when
        팔로우(followerSessionId, followingId);
        팔로우(followerSessionId, followingId2);
        팔로우(followerSessionId, followingId3);

        // then
        RestAssured.given()
                .sessionId(followerSessionId)
                .when()
                .get("/following/members")
                .then()

                .assertThat()
                .body("followingMembers", hasSize(3))
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void 팔로잉_목록0_가져오기() {
        // given
        final String followerSessionId = 로그인_API();

        // then
        RestAssured.given()
                .sessionId(followerSessionId)
                .when()
                .get("/following/members")
                .then()
                .assertThat()
                .body("followingMembers", empty())
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void 팔로워_목록2_가져오기() {
        // given
        final Member 팔로잉 = 회원가입();
        final Member 팔로워 = 회원가입();
        final Member 팔로워2 = 회원가입();

        final String 팔로워1_sessionId = 로그인_API(팔로워.getCustomId(), DEFAULT_PASSWORD);
        final String 팔로워2_sessionId = 로그인_API(팔로워2.getCustomId(), DEFAULT_PASSWORD);

        // when
        팔로우(팔로워1_sessionId, 팔로잉.getId());
        팔로우(팔로워2_sessionId, 팔로잉.getId());

        // then
        final String 팔로잉_sessionId = 로그인_API(팔로잉.getCustomId(), DEFAULT_PASSWORD);
        RestAssured.given()
                .sessionId(팔로잉_sessionId)
                .when()
                .get("/follower/members")
                .then()
                .assertThat()
                .body("followerMembers", hasSize(2))
                .statusCode(HttpStatus.OK.value());
    }
}
