package jjabtwitter.post.domain;

import jakarta.persistence.*;
import jjabtwitter.global.domain.TemporalRecord;
import jjabtwitter.global.exception.ClientException;
import jjabtwitter.member.domain.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.SQLRestriction;

import java.util.Objects;

import static jjabtwitter.global.exception.ExceptionInformation.POST_CONTENT_LENGTH_INVALID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@SQLRestriction("deleted = false")
@Entity
public class Post extends TemporalRecord {

    private static final int MIN_CONTENT_LENGTH = 1;
    private static final int MAX_CONTENT_LENGTH = 140;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Member member;

    @JoinColumn(nullable = false)
    private String content;

    @ColumnDefault("0")
    private Long retweetCount;

    @ColumnDefault("0")
    private Long heartCount;

    private PostImages postImages;

    @ColumnDefault("false")
    private boolean deleted;

    private Post(final String content, final Member member, final PostImages postImages) {
        this.content = content;
        this.member = member;
        this.postImages = postImages;
    }

    public static Post create(final String content, final Member member) {
        validateContent(content);
        return new Post(content, member, PostImages.create());
    }

    private static void validateContent(final String content) {
        if (Objects.isNull(content) || content.isBlank() || content.length() < MIN_CONTENT_LENGTH || content.length() > MAX_CONTENT_LENGTH) {
            throw new ClientException(POST_CONTENT_LENGTH_INVALID);
        }
    }

    public void validateImageCount(final int imageSize) {
        postImages.validateImageCount(imageSize);
    }
}
