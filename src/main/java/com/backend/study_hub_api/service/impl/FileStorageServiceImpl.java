package com.backend.study_hub_api.service.impl;

import com.backend.study_hub_api.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileStorageServiceImpl implements FileStorageService {

    private final S3Client s3Client;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    @Override
    public String uploadFile(MultipartFile file, String prefix) {
        try {
            String fileExtension = getFileExtension(file.getOriginalFilename());
            String key = prefix + "/" + UUID.randomUUID() + fileExtension;

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            return String.format("https://%s.s3.amazonaws.com/%s", bucketName, key);
        } catch (IOException e) {
            log.error("Error uploading file to S3", e);
            throw new RuntimeException("Error uploading file to S3", e);
        }
    }

    @Override
    public List<String> uploadFiles(List<MultipartFile> files, String prefix) {
        List<String> fileUrls = new ArrayList<>();
        for (MultipartFile file : files) {
            fileUrls.add(uploadFile(file, prefix));
        }
        return fileUrls;
    }

    @Override
    public void deleteFile(String fileUrl) {
        try {
            String key = fileUrl.replace("https://" + bucketName + ".s3.amazonaws.com/", "");

            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            s3Client.deleteObject(deleteObjectRequest);
        } catch (Exception e) {
            log.error("Error deleting file from S3", e);
            throw new RuntimeException("Error deleting file from S3", e);
        }
    }

    private String getFileExtension(String filename) {
        if (filename == null || filename.lastIndexOf(".") == -1) {
            return "";
        }
        return filename.substring(filename.lastIndexOf("."));
    }
}
