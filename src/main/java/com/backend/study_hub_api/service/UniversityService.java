package com.backend.study_hub_api.service;

import com.backend.study_hub_api.dto.UniversityDTO;
import com.backend.study_hub_api.dto.common.PaginationDTO;
import com.backend.study_hub_api.dto.criteria.UniversityFilterCriteria;
import com.backend.study_hub_api.model.University;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UniversityService {

    UniversityDTO.UniversityResponse createUniversity(UniversityDTO.CreateUniversityRequest request,
                                                      MultipartFile logoFile);

    UniversityDTO.UniversityResponse getUniversityById(Long id);

    PaginationDTO<UniversityDTO.UniversityResponse> getAllUniversities(Pageable pageable);

    PaginationDTO<UniversityDTO.UniversityResponse> getUniversitiesWithFilter(UniversityFilterCriteria criteria);

    List<UniversityDTO.UniversityResponse> getActiveUniversities();

    UniversityDTO.UniversityResponse updateUniversity(Long id,
                                                      UniversityDTO.UpdateUniversityRequest request,
                                                      MultipartFile logoFile);

    UniversityDTO.UniversityResponse updateUniversityStatus(Long id,
                                                            UniversityDTO.UpdateStatusRequest request);

    void deleteUniversity(Long id);

    UniversityDTO.UniversityResponse mapToDTO(University university);

    University getUniversityByIdOrThrow(Long id);

    List<UniversityDTO.UniversityResponse> getAllActiveUniversities();
}