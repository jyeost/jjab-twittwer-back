package jjabtwitter.member.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.regex.Pattern;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PasswordValidator {
    private static final String PASSWORD_PATTERN = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,30}$";
    private static final Pattern passwordPattern = Pattern.compile(PASSWORD_PATTERN);

    static boolean isValidPassword(final String value) {
        if (Objects.isNull(value) || value.isBlank()) {
            return false;
        }
        return passwordPattern.matcher(value).matches();
    }
}
