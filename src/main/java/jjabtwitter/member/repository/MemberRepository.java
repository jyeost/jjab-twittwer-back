package jjabtwitter.member.repository;

import jjabtwitter.member.domain.CustomId;
import jjabtwitter.member.domain.Member;
import jjabtwitter.member.domain.Password;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByCustomId(CustomId customId);

    Optional<Member> findByCustomIdAndPassword(CustomId customId, Password password);
}
