package com.backend.study_hub_api.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.cors.CorsConfiguration;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ApplicationProperties {

    CorsConfiguration cors;
    SecurityProperties security;
    AwsProperties aws;

    public record SecurityProperties(
            JwtProperties jwt
    ) {}

    public record JwtProperties(
            String secret,
            long accessTokenInMinutes,
            long refreshTokenInHours
    ) {}

    public record AwsProperties(
            String region,
            String s3Bucket,
            String accessKey,
            String secretAccessKey
    ) {}

}

