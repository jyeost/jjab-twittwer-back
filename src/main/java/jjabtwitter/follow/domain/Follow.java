package jjabtwitter.follow.domain;

import jakarta.persistence.*;
import jjabtwitter.global.domain.TemporalRecord;
import jjabtwitter.member.domain.Member;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@Table(
        name = "follow",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_follow_follower_following",
                        columnNames = {"follower_id", "following_id"}
                ),
        })
@Entity
public class Follow extends TemporalRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Member follower;

    @ManyToOne
    private Member following;

    private Follow(final Member follower, final Member following) {
        this.follower = follower;
        this.following = following;
    }

    public static Follow create(final Member follower, final Member following) {
        return new Follow(follower, following);
    }
}
