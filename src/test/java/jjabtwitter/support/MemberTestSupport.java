package jjabtwitter.support;

import jjabtwitter.member.domain.Member;
import jjabtwitter.member.domain.PasswordEncoder;
import jjabtwitter.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Objects;

@Component
public class MemberTestSupport {
    public static final String DEFAULT_CUSTOM_ID = "TWITTY";
    public static final String DEFAULT_PASSWORD = "TWITTY1234!";
    public static final String DEFAULT_NICKNAME = "트위띠";

    private static int customIdCount = 1;
    private static int nickNameCount = 1;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MemberRepository memberRepository;

    public String getDefaultPassword() {
        return DEFAULT_PASSWORD;
    }

    public MemberBuilder create() {
        return new MemberBuilder();
    }

    public final class MemberBuilder {

        private Long id;

        private String customId;

        private String nickName;

        private String password;

        private String profileImageInfo;

        private boolean deleted;

        public MemberBuilder id(final Long id) {
            this.id = id;
            return this;
        }

        public MemberBuilder customId(final String customId) {
            this.customId = customId;
            return this;
        }

        public MemberBuilder nickName(final String nickName) {
            this.nickName = nickName;
            return this;
        }

        public MemberBuilder password(final String password) {
            this.password = password;
            return this;
        }

        public MemberBuilder profileImageInfo(final String profileImageInfo) {
            this.profileImageInfo = profileImageInfo;
            return this;
        }

        public MemberBuilder deleted(final boolean deleted) {
            this.deleted = deleted;
            return this;
        }

        public Member build() {
            final Member member = Member.create(
                    this.customId != null ? this.customId : DEFAULT_CUSTOM_ID + customIdCount++,
                    this.nickName != null ? this.nickName : DEFAULT_NICKNAME + nickNameCount++,
                    this.password != null ? this.password : DEFAULT_PASSWORD
            );
            member.encrypt(passwordEncoder);

            setField(member, "id", this.id, null);
            setField(member, "profileImageInfo", this.profileImageInfo, null);
            setField(member, "deleted", this.deleted, false);

            return memberRepository.save(member);
        }

        private void setField(final Member member, final String fieldName, final Object value, final Object orElse) {
            final Field field = ReflectionUtils.findField(Member.class, fieldName);
            ReflectionUtils.makeAccessible(field);
            if (Objects.nonNull(value)) {
                ReflectionUtils.setField(field, member, value);
                return;
            }
            ReflectionUtils.setField(field, member, orElse);
        }
    }
}
