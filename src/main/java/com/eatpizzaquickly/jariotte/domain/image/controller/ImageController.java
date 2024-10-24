package com.eatpizzaquickly.jariotte.domain.image.controller;

import com.eatpizzaquickly.jariotte.domain.image.dto.ImageResponse;
import com.eatpizzaquickly.jariotte.domain.image.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RequestMapping("/images")
@RestController
public class ImageController {

    private final ImageService imageService;

    @PostMapping
    public ResponseEntity<ImageResponse> uploadImage(@RequestPart final MultipartFile image) {
        final ImageResponse imageResponse = imageService.save(image);
        return ResponseEntity.ok(imageResponse);
    }
}
