package com.backend.study_hub_api.controller;

import com.backend.study_hub_api.dto.FileUploadDTO;
import com.backend.study_hub_api.service.FileUploadService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/user/files")
@RequiredArgsConstructor
@Tag(name = "File Upload", description = "Endpoints for file upload and management")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FileUploadController {

    FileUploadService fileUploadService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FileUploadDTO.FileUploadResponse> uploadFile(
            @Parameter(description = "File to upload", required = true)
            @RequestParam("file") MultipartFile file,

            @Parameter(description = "Folder path in S3 bucket", example = "products")
            @RequestParam(value = "folder", defaultValue = "uploads") String folder) {

        FileUploadDTO.FileUploadResponse response = fileUploadService.uploadFile(file, folder);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/upload-multiple", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FileUploadDTO.MultipleFileUploadResponse> uploadMultipleFiles(
            @Parameter(description = "Files to upload (max 10)", required = true)
            @RequestParam("files") List<MultipartFile> files,

            @Parameter(description = "Folder path in S3 bucket", example = "products")
            @RequestParam(value = "folder", defaultValue = "uploads") String folder) {

        FileUploadDTO.MultipleFileUploadResponse response = fileUploadService.uploadMultipleFiles(files, folder);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/upload-product-images")
    public ResponseEntity<FileUploadDTO.MultipleFileUploadResponse> uploadProductImages(
            @Parameter(description = "Product image files", required = true)
            @RequestParam("files") List<MultipartFile> files) {

        FileUploadDTO.MultipleFileUploadResponse response = fileUploadService.uploadMultipleFiles(files, "products");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/upload-avatar")
    public ResponseEntity<FileUploadDTO.FileUploadResponse> uploadAvatar(
            @Parameter(description = "Avatar image file", required = true)
            @RequestParam("file") MultipartFile file) {

        FileUploadDTO.FileUploadResponse response = fileUploadService.uploadFile(file, "avatars");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteFile(
            @Parameter(description = "Complete S3 file URL", required = true)
            @RequestParam("fileUrl") String fileUrl) {

        boolean deleted = fileUploadService.deleteFile(fileUrl);
        if (deleted) {
            return ResponseEntity.ok("File deleted successfully");
        } else {
            return ResponseEntity.badRequest().body("Failed to delete file");
        }
    }

    @PostMapping("/presigned-url")
    public ResponseEntity<String> generatePresignedUrl(
            @Parameter(description = "File name", required = true)
            @RequestParam("fileName") String fileName,

            @Parameter(description = "Folder path", example = "products")
            @RequestParam(value = "folder", defaultValue = "uploads") String folder,

            @Parameter(description = "Content type", example = "image/jpeg")
            @RequestParam("contentType") String contentType) {

        // Return error message instead of calling the service
        return ResponseEntity.badRequest()
                             .body("Presigned URL không được hỗ trợ. Vui lòng sử dụng endpoint upload thông thường.");
    }
}