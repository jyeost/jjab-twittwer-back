package jjabtwitter.follow.repository;

import jjabtwitter.follow.domain.Follow;
import jjabtwitter.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    boolean existsByFollowerAndFollowing(final Member follower, final Member following);
}
