package jjabtwitter;

import jjabtwitter.member.ui.session.SessionConst;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {

    @Bean
    public SessionConst sessionConst() {
        return new TestSessionConst();
    }
}
