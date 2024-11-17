package com.eatpizzaquickly.concertservice.dto;

import com.eatpizzaquickly.concertservice.exception.ImageException;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Getter
public class ImageFile {

    private final MultipartFile file;
    private final String uniqueName;

    public ImageFile(MultipartFile file) {
        validateNullImage(file);
        this.file = file;
        this.uniqueName = createUniqueName(file);
    }

    private void validateNullImage(final MultipartFile file) {
        if (file.isEmpty()) {
            throw new ImageException("이미지가 존재하지 않습니다.");
        }
    }

    private String createUniqueName(final MultipartFile image) {
        String originalFilename = image.getOriginalFilename();
        return UUID.randomUUID().toString() + "_" + originalFilename;
    }

    public String getContentType() {
        return this.file.getContentType();
    }

    public long getSize() {
        return this.file.getSize();
    }

    public InputStream getInputStream() throws IOException {
        return this.file.getInputStream();
    }
}