package com.backend.study_hub_api.helper.constant;

import lombok.experimental.FieldDefaults;

@FieldDefaults(makeFinal = true)
public class Message {

    public static String BAD_REQUEST_TITLE = "Bad Request";
    public static String UNAUTHORIZED_TITLE = "Unauthorized";
    public static String INTERNAL_SERVER_TITLE = "Internal Server Error";
    public static String NOT_FOUND_TITLE = "Not Found";

    public static String BODY_MISSING_MSG = "Required request body is missing";
    public static String INTERNAL_SERVER_ERROR = "error.internal-server";
    public static String UNAUTHORIZED = "error.unauthorized";
    public static String BAD_REQUEST = "error.bad-request";

    public static final String FORBIDDEN = "error.forbidden.access-denied";
    public static final String FORBIDDEN_TITLE = "Forbidden";

    public static final String EMAIL_REQUIRED_ERROR = "error.validate.email.required";
    public static final String EMAIL_FORMAT_ERROR = "error.validate.email.format";
    public static final String PASSWORD_REQUIRED_ERROR = "error.validate.password.required";
    public static final String PASSWORD_MIN_LENGTH_ERROR = "error.validate.password.min-length";
    public static final String LOGIN_TYPE_REQUIRED_ERROR = "error.validate.login-type.required";

    public static final String FULL_NAME_REQUIRED_ERROR = "error.validate.login-type.required";

    public static final String IMAGE_REQUIRED_ERROR = "error.validate.image.required";
    public static final String IMAGE_UPLOAD_FAIL_ERROR = "error.validate.image.upload-fail";

    public static final String FIRST_NAME_REQUIRED_ERROR = "error.validate.first-name.required";
    public static final String LAST_NAME_REQUIRED_ERROR = "error.validate.last-name.required";
    public static final String DATE_OF_BIRTH_REQUIRED_ERROR = "error.validate.date-of-birth.required";
    public static final String GENDER_REQUIRED_ERROR = "error.validate.gender.required";

    public static final String USER_NOT_FOUND_ERROR = "error.user.not-found";
    public static final String INVALID_CREDENTIAL_ERR = "error.validate.login.invalid-credential";
    public static final String USER_ALREADY_EXISTS = "error.user.already-exists";

    public static final String UNIVERSITY_NAME_REQUIRED_ERROR = "error.validate.university.name.required";
    public static final String UNIVERSITY_NOT_FOUND = "error.university.not-found";
    public static final String UNIVERSITY_ALREADY_EXISTS = "error.university.already-exists";

    public static final String CATEGORY_NOT_FOUND = "error.category.not-found";
    public static final String CATEGORY_NAME_ALREADY_EXISTS = "error.category.already-exists";
    public static final String CATEGORY_NAME_REQUIRED_ERROR = "error.validate.category.name.required";
    public static final String CATEGORY_NAME_LENGTH_ERROR = "error.validate.category.name.length";

}

