package jjabtwitter.support;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import jjabtwitter.member.application.dto.JoinRequest;
import jjabtwitter.member.application.dto.LoginRequest;
import jjabtwitter.member.domain.Member;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;

@IntegrationTest
public class IntegrationFixture {

    @Autowired
    private MemberTestSupport memberSupport;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    protected ExtractableResponse<Response> 회원가입(final String customId, final String password, final String nickName) {
        final JoinRequest joinRequest = new JoinRequest(customId, password, nickName);
        return RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(joinRequest)
                .when()
                .post("/join")
                .then()
                .extract();
    }

    protected String 로그인(){
        final Member member = memberSupport.create().build();

        return  RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new LoginRequest(member.getCustomId(), memberSupport.getDefaultPassword()))
                .when()
                .post("/login")
                .then()
                .extract()
                .sessionId();
    }
}
