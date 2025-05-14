package com.backend.study_hub_api.helper.enumeration;

import java.net.URI;

public enum ResponseType {
    INTERNAL_SERVER_ERROR(500, "error.internal-server-error", "Internal Server Error", null),
    UNAUTHORIZED(401, "error.unauthorized", "Unauthorized", null),
    BAD_REQUEST(400, "error.bad-request", "Bad Request", null),
    NOT_FOUND(404, "error.not-found", "Not Found", null),
    CONFLICT(409, "error.conflict", "Conflict", null),

    REQUEST_BODY_MISSING(400, "error.request-body-missing", "Request Body Missing", null),
    FORBIDDEN(403, "error.forbidden", "Forbidden", null),
    METHOD_NOT_ALLOWED(405, "error.method-not-allowed", "Method Not Allowed", null),
    NOT_ACCEPTABLE(406, "error.not-acceptable", "Not Acceptable", null),
    UNSUPPORTED_MEDIA_TYPE(415, "error.unsupported-media-type", "Unsupported Media Type", null),
    ;

    private int status;
    private String messageCode;
    private String message;
    private URI type;

    private ResponseType(int status, String messageCode, String message, URI type) {
        this.status = status;
        this.messageCode = messageCode;
        this.message = message;
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public String getMessageCode() {
        return messageCode;
    }

    public String getMessage() {
        return message;
    }

    public URI getType() {
        return type;
    }

    public static ResponseType getUnHandleType(int statusCode) {
        switch (statusCode) {
            case 400:
                return REQUEST_BODY_MISSING;
            case 401:
                return UNAUTHORIZED;
            case 403:
                return FORBIDDEN;
            case 404:
                return NOT_FOUND;
            case 405:
                return METHOD_NOT_ALLOWED;
            case 406:
                return NOT_ACCEPTABLE;
            case 415:
                return UNSUPPORTED_MEDIA_TYPE;
            default:
                return INTERNAL_SERVER_ERROR;
        }
    }
}

