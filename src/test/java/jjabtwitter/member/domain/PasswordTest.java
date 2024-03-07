package jjabtwitter.member.domain;

import jjabtwitter.global.exception.ClientException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SuppressWarnings("NonAsciiCharacters")
class PasswordTest {

    @ParameterizedTest
    @ValueSource(strings = {"@a123456", "a1b2c3d4e5f6g7h8i9j1k2l3n4m5o!"})
    void 비밀번호가_8글자_이상_30글자_이하에_숫자_문자_특문을_전부포함한다면_통과한다(String password) {
        assertThat(Password.create(password))
                .isNotNull();
    }

    @Test
    void 비밀번호에_공백이_있으면_예외발생() {
        assertThatThrownBy(() -> Password.create("@a 123456"))
                .isExactlyInstanceOf(ClientException.class);
    }

    @Test
    void 비밀번호에_특문이_없으면_예외발생() {
        assertThatThrownBy(() -> Password.create("a123456b"))
                .isExactlyInstanceOf(ClientException.class);
    }

    @Test
    void 비밀번호에_숫자가_없으면_예외발생() {
        assertThatThrownBy(() -> Password.create("@abcdefgh"))
                .isExactlyInstanceOf(ClientException.class);
    }

    @Test
    void 비밀번호에_영문이_없으면_예외발생() {
        assertThatThrownBy(() -> Password.create("@12345678"))
                .isExactlyInstanceOf(ClientException.class);
    }
}
