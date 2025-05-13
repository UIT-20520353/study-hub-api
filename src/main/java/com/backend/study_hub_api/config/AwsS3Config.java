package com.backend.study_hub_api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class AwsS3Config {

    @Value("${aws.s3.region}")
    private String region;

    @Value("${AWS_ACCESS_KEY_ID:}")
    private String accessKeyId;

    @Value("${AWS_SECRET_ACCESS_KEY:}")
    private String secretAccessKey;

    @Bean
    public S3Client s3Client() {
        // Sử dụng AWS credentials từ environment variables hoặc IAM Role
        // Nếu sử dụng biến môi trường, đảm bảo đã set AWS_ACCESS_KEY_ID và AWS_SECRET_ACCESS_KEY
        if (accessKeyId.isEmpty() || secretAccessKey.isEmpty()) {
            return S3Client.builder()
                    .region(Region.of(region))
                    .build();
        } else {
            return S3Client.builder()
                    .region(Region.of(region))
                    .credentialsProvider(StaticCredentialsProvider.create(
                            AwsBasicCredentials.create(accessKeyId, secretAccessKey)))
                    .build();
        }
    }
}
