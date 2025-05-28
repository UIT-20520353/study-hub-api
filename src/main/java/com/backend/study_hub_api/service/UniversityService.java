package com.backend.study_hub_api.service;

import com.backend.study_hub_api.dto.UniversityDTO;
import com.backend.study_hub_api.model.University;

import java.util.List;

public interface UniversityService {
    UniversityDTO.UniversityResponse createUniversity(UniversityDTO.CreateUniversityRequest request);

    UniversityDTO.UniversityResponse getUniversityById(Long id);

    List<UniversityDTO.UniversityResponse> getAllUniversities();

    UniversityDTO.UniversityResponse updateUniversity(Long id, UniversityDTO.UpdateUniversityRequest request);

    void deleteUniversity(Long id);

    UniversityDTO.UniversityResponse mapToDTO(University university);

    University getUniversityByIdOrThrow(Long id);
}