package com.backend.study_hub_api.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileStorageService {
    String uploadFile(MultipartFile file, String prefix);
    List<String> uploadFiles(List<MultipartFile> files, String prefix);
    void deleteFile(String fileUrl);
}
