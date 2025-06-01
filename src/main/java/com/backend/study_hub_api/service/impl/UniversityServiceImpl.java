package com.backend.study_hub_api.service.impl;

import com.backend.study_hub_api.dto.UniversityDTO;
import com.backend.study_hub_api.dto.common.PaginationDTO;
import com.backend.study_hub_api.dto.criteria.UniversityFilterCriteria;
import com.backend.study_hub_api.helper.enumeration.UniversityStatus;
import com.backend.study_hub_api.helper.exception.BadRequestException;
import com.backend.study_hub_api.helper.util.PaginationUtils;
import com.backend.study_hub_api.model.University;
import com.backend.study_hub_api.repository.UniversityRepository;
import com.backend.study_hub_api.service.BaseFilterService;
import com.backend.study_hub_api.service.FileUploadService;
import com.backend.study_hub_api.service.UniversityService;
import com.backend.study_hub_api.specification.BaseSpecificationBuilder;
import com.backend.study_hub_api.specification.UniversitySpecification;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.function.Function;

import static com.backend.study_hub_api.helper.constant.Message.*;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UniversityServiceImpl extends BaseFilterService<University, Long, UniversityFilterCriteria, UniversityDTO.UniversityResponse>
        implements UniversityService {

    UniversityRepository universityRepository;
    UniversitySpecification universitySpecification;
    FileUploadService fileUploadService;

    private static final String[] ALLOWED_IMAGE_TYPES = {
            "image/jpeg", "image/jpg", "image/png"
    };

    @Override
    @Transactional
    public UniversityDTO.UniversityResponse createUniversity(UniversityDTO.CreateUniversityRequest request,
                                                             MultipartFile logoFile) {
        if (universityRepository.existsByEmailDomain(request.getEmailDomain())) {
            throw new BadRequestException(UNIVERSITY_EMAIL_DOMAIN_EXISTS_ERROR);
        }

        if (universityRepository.existsByName(request.getName())) {
            throw new BadRequestException(UNIVERSITY_NAME_EXISTS_ERROR);
        }

        String logoUrl = null;
        if (logoFile != null && !logoFile.isEmpty()) {
            logoUrl = fileUploadService.uploadFile(logoFile, "universities/logos", ALLOWED_IMAGE_TYPES)
                                       .getFileUrl();
        }

        University university = University.builder()
                                          .name(request.getName())
                                          .shortName(request.getShortName())
                                          .address(request.getAddress())
                                          .emailDomain(request.getEmailDomain())
                                          .city(request.getCity())
                                          .website(request.getWebsite())
                                          .logoUrl(logoUrl)
                                          .description(request.getDescription())
                                          .status(request.getStatus())
                                          .isActive(request.getStatus() == UniversityStatus.ACTIVE)
                                          .build();

        University savedUniversity = universityRepository.save(university);
        return mapToDTO(savedUniversity);
    }

    @Override
    public UniversityDTO.UniversityResponse getUniversityById(Long id) {
        University university = getUniversityByIdOrThrow(id);
        return mapToDTO(university);
    }

    @Override
    public PaginationDTO<UniversityDTO.UniversityResponse> getAllUniversities(Pageable pageable) {
        Page<UniversityDTO.UniversityResponse> universities = universityRepository.findAll(pageable)
                                                                                  .map(this::mapToDTO);
        return PaginationUtils.createPaginationResponse(universities);
    }

    @Override
    public PaginationDTO<UniversityDTO.UniversityResponse> getUniversitiesWithFilter(UniversityFilterCriteria criteria) {
        return getWithFilter(criteria);
    }

    @Override
    public List<UniversityDTO.UniversityResponse> getActiveUniversities() {
        return universityRepository.findByStatusOrderByNameAsc(UniversityStatus.ACTIVE)
                                   .stream()
                                   .map(this::mapToDTO)
                                   .toList();
    }

    @Override
    @Transactional
    public UniversityDTO.UniversityResponse updateUniversity(Long id,
                                                             UniversityDTO.UpdateUniversityRequest request,
                                                             MultipartFile logoFile) {
        University university = getUniversityByIdOrThrow(id);

        if (universityRepository.existsByEmailDomainAndIdNot(request.getEmailDomain(), id)) {
            throw new BadRequestException(UNIVERSITY_EMAIL_DOMAIN_EXISTS_ERROR);
        }

        if (universityRepository.existsByNameAndIdNot(request.getName(), id)) {
            throw new BadRequestException(UNIVERSITY_NAME_EXISTS_ERROR);
        }

        String logoUrl = university.getLogoUrl();
        if (logoFile != null && !logoFile.isEmpty()) {
            if (logoUrl != null) {
                fileUploadService.deleteFile(logoUrl);
            }
            logoUrl = fileUploadService.uploadFile(logoFile, "universities/logos", ALLOWED_IMAGE_TYPES)
                                       .getFileUrl();
        }

        university.setName(request.getName());
        university.setShortName(request.getShortName());
        university.setAddress(request.getAddress());
        university.setEmailDomain(request.getEmailDomain());
        university.setCity(request.getCity());
        university.setWebsite(request.getWebsite());
        university.setLogoUrl(logoUrl);
        university.setDescription(request.getDescription());

        if (request.getStatus() != null) {
            university.setStatus(request.getStatus());
            university.setIsActive(request.getStatus() == UniversityStatus.ACTIVE);
        }

        University savedUniversity = universityRepository.save(university);
        return mapToDTO(savedUniversity);
    }

    @Override
    @Transactional
    public UniversityDTO.UniversityResponse updateUniversityStatus(Long id,
                                                                   UniversityDTO.UpdateStatusRequest request) {
        University university = getUniversityByIdOrThrow(id);

        university.setStatus(request.getStatus());
        university.setIsActive(request.getStatus() == UniversityStatus.ACTIVE);

        University savedUniversity = universityRepository.save(university);
        return mapToDTO(savedUniversity);
    }

    @Override
    @Transactional
    public void deleteUniversity(Long id) {
        University university = getUniversityByIdOrThrow(id);

        university.setStatus(UniversityStatus.DELETED);
        university.setIsActive(false);

        universityRepository.save(university);
    }

    @Override
    public UniversityDTO.UniversityResponse mapToDTO(University university) {
        return UniversityDTO.UniversityResponse.builder()
                                               .id(university.getId())
                                               .name(university.getName())
                                               .shortName(university.getShortName())
                                               .address(university.getAddress())
                                               .emailDomain(university.getEmailDomain())
                                               .city(university.getCity())
                                               .website(university.getWebsite())
                                               .logoUrl(university.getLogoUrl())
                                               .description(university.getDescription())
                                               .status(university.getStatus())
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

    @Override
    protected JpaSpecificationExecutor<University> getRepository() {
        return universityRepository;
    }

    @Override
    protected BaseSpecificationBuilder<University, UniversityFilterCriteria> getSpecificationBuilder() {
        return universitySpecification;
    }

    @Override
    protected Function<University, UniversityDTO.UniversityResponse> getEntityToDtoMapper() {
        return this::mapToDTO;
    }
}