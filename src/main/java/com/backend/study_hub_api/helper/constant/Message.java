package com.backend.study_hub_api.helper.constant;

import lombok.experimental.FieldDefaults;

@FieldDefaults(makeFinal = true)
public class Message {

    // Common Response Titles
    public static String BAD_REQUEST_TITLE = "Bad Request";
    public static String UNAUTHORIZED_TITLE = "Unauthorized";
    public static String INTERNAL_SERVER_TITLE = "Internal Server Error";
    public static String NOT_FOUND_TITLE = "Not Found";
    public static String FORBIDDEN_TITLE = "Forbidden";

    // Common Error Messages
    public static String BODY_MISSING_MSG = "Required request body is missing";
    public static String INTERNAL_SERVER_ERROR = "error.internal-server";
    public static String UNAUTHORIZED = "error.unauthorized";
    public static String BAD_REQUEST = "error.bad-request";
    public static String FORBIDDEN = "error.forbidden.access-denied";

    // Email Related Errors
    public static final String EMAIL_ALREADY_VERIFIED_ERROR = "error.email.already-verified";
    public static final String EMAIL_INVALID_ERROR = "error.email.invalid";
    public static final String EMAIL_VERIFICATION_FAILED_ERROR = "error.email.verification-failed";

    // Authentication & Authorization Errors
    public static final String EMAIL_REQUIRED_ERROR = "error.validate.email.required";
    public static final String EMAIL_FORMAT_ERROR = "error.validate.email.format";
    public static final String PASSWORD_REQUIRED_ERROR = "error.validate.password.required";
    public static final String PASSWORD_MIN_LENGTH_ERROR = "error.validate.password.min-length";
    public static final String LOGIN_TYPE_REQUIRED_ERROR = "error.validate.login-type.required";
    public static final String USER_NOT_AUTHENTICATED_ERROR = "error.user.not-authenticated";
    public static final String INVALID_CREDENTIAL_ERR = "error.validate.login.invalid-credential";
    public static final String UNIVERSITY_REQUIRED_ERROR = "error.validate.university.required";
    public static final String ACCOUNT_ALREADY_VERIFIED_ERROR = "error.account.already-verified";
    public static final String ACCOUNT_NOT_VERIFIED_ERROR = "error.account.not-verified";
    public static final String ACCOUNT_BLOCKED_ERROR = "error.account.blocked";

    // User Related Errors
    public static final String USER_NOT_FOUND_ERROR = "error.user.not-found";
    public static final String USER_ALREADY_EXISTS = "error.user.already-exists";
    public static final String USER_STUDENT_ID_EXISTS_ERROR = "error.user.student-id.exists";
    public static final String FULL_NAME_REQUIRED_ERROR = "error.validate.full-name.required";
    public static final String EMAIL_DOMAIN_MISMATCH_ERROR = "error.validate.email.domain-mismatch";
    public static final String FIRST_NAME_REQUIRED_ERROR = "error.validate.first-name.required";
    public static final String LAST_NAME_REQUIRED_ERROR = "error.validate.last-name.required";
    public static final String DATE_OF_BIRTH_REQUIRED_ERROR = "error.validate.date-of-birth.required";
    public static final String GENDER_REQUIRED_ERROR = "error.validate.gender.required";
    public static final String CURRENT_PASSWORD_REQUIRED_ERROR = "error.validate.current-password.required";
    public static final String CURRENT_PASSWORD_INCORRECT_ERROR = "error.validate.current-password.incorrect";
    public static final String NEW_PASSWORD_REQUIRED_ERROR = "error.validate.new-password.required";
    public static final String NEW_PASSWORD_SIZE_ERROR = "error.validate.new-password.size";
    public static final String NEW_PASSWORD_MUST_DIFFER_ERROR = "error.validate.new-password.must-differ";

    // File Upload Errors
    public static final String IMAGE_REQUIRED_ERROR = "error.validate.image.required";
    public static final String IMAGE_UPLOAD_FAIL_ERROR = "error.validate.image.upload-fail";
    public static final String FILE_REQUIRED_ERROR = "error.validate.file.required";
    public static final String FILE_MAX_SIZE_ERROR = "error.validate.file.max-size";
    public static final String FILE_NAME_INVALID_ERROR = "error.validate.file.name.invalid";
    public static final String FILE_TYPE_NOT_SUPPORTED_ERROR = "error.validate.file.type.not-supported";
    public static final String MAX_FILE_UPLOAD_ERROR = "error.validate.file.max-upload";

    // University Related Errors
    public static final String UNIVERSITY_NOT_FOUND = "error.university.not-found";
    public static final String UNIVERSITY_ALREADY_EXISTS = "error.university.already-exists";
    public static final String UNIVERSITY_NAME_REQUIRED_ERROR = "error.validate.university.name.required";
    public static final String UNIVERSITY_NAME_EXISTS_ERROR = "error.validate.university.name.exists";
    public static final String UNIVERSITY_NAME_MAX_LENGTH_ERROR = "error.validate.university.name.max-length";
    public static final String UNIVERSITY_SHORT_NAME_REQUIRED_ERROR = "error.validate.university.short-name.required";
    public static final String UNIVERSITY_SHORT_NAME_MAX_LENGTH_ERROR = "error.validate.university.short-name.max-length";
    public static final String UNIVERSITY_ADDRESS_MAX_LENGTH_ERROR = "error.validate.university.address.max-length";
    public static final String UNIVERSITY_EMAIL_DOMAIN_REQUIRED_ERROR = "error.validate.university.email-domain.required";
    public static final String UNIVERSITY_EMAIL_DOMAIN_MAX_LENGTH_ERROR = "error.validate.university.email-domain.max-length";
    public static final String UNIVERSITY_EMAIL_DOMAIN_EXISTS_ERROR = "error.validate.university.email-domain.exists";
    public static final String UNIVERSITY_CITY_MAX_LENGTH_ERROR = "error.validate.university.city.max-length";
    public static final String UNIVERSITY_WEBSITE_MAX_LENGTH_ERROR = "error.validate.university.website.max-length";
    public static final String UNIVERSITY_DESCRIPTION_MAX_LENGTH_ERROR = "error.validate.university.description.max-length";
    public static final String UNIVERSITY_STATUS_REQUIRED_ERROR = "error.validate.university.status.required";

    // Category Related Errors
    public static final String CATEGORY_NOT_FOUND = "error.category.not-found";
    public static final String CATEGORY_NAME_ALREADY_EXISTS = "error.category.already-exists";
    public static final String CATEGORY_NAME_REQUIRED_ERROR = "error.validate.category.name.required";
    public static final String CATEGORY_NAME_LENGTH_ERROR = "error.validate.category.name.length";
    public static final String CATEGORY_TYPE_REQUIRED_ERROR = "error.validate.category.type.required";

    // Topic Related Errors
    public static final String TOPIC_NOT_FOUND_ERROR = "error.topic.not-found";
    public static final String TOPIC_NOT_AUTHORIZED_ERROR = "error.topic.not-authorized";
    public static final String TOPIC_TITLE_REQUIRED_ERROR = "error.validate.topic.title.required";
    public static final String TOPIC_TITLE_MAX_LENGTH_ERROR = "error.validate.topic.title.max-length";
    public static final String TOPIC_CONTENT_REQUIRED_ERROR = "error.validate.topic.content.required";
    public static final String TOPIC_ATTACHMENT_URL_REQUIRED_ERROR = "error.validate.topic-attachment.url.required";
    public static final String TOPIC_ATTACHMENT_NAME_REQUIRED_ERROR = "error.validate.topic-attachment.name.required";
    public static final String TOPIC_ATTACHMENT_INVALID_TYPE_ERROR = "error.validate.topic-attachment.invalid-type";
    public static final String TOPIC_UNIVERSITY_ID_REQUIRED_ERROR = "error.validate.topic.university-id.required";

    // Validation Errors
    public static final String URL_INVALID_ERROR = "error.validate.url.invalid";

    // Product Related Errors
    public static final String PRODUCT_NOT_FOUND_ERROR = "error.product.not-found";
    public static final String PRODUCT_NOT_AUTHORIZED_ERROR = "error.product.not-authorized";
    public static final String PRODUCT_TITLE_REQUIRED_ERROR = "error.validate.product.title.required";
    public static final String PRODUCT_TITLE_MAX_LENGTH_ERROR = "error.validate.product.title.max-length";
    public static final String PRODUCT_DESCRIPTION_MAX_LENGTH_ERROR = "error.validate.product.description.max-length";
    public static final String PRODUCT_PRICE_REQUIRED_ERROR = "error.validate.product.price.required";
    public static final String PRODUCT_PRICE_MIN_ERROR = "error.validate.product.price.min";
    public static final String PRODUCT_PRICE_MAX_ERROR = "error.validate.product.price.max";
    public static final String PRODUCT_CONDITION_REQUIRED_ERROR = "error.validate.product.condition.required";
    public static final String PRODUCT_CATEGORY_REQUIRED_ERROR = "error.validate.product.category.required";
    public static final String PRODUCT_DELIVERY_METHOD_REQUIRED_ERROR = "error.validate.product.delivery-method.required";
    public static final String PRODUCT_IMAGE_REQUIRED_ERROR = "error.validate.product.image.required";
    public static final String PRODUCT_IMAGE_MAX_COUNT_ERROR = "error.validate.product.image.max-count";
    public static final String PRODUCT_IMAGE_TYPE_NOT_SUPPORTED_ERROR = "error.validate.product.image.type.not-supported";
    public static final String PRODUCT_IMAGE_MAX_SIZE_ERROR = "error.validate.product.image.max-size";
    public static final String AVATAR_FILE_EMPTY_ERROR = "error.validate.avatar.file.empty";
    public static final String AVATAR_UPLOAD_ERROR = "error.validate.avatar.upload.error";

    // Faculty Related Errors
    public static final String FACULTY_NOT_FOUND_ERROR = "error.faculty.not-found";
    public static final String FACULTY_NAME_REQUIRED_ERROR = "error.validate.faculty.name.required";
    public static final String FACULTY_NAME_MAX_LENGTH_ERROR = "error.validate.faculty.name.max-length";
    public static final String FACULTY_SHORT_NAME_MAX_LENGTH_ERROR = "error.validate.faculty.short-name.max-length";

    // Comment Related Errors
    public static final String COMMENT_NOT_FOUND_ERROR = "error.comment.not-found";
    public static final String COMMENT_NOT_AUTHORIZED_ERROR = "error.comment.not-authorized";
    public static final String COMMENT_CONTENT_REQUIRED_ERROR = "error.validate.comment.content.required";
    public static final String COMMENT_CONTENT_MAX_LENGTH_ERROR = "error.validate.comment.content.max-length";
    public static final String ONLY_COMMENT_AUTHOR_CAN_DELETE = "error.comment.only-author-can-delete";

    // Message/Chat Related Errors
    public static final String MESSAGE_NOT_FOUND_ERROR = "error.message.not-found";
    public static final String MESSAGE_CONTENT_REQUIRED_ERROR = "error.validate.message.content.required";
    public static final String MESSAGE_CONTENT_MAX_LENGTH_ERROR = "error.validate.message.content.max-length";
    public static final String CONVERSATION_NOT_FOUND_ERROR = "error.conversation.not-found";
    public static final String CONVERSATION_NOT_AUTHORIZED_ERROR = "error.conversation.not-authorized";

    // Rating/Review Related Errors
    public static final String RATING_NOT_FOUND_ERROR = "error.rating.not-found";
    public static final String RATING_VALUE_REQUIRED_ERROR = "error.validate.rating.value.required";
    public static final String RATING_VALUE_RANGE_ERROR = "error.validate.rating.value.range";
    public static final String RATING_ALREADY_EXISTS_ERROR = "error.rating.already-exists";

    // Transaction Related Errors
    public static final String TRANSACTION_NOT_FOUND_ERROR = "error.transaction.not-found";
    public static final String TRANSACTION_NOT_AUTHORIZED_ERROR = "error.transaction.not-authorized";
    public static final String TRANSACTION_INVALID_STATUS_ERROR = "error.transaction.invalid-status";

    // Report Related Errors
    public static final String REPORT_NOT_FOUND_ERROR = "error.report.not-found";
    public static final String REPORT_REASON_REQUIRED_ERROR = "error.validate.report.reason.required";
    public static final String REPORT_ALREADY_EXISTS_ERROR = "error.report.already-exists";

    // Notification Related Errors
    public static final String NOTIFICATION_NOT_FOUND_ERROR = "error.notification.not-found";
    public static final String NOTIFICATION_TITLE_REQUIRED_ERROR = "error.validate.notification.title.required";
    public static final String NOTIFICATION_CONTENT_REQUIRED_ERROR = "error.validate.notification.content.required";

    // Common Validation Errors
    public static final String ID_REQUIRED_ERROR = "error.validate.id.required";
    public static final String ID_POSITIVE_ERROR = "error.validate.id.positive";
    public static final String PAGE_MIN_ERROR = "error.validate.page.min";
    public static final String SIZE_MIN_ERROR = "error.validate.size.min";
    public static final String SIZE_MAX_ERROR = "error.validate.size.max";
    public static final String SORT_DIRECTION_INVALID_ERROR = "error.validate.sort-direction.invalid";

    // Permission Errors
    public static final String PERMISSION_DENIED_ERROR = "error.permission.denied";
    public static final String INSUFFICIENT_PRIVILEGES_ERROR = "error.permission.insufficient-privileges";

    // Data Integrity Errors
    public static final String DUPLICATE_ENTRY_ERROR = "error.data.duplicate-entry";
    public static final String FOREIGN_KEY_CONSTRAINT_ERROR = "error.data.foreign-key-constraint";
    public static final String DATA_INTEGRITY_VIOLATION_ERROR = "error.data.integrity-violation";

    // Search Related Errors
    public static final String SEARCH_KEYWORD_TOO_SHORT_ERROR = "error.validate.search.keyword.too-short";
    public static final String SEARCH_NO_RESULTS_ERROR = "error.search.no-results";

    // Verification Related Errors
    public static final String VERIFICATION_CODE_EXISTS_ERROR = "error.verification.code.exists";
    public static final String VERIFICATION_CODE_SENT_FAILED_ERROR = "error.verification.code.send-failed";
    public static final String CODE_REQUIRED_ERROR = "error.validate.code.required";
    public static final String USER_ID_REQUIRED_ERROR = "error.validate.user-id.required";

    // Order Related Errors
    public static final String ORDER_NOT_FOUND_ERROR = "error.order.not-found";
    public static final String ORDER_NOT_AUTHORIZED_ERROR = "error.order.not-authorized";
    public static final String ORDER_INVALID_STATUS_ERROR = "error.order.invalid-status";
    public static final String ORDER_ALREADY_CANCELLED_ERROR = "error.order.already-cancelled";
    public static final String ORDER_ALREADY_COMPLETED_ERROR = "error.order.already-completed";
    public static final String ORDER_CANNOT_CANCEL_ERROR = "error.order.cannot-cancel";
    public static final String ORDER_CANNOT_CONFIRM_ERROR = "error.order.cannot-confirm";
    public static final String ORDER_DELIVERY_ADDRESS_REQUIRED_ERROR = "error.validate.order.delivery-address.required";
    public static final String ORDER_DELIVERY_ADDRESS_MAX_LENGTH_ERROR = "error.validate.order.delivery-address.max-length";
    public static final String ORDER_DELIVERY_PHONE_REQUIRED_ERROR = "error.validate.order.delivery-phone.required";
    public static final String ORDER_DELIVERY_PHONE_FORMAT_ERROR = "error.validate.order.delivery-phone.format";
    public static final String ORDER_DELIVERY_METHOD_REQUIRED_ERROR = "error.validate.order.delivery-method.required";
    public static final String ORDER_SHIPPING_FEE_MIN_ERROR = "error.validate.order.shipping-fee.min";
    public static final String ORDER_SHIPPING_FEE_MAX_ERROR = "error.validate.order.shipping-fee.max";
    public static final String ORDER_CANCELLATION_REASON_REQUIRED_ERROR = "error.validate.order.cancellation-reason.required";
    public static final String ORDER_CANCELLATION_REASON_MAX_LENGTH_ERROR = "error.validate.order.cancellation-reason.max-length";
    public static final String ORDER_DELIVERY_NOTES_MAX_LENGTH_ERROR = "error.validate.order.delivery-notes.max-length";
    public static final String ORDER_CODE_ALREADY_EXISTS_ERROR = "error.order.code.already-exists";
    public static final String ORDER_PRODUCT_NOT_AVAILABLE_ERROR = "error.order.product.not-available";
    public static final String ORDER_CANNOT_ORDER_OWN_PRODUCT_ERROR = "error.order.cannot-order-own-product";
    public static final String ORDER_ITEMS_EMPTY_ERROR = "error.validate.order.items.empty";
    public static final String ORDER_PRODUCTS_SAME_SELLER_ERROR = "error.validate.order.products.same-seller";
    public static final String ORDER_PRODUCT_ID_REQUIRED_ERROR = "error.validate.order.product.id.required";
    public static final String ORDER_BUYER_SELLER_SAME_ERROR = "error.validate.order.buyer-seller.same";
    public static final String ORDER_ONLY_PENDING_CONFIRMED_CAN_CANCEL_ERROR = "error.order.only-pending-confirmed-can-cancel";

    // Order Status and Permission Errors
    public static final String ORDER_ONLY_SELLER_CAN_CONFIRM_ERROR = "error.order.only-seller-can-confirm";
    public static final String ORDER_ONLY_PENDING_CAN_CONFIRM_ERROR = "error.order.only-pending-can-confirm";
    public static final String ORDER_ONLY_SELLER_CAN_UPDATE_SHIPPING_FEE_ERROR = "error.order.only-seller-can-update-shipping-fee";
    public static final String ORDER_ONLY_CONFIRMED_CAN_UPDATE_SHIPPING_FEE_ERROR = "error.order.only-confirmed-can-update-shipping-fee";
    public static final String ORDER_ONLY_SELLER_CAN_MARK_SHIPPING_ERROR = "error.order.only-seller-can-mark-shipping";
    public static final String ORDER_MUST_BE_CONFIRMED_BEFORE_SHIPPING_ERROR = "error.order.must-be-confirmed-before-shipping";
    public static final String ORDER_ONLY_SELLER_CAN_MARK_DELIVERED_ERROR = "error.order.only-seller-can-mark-delivered";
    public static final String ORDER_MUST_BE_SHIPPING_TO_MARK_DELIVERED_ERROR = "error.order.must-be-shipping-to-mark-delivered";
    public static final String ORDER_ONLY_BUYER_CAN_COMPLETE_ERROR = "error.order.only-buyer-can-complete";
    public static final String ORDER_MUST_BE_DELIVERED_TO_COMPLETE_ERROR = "error.order.must-be-delivered-to-complete";
    public static final String ORDER_ONLY_BUYER_SELLER_CAN_CANCEL_ERROR = "error.order.only-buyer-seller-can-cancel";
    public static final String ORDER_CANNOT_CANCEL_COMPLETED_ERROR = "error.order.cannot-cancel-completed";
    public static final String ORDER_SHIPPING_FEE_REQUIRED_ERROR = "error.validate.order.shipping-fee.required";
    public static final String TOPIC_ID_REQUIRED_ERROR = "error.validate.topic.id.required";

}