package jjabtwitter.post.domain;

import jakarta.persistence.Embeddable;
import jakarta.persistence.OneToMany;
import jjabtwitter.global.exception.ClientException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jjabtwitter.global.exception.ExceptionInformation.POST_IMAGE_COUNT_INVALID;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class PostImages {

    public static final int MAX_IMAGE_COUNT = 4;

    @OneToMany(mappedBy = "post")
    private List<PostImage> postImages = new ArrayList<>();

    private PostImages(final List<PostImage> postImages) {
        validateImageCount(postImages.size());
        this.postImages = postImages;
    }

    public static PostImages create() {
        return new PostImages(new ArrayList<>());
    }

    public static PostImages create(final List<String> postImages, final Post post) {
        final List<PostImage> images = postImages.stream()
                .map(postImage -> PostImage.create(postImage, post))
                .toList();
        return new PostImages(images);
    }

    public void validateImageCount(final int imageCount) {
        if (imageCount > MAX_IMAGE_COUNT) {
            throw new ClientException(POST_IMAGE_COUNT_INVALID);
        }
    }

    public List<PostImage> getPostImages() {
        return postImages;
    }
}
