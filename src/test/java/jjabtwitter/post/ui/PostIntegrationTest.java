package jjabtwitter.post.ui;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import jjabtwitter.support.IntegrationFixture;
import jjabtwitter.support.TestFileCleaner;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.io.File;

import static io.restassured.config.EncoderConfig.encoderConfig;
import static jjabtwitter.global.exception.ExceptionInformation.AUTHORIZATION_EMPTY;
import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
class PostIntegrationTest extends IntegrationFixture implements TestFileCleaner {

    private static final File 이미지1 = new File("src/test/resources/uploadtest/image/test1.jpg");

    @Test
    void 사진_있는_글쓰기_정상동작_201() {
        final String sessionId = 로그인_API();
        final int statusCode = RestAssured.given()
                .header(new Header("content-type", "multipart/form-data"))
                .multiPart("images", 이미지1)
                .multiPart("images", 이미지1)
                .formParam("content", "content test")
                .sessionId(sessionId)
                .when()
                .post("/posts")
                .then()
                .extract()
                .statusCode();

        assertThat(statusCode).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    void 사진_없는_글쓰기_정상동작_201() {
        final String sessionId = 로그인_API();

        final int statusCode = RestAssured.given()
                .config(RestAssured.config().encoderConfig(encoderConfig().encodeContentTypeAs("multipart/form-data", ContentType.TEXT)))
                .formParam("content", "content test")
                .sessionId(sessionId)
                .when()
                .post("/posts")
                .then()
                .extract()
                .statusCode();

        assertThat(statusCode).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    void 로그인_하지_않은_사용자는_글을_작성할_수_없다_401() {

        final ExtractableResponse<Response> response = RestAssured.given()
                .header(new Header("content-type", "multipart/form-data"))
                .multiPart("images", 이미지1)
                .formParam("content", "content test")
                .when()
                .post("/posts")
                .then()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
        assertThat(response.jsonPath().getInt("errorCode")).isEqualTo(AUTHORIZATION_EMPTY.getCode());
    }
}
