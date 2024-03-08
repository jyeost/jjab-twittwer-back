package jjabtwitter.post.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class PostImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String storedName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Post post;

    @ColumnDefault("false")
    private boolean deleted;

    private PostImage(final String storedName, final Post post) {
        this.storedName = storedName;
        this.post = post;
    }

    public static PostImage create(final String storeName, final Post post) {
        return new PostImage(storeName, post);
    }

    public String getStoredName() {
        return storedName;
    }
}
