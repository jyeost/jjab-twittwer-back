package jjabtwitter.post.application.dto;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record PostRequest(
        String content,
        List<MultipartFile> images
) {
}
