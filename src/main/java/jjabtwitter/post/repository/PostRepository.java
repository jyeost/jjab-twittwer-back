package jjabtwitter.post.repository;

import jjabtwitter.member.domain.Member;
import jjabtwitter.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByMember(Member member);

    List<Post> findByMemberIn(List<Member> members);
}
