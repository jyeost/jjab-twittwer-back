package jjabtwitter.image;

import jjabtwitter.global.exception.BusinessLogicException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static jjabtwitter.global.exception.ExceptionInformation.IMAGE_UPLOAD_FAIL;

@RequiredArgsConstructor
@Component
public class ImageUploadService {

    private final ImageInfoFactory imageInfoFactory;

    @Value("${image.root-dir}")
    private String rootDirectory;


    public List<String> uploadAll(final List<MultipartFile> images) {
        // TODO: 사진을 5장 저장한다고 했을떄, 4번째 사진에서 확장자명이 맞지 않거나 업로드 실패하면,,, 고아객체가 생길수도 잇음....
        return images.stream()
                .map(this::uploadOne)
                .toList();
    }

    public String uploadOne(final MultipartFile imageFile) {
        final String imageFileName = imageInfoFactory.create(imageFile);
        uploadRealStorage(imageFile, imageFileName);
        return imageFileName;
    }

    private void uploadRealStorage(final MultipartFile imageFile, final String imageFileName) {
        try {
            final Path path = Paths.get(rootDirectory + imageFileName);
            imageFile.transferTo(path);
        } catch (IOException e) {
            throw new BusinessLogicException(IMAGE_UPLOAD_FAIL);
        }
    }
}
