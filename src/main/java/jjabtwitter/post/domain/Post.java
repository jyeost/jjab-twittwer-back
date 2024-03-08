package jjabtwitter.post.domain;

import jakarta.persistence.*;
import jjabtwitter.global.domain.TemporalRecord;
import jjabtwitter.member.domain.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.SQLRestriction;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@SQLRestriction("deleted = false")
@Entity
public class Post extends TemporalRecord {

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
        return new Post(content, member, PostImages.create());
    }

    public void validateImageCount(final int imageSize) {
        postImages.validateImageCount(imageSize);
    }
}
