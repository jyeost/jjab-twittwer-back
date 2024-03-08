package jjabtwitter.image;

import jjabtwitter.support.IntegrationTest;
import jjabtwitter.support.TestFileCleaner;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
@SuppressWarnings("NonAsciiCharacters")
class ImageUploadServiceTest implements TestFileCleaner {

    @Autowired
    private ImageUploadService imageUploadService;

    @Value("${image.root-dir}")
    private String rootDirectory;

    @Test
    void 이미지가_올바른_경로에_저장되는지_확인한다() throws IOException {
        // given
        final String baseDirectory = rootDirectory;
        final MockMultipartFile image = new MockMultipartFile("images", "test1.jpg", "image/jpg", ImageUploadService.class.getResourceAsStream("/static/uploadtest/image/test1.jpg"));

        assertThat(new File(baseDirectory).list()).containsOnly("dummy.txt");
        assertThat(new File(baseDirectory).list()).hasSize(1);

        // when
        final String save = imageUploadService.uploadOne(image);

        // then
        assertThat(new File(rootDirectory).list()).contains(save);
        assertThat(new File(rootDirectory).list()).hasSize(2);
    }

    @Test
    void 이미지_여러개가_업로드되는지_확인한다() throws IOException {
        // given
        final String baseDirectory = rootDirectory;
        final MockMultipartFile image1 = new MockMultipartFile("images", "test1.jpg", "image/jpg", ImageUploadService.class.getResourceAsStream("/static/uploadtest/image/test1.jpg"));
        final MockMultipartFile image2 = new MockMultipartFile("images", "test2.JPG", "image/jpg", ImageUploadService.class.getResourceAsStream("/static/uploadtest/image/test2.JPG"));
        final MockMultipartFile image3 = new MockMultipartFile("images", "test3.png", "image/png", ImageUploadService.class.getResourceAsStream("/static/uploadtest/image/test3.png"));
        final MockMultipartFile image4 = new MockMultipartFile("images", "test4.jpeg", "image/jpeg", ImageUploadService.class.getResourceAsStream("/static/uploadtest/image/test4.jpeg"));

        assertThat(new File(baseDirectory).list()).containsOnly("dummy.txt");
        assertThat(new File(baseDirectory).list()).hasSize(1);

        // when
        final List<String> save = imageUploadService.uploadAll(List.of(image1, image2, image3, image4));

        // then
        assertThat(new File(baseDirectory).list()).containsAll(save);
        assertThat(new File(baseDirectory).list()).hasSize(5);
    }

}
