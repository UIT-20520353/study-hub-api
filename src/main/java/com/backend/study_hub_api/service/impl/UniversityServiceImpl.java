package com.backend.study_hub_api.service.impl;

import com.backend.study_hub_api.dto.UniversityDTO;
import com.backend.study_hub_api.helper.exception.BadRequestException;
import com.backend.study_hub_api.model.University;
import com.backend.study_hub_api.repository.UniversityRepository;
import com.backend.study_hub_api.service.UniversityService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.backend.study_hub_api.helper.constant.Message.*;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UniversityServiceImpl implements UniversityService {

    UniversityRepository universityRepository;

    @Override
    public UniversityDTO.UniversityResponse createUniversity(UniversityDTO.CreateUniversityRequest request) {
        if (universityRepository.existsByName(request.getName())) {
            throw new BadRequestException(UNIVERSITY_ALREADY_EXISTS);
        }

        University university = University.builder()
                                          .name(request.getName())
                                          .shortName(request.getShortName())
                                          .address(request.getAddress())
                                          .city(request.getCity())
                                          .website(request.getWebsite())
                                          .logoUrl(request.getLogoUrl())
                                          .description(request.getDescription())
                                          .isActive(true)
                                          .build();

        return mapToDTO(universityRepository.save(university));
    }

    @Override
    public UniversityDTO.UniversityResponse getUniversityById(Long id) {
        return universityRepository.findById(id)
                                   .map(this::mapToDTO)
                                   .orElseThrow(() -> new BadRequestException(UNIVERSITY_NOT_FOUND));
    }

    @Override
    public List<UniversityDTO.UniversityResponse> getAllUniversities() {
        return universityRepository.findAll().stream()
                                   .map(this::mapToDTO)
                                   .collect(Collectors.toList());
    }

    @Override
    public UniversityDTO.UniversityResponse updateUniversity(Long id, UniversityDTO.UpdateUniversityRequest request) {
        University university = universityRepository.findById(id)
                                                    .orElseThrow(() -> new BadRequestException(UNIVERSITY_NOT_FOUND));

        university.setName(request.getName());
        university.setShortName(request.getShortName());
        university.setAddress(request.getAddress());
        university.setCity(request.getCity());
        university.setWebsite(request.getWebsite());
        university.setLogoUrl(request.getLogoUrl());
        university.setDescription(request.getDescription());

        if (request.getIsActive() != null) {
            university.setIsActive(request.getIsActive());
        }

        return mapToDTO(universityRepository.save(university));
    }

    @Override
    public void deleteUniversity(Long id) {
        University university = universityRepository.findById(id)
                                                    .orElseThrow(() -> new BadRequestException(UNIVERSITY_NOT_FOUND));

        universityRepository.delete(university);
    }

    @Override
    public UniversityDTO.UniversityResponse mapToDTO(University university) {
        return UniversityDTO.UniversityResponse.builder()
                                               .id(university.getId())
                                               .name(university.getName())
                                               .shortName(university.getShortName())
                                               .address(university.getAddress())
                                               .city(university.getCity())
                                               .website(university.getWebsite())
                                               .logoUrl(university.getLogoUrl())
                                               .description(university.getDescription())
                                               .isActive(university.getIsActive())
                                               .createdAt(university.getCreatedAt())
                                               .updatedAt(university.getUpdatedAt())
                                               .build();
    }

    @Override
    public University getUniversityByIdOrThrow(Long id) {
        return universityRepository.findById(id)
                                   .orElseThrow(() -> new BadRequestException(UNIVERSITY_NOT_FOUND));
    }
}