package com.eatpizzaquickly.jariotte.domain.image.service;

import com.eatpizzaquickly.jariotte.domain.image.dto.ImageFile;
import com.eatpizzaquickly.jariotte.domain.image.dto.ImageResponse;
import com.eatpizzaquickly.jariotte.domain.image.exception.ImageException;
import com.eatpizzaquickly.jariotte.domain.image.infrastructure.ImageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class ImageService {

    private final ImageUtil imageUtil;

    public ImageResponse save(final MultipartFile image) {
        validateSizeOfImage(image);
        final ImageFile imageFile = new ImageFile(image);
        final String imageUrl = uploadImage(imageFile);
        return new ImageResponse(imageUrl);
    }

    private String uploadImage(final ImageFile imageFile) {
        try {
            return imageUtil.uploadImage(imageFile);
        } catch (final ImageException e) {
            throw e;
        }
    }

    private void validateSizeOfImage(final MultipartFile image) {
        if (image == null || image.isEmpty()) {
            throw new ImageException("이미지가 존재하지 않습니다.");
        }
    }
}
