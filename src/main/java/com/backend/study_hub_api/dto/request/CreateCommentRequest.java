package com.backend.study_hub_api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import static com.backend.study_hub_api.helper.constant.Message.*;

@Data
public class CreateCommentRequest {
    @NotNull(message = TOPIC_ID_REQUIRED_ERROR)
    private Long topicId;

    @NotBlank(message = COMMENT_CONTENT_REQUIRED_ERROR)
    @Size(min = 1, max = 500, message = COMMENT_CONTENT_MAX_LENGTH_ERROR)
    private String content;
}