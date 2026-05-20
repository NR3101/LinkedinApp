package com.nr3101.uploaderservice.service.impl;

import com.cloudinary.Cloudinary;
import com.nr3101.uploaderservice.service.UploaderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CloudinaryUploaderService implements UploaderService {

    private final Cloudinary cloudinary;

    @Override
    public String upload(MultipartFile file) {
        log.info("Uploading file: {}", file.getOriginalFilename());

        try {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), Map.of());
            log.info("File uploaded successfully: {}", uploadResult.get("secure_url"));
            return uploadResult.get("secure_url").toString();
        } catch (IOException e) {
            log.error("Failed to upload file to Cloudinary", e);
            throw new RuntimeException("Failed to upload file", e);
        }
    }
}
