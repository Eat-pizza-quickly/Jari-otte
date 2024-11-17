package com.eatpizzaquickly.concertservice.util;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.eatpizzaquickly.concertservice.dto.ImageFile;
import com.eatpizzaquickly.concertservice.exception.ImageException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

@RequiredArgsConstructor
@Component
public class ImageUtil {

    private final AmazonS3 s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String uploadImage(final ImageFile imageFile) {
        final String path = imageFile.getUniqueName();
        final ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(imageFile.getContentType());
        metadata.setContentLength(imageFile.getSize());

        try (final InputStream inputStream = imageFile.getInputStream()) {
            s3Client.putObject(bucket, path, inputStream, metadata);
        } catch (final AmazonServiceException e) {
            throw new ImageException("잘못된 이미지 경로입니다." + e.getMessage());
        } catch (final IOException e) {
            throw new ImageException("잘못된 이미지입니다.");
        }
        return String.format("https://%s.s3.amazonaws.com/%s", bucket, path);
    }

    public void deleteOriginalImage(String originalUrl) {
        if (originalUrl == null || originalUrl.isEmpty()) {
            return;
        }

        try {
            new URL(originalUrl);
            String targetName = originalUrl.substring(originalUrl.lastIndexOf("/") + 1);
            s3Client.deleteObject(bucket, targetName);
        } catch (MalformedURLException e) {
            throw new ImageException("잘못된 이미지 URL 입니다.");
        }
    }
}