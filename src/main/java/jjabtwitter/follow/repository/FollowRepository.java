package jjabtwitter.follow.repository;

import jjabtwitter.follow.domain.Follow;
import jjabtwitter.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    boolean existsByFollowerAndFollowing(final Member follower, final Member following);

    @Modifying
    @Query("delete from Follow f where f.follower.id = :followerId and f.following.id = :followingId")
    int deleteOneByFollowerIdAndFollowingId(
            @Param("followerId") final Long followerId,
            @Param("followingId") final Long followingId
    );
}
