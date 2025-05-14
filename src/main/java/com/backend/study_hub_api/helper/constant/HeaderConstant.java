package com.backend.study_hub_api.helper.constant;

public class HeaderConstant {

    private HeaderConstant() {}

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer";
    public static final String USER_REFRESH_TOKEN_HEADER = "x-user-refresh-token";
    public static final String IS_TOKEN_EXPIRED_HEADER = "X-Is-Token-Expired";
    public static final String X_TOTAL_COUNT_HEADER = "X-Total-Count";
    public static final String LINK_FORMAT_HEADER = "<{0}>; rel=\"{1}\"";
    public static final String LINK_HEADER = "Link";
    public static final String ACCESS_CONTROL_EXPOSE_HEADERS_HEADER = "Access-Control-Expose-Headers";

}

