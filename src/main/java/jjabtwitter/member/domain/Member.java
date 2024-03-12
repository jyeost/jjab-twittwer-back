package jjabtwitter.member.domain;

import jakarta.persistence.*;
import jjabtwitter.global.domain.TemporalRecord;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLRestriction;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = {@UniqueConstraint(name = "uk_custom_id", columnNames = {"customId"})})
@SQLRestriction("deleted = false")
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
        return new Member(CustomId.create(customId), nickName, Password.create(password));
    }

    public Long getId() {
        return this.id;
    }

    public String getCustomId() {
        return this.customId.getValue();
    }

    public String getNickName() {
        return this.nickName;
    }

    public String getProfileImageInfo() {
        return this.profileImageInfo;
    }

    public void encrypt(final PasswordEncoder passwordEncoder) {
        this.password.encrypt(passwordEncoder);
    }

    public void withdraw() {
        this.deleted = true;
    }
}
