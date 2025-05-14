package com.backend.study_hub_api.config.jwt;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.backend.study_hub_api.helper.constant.AppConstant.EMPTY_STR;
import static org.springframework.security.config.Elements.ANONYMOUS;

public final class SecurityUtils {

    private SecurityUtils() {}

    public static String generateRandomCode() {
        return UUID.randomUUID().toString().replace("-", EMPTY_STR);
    }

    public static Long getCurrentUserId() {
        return Optional
                .ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(Authentication::getPrincipal)
                .map(Object::toString)
                .map(Long::parseLong)
                .orElse(null);
    }

    public static String getCurrentTokenId() {
        return Optional
                .ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(Authentication::getCredentials)
                .map(Object::toString)
                .orElse(null);
    }

    public static Authentication createAnonymous() {
        return new UsernamePasswordAuthenticationToken(
                //                StringUtils.EMPTY,
                "3",
                StringUtils.EMPTY,
                List.of(new SimpleGrantedAuthority(ANONYMOUS))
        );
    }

}

