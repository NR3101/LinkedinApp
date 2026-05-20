package com.nr3101.uploaderservice.controller;

import com.nr3101.uploaderservice.service.UploaderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
@Slf4j
public class UploaderController {

    private final UploaderService uploaderService;

    @PostMapping
    public ResponseEntity<String> uploadFile(@RequestParam MultipartFile file) {
        log.info("Received file upload request: {}", file.getOriginalFilename());
        String resultUrl = uploaderService.upload(file);

        return ResponseEntity.ok(resultUrl);
    }
}
