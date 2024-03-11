package jjabtwitter.member.domain;

import jakarta.persistence.*;
import jjabtwitter.global.domain.TemporalRecord;
import jjabtwitter.global.exception.ClientException;
import jjabtwitter.global.exception.ExceptionInformation;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = {@UniqueConstraint(name = "uk_custom_id", columnNames = {"customId"})})
@Entity
public class Member extends TemporalRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private CustomId customId;

    @Column(nullable = false)
    private String nickName;

    @Column(nullable = false)
    private Password password;

    private String profileImageInfo;

    @ColumnDefault(value = "false")
    private boolean deleted;

    public Member(final CustomId customId, final String nickName, final Password password) {
        this.customId = customId;
        this.nickName = nickName;
        this.password = password;
    }

    public static Member create(final String customId, final String nickName, final String password) {
        if (Objects.isNull(nickName) || nickName.isBlank() || nickName.length() < 1 || nickName.length() > 30) {
            throw new ClientException(ExceptionInformation.MEMBER_NICKNAME_INVALID);
        }
        return new Member(CustomId.create(customId), nickName, Password.create(password));
    }

    public Long getId() {
        return id;
    }

    public String getCustomId() {
        return customId.getValue();
    }

    public String getNickName() {
        return nickName;
    }

    public String getProfileImageInfo() {
        return profileImageInfo;
    }

    public void encrypt(final PasswordEncoder passwordEncoder) {
        password.encrypt(passwordEncoder);
    }
}
