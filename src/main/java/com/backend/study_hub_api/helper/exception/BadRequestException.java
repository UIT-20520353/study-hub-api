package com.backend.study_hub_api.helper.exception;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import static com.backend.study_hub_api.helper.constant.Message.BAD_REQUEST_TITLE;

public class BadRequestException extends AbstractThrowableProblem {
    public BadRequestException(String message) {
        super(null, BAD_REQUEST_TITLE, Status.BAD_REQUEST, message);
    }
}
