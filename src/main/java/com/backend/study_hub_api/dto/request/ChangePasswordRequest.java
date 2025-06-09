package com.backend.study_hub_api.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import static com.backend.study_hub_api.helper.constant.Message.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordRequest {

    @NotBlank(message = CURRENT_PASSWORD_REQUIRED_ERROR)
    private String currentPassword;

    @NotBlank(message = NEW_PASSWORD_REQUIRED_ERROR)
    @Size(min = 6, max = 50, message = NEW_PASSWORD_SIZE_ERROR)
    private String newPassword;

}