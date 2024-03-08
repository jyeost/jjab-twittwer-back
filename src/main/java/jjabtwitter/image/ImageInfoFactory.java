package jjabtwitter.image;

import jjabtwitter.global.exception.ClientException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import static jjabtwitter.global.exception.ExceptionInformation.IMAGE_EXTENSION_INVALID;

@RequiredArgsConstructor
@Component
public class ImageInfoFactory {

    private final ImageFileNameStrategy imageFileNameStrategy;

    public String create(final MultipartFile image) {
        final String originalFileName = image.getOriginalFilename();
        validateExtension(originalFileName);
        return imageFileNameStrategy.createName(originalFileName);
    }

    public void validateExtension(final String originalFileName) {
        final String ext = ImageExtension.extractExt(originalFileName);
        if (ImageExtension.contains(ext)) {
            return;
        }
        throw new ClientException(IMAGE_EXTENSION_INVALID);
    }
}
