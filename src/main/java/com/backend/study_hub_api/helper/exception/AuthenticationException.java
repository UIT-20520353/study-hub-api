package com.backend.study_hub_api.helper.exception;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import static com.backend.study_hub_api.helper.constant.Message.UNAUTHORIZED_TITLE;

public class AuthenticationException extends AbstractThrowableProblem {

    public AuthenticationException(String message) {
        super(null, UNAUTHORIZED_TITLE, Status.UNAUTHORIZED, message);
    }

}

