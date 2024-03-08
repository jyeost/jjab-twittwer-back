package jjabtwitter.post.ui;

import jjabtwitter.member.application.dto.MemberId;
import jjabtwitter.member.ui.Auth;
import jjabtwitter.post.application.PostService;
import jjabtwitter.post.application.dto.PostRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

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
}
