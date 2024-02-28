package jjabtwitter.member.repository;

import jjabtwitter.member.domain.CustomId;
import jjabtwitter.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByCustomId(CustomId customId);
}
