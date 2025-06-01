package com.backend.study_hub_api.dto;

import com.backend.study_hub_api.helper.enumeration.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String email;
    private String fullName;
    private String studentId;
    private UniversityDTO.UniversityResponse university;
    private String major;
    private Integer year;
    private String avatarUrl;
    private String phone;
    private String bio;
    private UserRole role;
    private Boolean isVerified;
}