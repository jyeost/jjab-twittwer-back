package jjabtwitter.member.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jjabtwitter.global.exception.ClientException;
import jjabtwitter.global.exception.ExceptionInformation;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.regex.Pattern;

@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Password {

    private static final String PASSWORD_PATTERN = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,30}$";
    private static final Pattern passwordPattern = Pattern.compile(PASSWORD_PATTERN);

    @Column(name = "password", nullable = false)
    private String value;

    protected Password(final String value) {
        this.value = value;
    }

    public static Password create(final String value) {
        if (isValidPassword(value)) {
            return new Password(value);
        }
        throw new ClientException(ExceptionInformation.MEMBER_PASSWORD_INVALID);
    }

    private static boolean isValidPassword(final String value) {
        if (Objects.isNull(value) || value.isBlank()) {
            return false;
        }
        return passwordPattern.matcher(value).matches();
    }
}
