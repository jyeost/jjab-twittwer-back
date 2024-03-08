package jjabtwitter.post.repository;

import jjabtwitter.post.domain.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostImageRepository extends JpaRepository<PostImage, Long> {

}
