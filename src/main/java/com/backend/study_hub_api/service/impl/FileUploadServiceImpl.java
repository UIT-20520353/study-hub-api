package com.backend.study_hub_api.service.impl;

import com.backend.study_hub_api.config.ApplicationProperties;
import com.backend.study_hub_api.dto.FileUploadDTO;
import com.backend.study_hub_api.helper.exception.BadRequestException;
import com.backend.study_hub_api.helper.exception.InternalException;
import com.backend.study_hub_api.service.FileUploadService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.backend.study_hub_api.helper.constant.Message.*;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class FileUploadServiceImpl implements FileUploadService {

    S3Client s3Client;
    ApplicationProperties applicationProperties;

    public static final String[] DEFAULT_ALLOWED_IMAGE_TYPES = {
            "image/jpeg", "image/jpg", "image/png", "image/gif", "image/webp"
    };

    private static final String[] DEFAULT_ALLOWED_DOCUMENT_TYPES = {
            "application/pdf", "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "text/plain"
    };

    private static final int DEFAULT_MAX_FILE_SIZE_MB = 10;

    @Override
    public FileUploadDTO.FileUploadResponse uploadFile(MultipartFile file, String folder,
                                                      String[] allowedTypes) {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException(FILE_REQUIRED_ERROR);
        }

        // Validate file
        validateFile(file, allowedTypes, DEFAULT_MAX_FILE_SIZE_MB);

        try {
            String fileName = generateUniqueFileName(file.getOriginalFilename());
            String key = buildS3Key(folder, fileName);
            String bucketName = applicationProperties.getAws().s3Bucket();

            // Upload to S3
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                                                                .bucket(bucketName)
                                                                .key(key)
                                                                .contentType(file.getContentType())
                                                                .contentLength(file.getSize())
                                                                .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            // Generate public URL
            String fileUrl = generatePublicUrl(bucketName, key);

            log.info("File uploaded successfully: {}", fileUrl);

            return FileUploadDTO.FileUploadResponse.builder()
                                                   .fileUrl(fileUrl)
                                                   .originalFilename(file.getOriginalFilename())
                                                   .fileSize(file.getSize())
                                                   .contentType(file.getContentType())
                                                   .uploadedAt(Instant.now())
                                                   .build();

        } catch (IOException e) {
            log.error("Error uploading file to S3", e);
            throw new InternalException();
        }
    }

    @Override
    public FileUploadDTO.MultipleFileUploadResponse uploadMultipleFiles(List<MultipartFile> files, String folder, String[] allowedTypes) {
        if (files == null || files.isEmpty()) {
            throw new BadRequestException(FILE_REQUIRED_ERROR);
        }

        if (files.size() > 10) {
            throw new BadRequestException(MAX_FILE_UPLOAD_ERROR);
        }

        List<FileUploadDTO.FileUploadResponse> uploadedFiles = files.stream()
                                                                    .filter(file -> !file.isEmpty())
                                                                    .map(file -> uploadFile(file, folder, allowedTypes))
                                                                    .collect(Collectors.toList());

        return FileUploadDTO.MultipleFileUploadResponse.builder()
                                                       .files(uploadedFiles)
                                                       .totalFiles(uploadedFiles.size())
                                                       .uploadedAt(Instant.now())
                                                       .build();
    }

    @Override
    public boolean deleteFile(String fileUrl) {
        try {
            if (fileUrl == null || fileUrl.isEmpty()) {
                return false;
            }

            String key = extractKeyFromUrl(fileUrl);
            String bucketName = applicationProperties.getAws().s3Bucket();

            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                                                                         .bucket(bucketName)
                                                                         .key(key)
                                                                         .build();

            s3Client.deleteObject(deleteObjectRequest);
            log.info("File deleted successfully: {}", fileUrl);
            return true;

        } catch (Exception e) {
            log.error("Error deleting file from S3: {}", fileUrl, e);
            return false;
        }
    }

    @Override
    public String generatePresignedUploadUrl(String fileName, String folder, String contentType) {
        // For simplicity, we'll return a standard upload endpoint instead of presigned URL
        // In production, you might want to implement proper presigned URLs with additional dependencies
        log.warn("Presigned URL generation not implemented. Use regular upload endpoints instead.");
        throw new BadRequestException("Presigned URL không được hỗ trợ. Vui lòng sử dụng endpoint upload thông thường.");
    }

    @Override
    public boolean validateFile(MultipartFile file, String[] allowedTypes, int maxSizeInMB) {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException(FILE_REQUIRED_ERROR);
        }

        // Check file size
        long maxSizeInBytes = maxSizeInMB * 1024L * 1024L;
        if (file.getSize() > maxSizeInBytes) {
            throw new BadRequestException(FILE_MAX_SIZE_ERROR);
        }

        // Check content type
        String contentType = file.getContentType();
        if (contentType == null || !Arrays.asList(allowedTypes).contains(contentType)) {
            throw new BadRequestException(FILE_TYPE_NOT_SUPPORTED_ERROR);
        }

        // Check filename
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.trim().isEmpty()) {
            throw new BadRequestException(FILE_NAME_INVALID_ERROR);
        }

        return true;
    }

    // Helper methods
    private String generateUniqueFileName(String originalFilename) {
        if (originalFilename == null) {
            return UUID.randomUUID().toString();
        }

        String extension = "";
        int lastDotIndex = originalFilename.lastIndexOf('.');
        if (lastDotIndex > 0) {
            extension = originalFilename.substring(lastDotIndex);
        }

        return UUID.randomUUID().toString() + extension;
    }

    private String buildS3Key(String folder, String fileName) {
        if (folder == null || folder.trim().isEmpty()) {
            return "uploads/" + fileName;
        }
        return folder.trim() + "/" + fileName;
    }

    private String generatePublicUrl(String bucketName, String key) {
        String region = applicationProperties.getAws().region();
        return String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, key);
    }

    private String extractKeyFromUrl(String fileUrl) {
        // Extract key from URL format: https://bucket.s3.region.amazonaws.com/key
        try {
            String bucketName = applicationProperties.getAws().s3Bucket();
            String region = applicationProperties.getAws().region();
            String prefix = String.format("https://%s.s3.%s.amazonaws.com/", bucketName, region);

            if (fileUrl.startsWith(prefix)) {
                return fileUrl.substring(prefix.length());
            }

            throw new BadRequestException(URL_INVALID_ERROR);
        } catch (Exception e) {
            log.error("Error extracting key from URL: {}", fileUrl, e);
            throw new BadRequestException(URL_INVALID_ERROR);
        }
    }
}