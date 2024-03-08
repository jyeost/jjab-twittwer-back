package jjabtwitter.image;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ImageFileNameUUIDStrategy implements ImageFileNameStrategy{

    @Override
    public String createName(final String originalFileName) {
        String ext = ImageExtension.extractExt(originalFileName);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }
}
