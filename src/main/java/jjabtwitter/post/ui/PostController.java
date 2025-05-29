package jjabtwitter.post.ui;

import jjabtwitter.member.application.dto.MemberId;
import jjabtwitter.member.ui.Auth;
import jjabtwitter.post.application.PostService;
import jjabtwitter.post.application.dto.PostRequest;
import jjabtwitter.post.domain.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/posts")
@RestController
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<Void> createPost(@ModelAttribute PostRequest postRequest, @Auth MemberId memberId) {
        postService.createPost(postRequest, memberId);
        return ResponseEntity.created(URI.create("/post"))
                .build();
    }

    @GetMapping
    public ResponseEntity<List<Post>> getPosts(@Auth MemberId memberId) {
        return ResponseEntity.ok().body(postService.getPosts(memberId));
    }
}
