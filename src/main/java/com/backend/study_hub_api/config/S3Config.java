package com.backend.study_hub_api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class S3Config {
    @Value("${aws.region}")
    private String awsRegion;

    // In a production environment, these would be stored more securely
    // For example, using AWS IAM roles or environment variables
    @Value("${aws.accessKeyId:}")
    private String accessKey;

    @Value("${aws.secretKey:}")
    private String secretKey;

    @Bean
    public S3Client s3Client() {
        // If access key and secret key are provided, use them
        if (accessKey != null && !accessKey.isEmpty() && secretKey != null && !secretKey.isEmpty()) {
            AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(accessKey, secretKey);
            return S3Client.builder()
                           .region(Region.of(awsRegion))
                           .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                           .build();
        }

        // Otherwise, rely on default AWS credential provider chain
        // (Environment variables, Java system properties, credentials file, etc.)
        return S3Client.builder()
                       .region(Region.of(awsRegion))
                       .build();
    }
}
