package jjabtwitter.member.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jjabtwitter.global.exception.ClientException;
import jjabtwitter.global.exception.ExceptionInformation;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Password {

    @Column(name = "password", nullable = false)
    private String value;

    protected Password(final String value) {
        this.value = value;
    }

    public static Password create(final String value) {
        if (PasswordValidator.isValidPassword(value)) {
            return new Password(value);
        }
        throw new ClientException(ExceptionInformation.MEMBER_PASSWORD_INVALID);
    }

    public void encrypt(final PasswordEncoder passwordEncoder) {
        this.value = passwordEncoder.encode(value);
    }
}
