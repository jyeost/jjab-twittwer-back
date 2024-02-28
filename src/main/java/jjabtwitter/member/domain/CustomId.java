package jjabtwitter.member.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jjabtwitter.global.exception.ClientException;
import jjabtwitter.global.exception.ExceptionInformation;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.util.Strings;

@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class CustomId {
    private static final int MIN_ID_LENGTH = 4;
    private static final int MAX_ID_LENGTH = 20;

    @Column(name = "customId", nullable = false)
    private String value;

    private CustomId(final String value) {
        this.value = value;
    }

    public static CustomId create(final String value) {
        validate(value);
        return new CustomId(value);
    }

    private static void validate(final String customId) {
        if (Strings.isBlank(customId) || customId.length() > MAX_ID_LENGTH || customId.length() < MIN_ID_LENGTH) {
            throw new ClientException(ExceptionInformation.MEMBER_NICKNAME_INVALID);
        }
    }

    public String getValue() {
        return value;
    }
}
