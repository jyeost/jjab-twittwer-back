package jjabtwitter.follow.repository;

import jjabtwitter.follow.domain.Follow;
import jjabtwitter.member.domain.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    boolean existsByFollowerAndFollowing(final Member follower, final Member following);

    @Modifying
    @Query("delete from Follow f where f.follower.id = :followerId and f.following.id = :followingId")
    int deleteOneByFollowerIdAndFollowingId(
            @Param("followerId") final Long followerId,
            @Param("followingId") final Long followingId
    );

    @EntityGraph(attributePaths = "following")
    @Query("select f from Follow f where f.follower.id = :followerId")
    List<Follow> findAllFollowingMembers(@Param("followerId") final Long followerId);

    @EntityGraph(attributePaths = "follower")
    @Query("select f from Follow f where f.following.id = :followingId")
    List<Follow> findAllFollowerMembers(@Param("followingId") final Long followingId);

    @Query("select f.follower.id from Follow f where f.follower.id in (:allFollowingMemberIds) and f.following.id = :followerId")
    List<Long> findFollowBack(@Param("allFollowingMemberIds") final List<Long> allFollowingMemberIds, @Param("followerId") final Long followerId);

    @Query("select f.following.id from Follow f where f.following.id in (:allFollowerMemberIds) and f.follower.id = :followeeId")
    List<Long> findFollowing(@Param("allFollowerMemberIds") final List<Long> allFollowerMemberIds, @Param("followeeId") final Long followeeId);
}
