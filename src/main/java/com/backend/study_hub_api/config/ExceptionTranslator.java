package com.backend.study_hub_api.config;

import com.backend.study_hub_api.helper.exception.AuthenticationException;
import com.backend.study_hub_api.helper.exception.BadRequestException;
import com.backend.study_hub_api.helper.exception.InternalException;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.connector.ClientAbortException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.NativeWebRequest;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;
import org.zalando.problem.spring.web.advice.ProblemHandling;
import org.zalando.problem.spring.web.advice.security.SecurityAdviceTrait;
import org.zalando.problem.violations.Violation;
import org.zalando.problem.violations.ConstraintViolationProblem;
import org.zalando.problem.DefaultProblem;
import com.backend.study_hub_api.helper.enumeration.ResponseType;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static com.backend.study_hub_api.helper.constant.Message.*;

@ControllerAdvice
@RequiredArgsConstructor
public class ExceptionTranslator implements ProblemHandling, SecurityAdviceTrait {

    @Override
    public ResponseEntity<Problem> process(ResponseEntity<Problem> entity, NativeWebRequest request) {
        Problem problem = entity.getBody();

        if (problem == null) {
            return null;
        }

        if (!(problem instanceof ConstraintViolationProblem
              || problem instanceof DefaultProblem
              || problem instanceof BadRequestException
              || problem instanceof InternalException
              || problem instanceof AuthenticationException)) {
            return entity;
        }

        if (problem.getStatus().equals(Status.BAD_REQUEST) || problem instanceof BadRequestException) {
            return new ResponseEntity<>(buildBadRequestProblem(problem), entity.getHeaders(), entity.getStatusCode());
        }

        if (problem.getStatus().equals(Status.INTERNAL_SERVER_ERROR) || problem instanceof InternalException) {
            return new ResponseEntity<>(buildInternalExceptionProblem(problem), entity.getHeaders(), entity.getStatusCode());
        }

        if (problem.getStatus().equals(Status.UNAUTHORIZED) || problem instanceof AuthenticationException) {
            return new ResponseEntity<>(buildUnauthorizedProblem(problem), entity.getHeaders(), entity.getStatusCode());
        }

        if (problem.getStatus().equals(Status.FORBIDDEN)) {
            return new ResponseEntity<>(buildForbiddenProblem(problem), entity.getHeaders(), entity.getStatusCode());
        }

        return new ResponseEntity<>(buildUnknownExceptionProblem(problem), entity.getHeaders(), entity.getStatusCode());
    }

    @Override
    public ResponseEntity<Problem> newConstraintViolationProblem(Throwable throwable, Collection<Violation> stream, NativeWebRequest request) {
        List<Violation> violations = stream.stream()
                                           .sorted(Comparator.comparing(Violation::getField).thenComparing(Violation::getMessage))
                                           .toList();

        Problem problem = Problem
                .builder()
                .withStatus(Status.BAD_REQUEST)
                .withTitle(BAD_REQUEST_TITLE)
                .withDetail("Invalid request body")
                .with("violations", violations)
                .build();

        return this.create(throwable, problem, request);
    }

    @Override
    public ResponseEntity<Problem> handleMessageNotReadableException(HttpMessageNotReadableException exception, NativeWebRequest request) {
        String detail = exception.getMessage().startsWith(BODY_MISSING_MSG) ? BODY_MISSING_MSG : exception.getMessage();
        Problem problem = Problem
                .builder()
                .withStatus(Status.BAD_REQUEST)
                .withTitle(BAD_REQUEST_TITLE)
                .withDetail(detail)
                .build();
        return new ResponseEntity<>(problem, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<Problem> handleAccessDenied(AccessDeniedException exception, NativeWebRequest request) {
        Problem problem = Problem
                .builder()
                .withStatus(Status.FORBIDDEN)
                .withTitle(FORBIDDEN_TITLE)
                .withDetail(FORBIDDEN)
                .build();
        return create(exception, problem, request);
    }

    @ExceptionHandler(ClientAbortException.class)
    public ResponseEntity<?> handleClientAbortException(ClientAbortException ex) {
        return ResponseEntity.noContent().build();
    }

    private Problem buildUnauthorizedProblem(Problem problem) {
        String msgCode = problem.getDetail();
        String title = problem.getTitle();
        return Problem.builder().withType(
                              Problem.DEFAULT_TYPE.equals(problem.getType())
                                      ? ResponseType.UNAUTHORIZED.getType()
                                      : problem.getType())
                      .withDetail(msgCode != null ? msgCode : UNAUTHORIZED)
                      .withTitle(title != null ? title : UNAUTHORIZED_TITLE)
                      .withStatus(Status.UNAUTHORIZED).build();
    }

    private Problem buildBadRequestProblem(Problem problem) {
        String msgCode = problem.getDetail();
        String title = problem.getTitle();
        return Problem.builder().withType(
                              Problem.DEFAULT_TYPE.equals(problem.getType())
                                      ? ResponseType.BAD_REQUEST.getType()
                                      : problem.getType())
                      .withDetail(msgCode != null ? msgCode : BAD_REQUEST)
                      .withTitle(title != null ? title : BAD_REQUEST_TITLE)
                      .withStatus(Status.BAD_REQUEST).build();
    }

    private Problem buildInternalExceptionProblem(Problem problem) {
        String msgCode = Objects.requireNonNull(problem.getDetail()).startsWith("error.")
                ? problem.getDetail()
                : INTERNAL_SERVER_ERROR;
        String title = problem.getTitle();
        return Problem.builder().withType(
                              Problem.DEFAULT_TYPE.equals(problem.getType())
                                      ? ResponseType.INTERNAL_SERVER_ERROR.getType()
                                      : problem.getType())
                      .withDetail(msgCode)
                      .withTitle(title != null ? title : INTERNAL_SERVER_TITLE)
                      .withStatus(Status.INTERNAL_SERVER_ERROR).build();
    }

    private Problem buildUnknownExceptionProblem(Problem problem) {
        return Problem.builder().withType(
                              Problem.DEFAULT_TYPE.equals(problem.getType())
                                      ? ResponseType.UNAUTHORIZED.getType()
                                      : problem.getType())
                      .withDetail("Internal Server Error")
                      .with("test", "error.internal.server")
                      .withStatus(Status.INTERNAL_SERVER_ERROR).build();
    }

    private Problem buildForbiddenProblem(Problem problem) {
        String msgCode = problem.getDetail();
        String title = problem.getTitle();
        return Problem.builder().withType(
                              Problem.DEFAULT_TYPE.equals(problem.getType())
                                      ? ResponseType.FORBIDDEN.getType()
                                      : problem.getType())
                      .withDetail(msgCode != null ? msgCode : "Access Denied")
                      .withTitle(title != null ? title : "Forbidden")
                      .withStatus(Status.FORBIDDEN).build();
    }

}

