package com.backend.study_hub_api.service;

import com.backend.study_hub_api.dto.FileUploadDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileUploadService {

    /**
     * Upload single file to S3
     * @param file MultipartFile to upload
     * @param folder Folder path in S3 bucket (e.g., "products", "avatars")
     * @return FileUploadResponse containing file URL and metadata
     */
    FileUploadDTO.FileUploadResponse uploadFile(MultipartFile file, String folder, String[] allowedTypes);

    /**
     * Upload multiple files to S3
     * @param files List of MultipartFiles to upload
     * @param folder Folder path in S3 bucket
     * @return MultipleFileUploadResponse containing list of uploaded files
     */
    FileUploadDTO.MultipleFileUploadResponse uploadMultipleFiles(List<MultipartFile> files, String folder, String[] allowedTypes);

    /**
     * Delete file from S3
     * @param fileUrl Complete S3 file URL
     * @return true if deleted successfully, false otherwise
     */
    boolean deleteFile(String fileUrl);

    /**
     * Generate pre-signed URL for direct upload (optional feature)
     * @param fileName File name
     * @param folder Folder path
     * @param contentType MIME type
     * @return Pre-signed URL for upload
     */
    String generatePresignedUploadUrl(String fileName, String folder, String contentType);

    /**
     * Validate file type and size
     * @param file MultipartFile to validate
     * @param allowedTypes Array of allowed MIME types
     * @param maxSizeInMB Maximum file size in MB
     * @return true if valid, throws exception if invalid
     */
    boolean validateFile(MultipartFile file, String[] allowedTypes, int maxSizeInMB);

}