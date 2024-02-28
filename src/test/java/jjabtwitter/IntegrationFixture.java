package jjabtwitter;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import jjabtwitter.member.application.dto.JoinRequest;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;

@IntegrationTest
public class IntegrationFixture {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    protected ExtractableResponse<Response> 회원가입(final JoinRequest joinRequest) {
        return RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(joinRequest)
                .when()
                .post("/join")
                .then()
                .extract();
    }
}
