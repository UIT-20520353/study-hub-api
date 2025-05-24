package com.backend.study_hub_api.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.http.urlconnection.UrlConnectionHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
@RequiredArgsConstructor
public class AwsS3Config {

    private final ApplicationProperties applicationProperties;

    @Bean
    public S3Client s3Client() {
        ApplicationProperties.AwsProperties awsProps = applicationProperties.getAws();

        AwsBasicCredentials credentials = AwsBasicCredentials.create(
                awsProps.accessKey(),
                awsProps.secretAccessKey()
        );

        return S3Client.builder()
                       .region(Region.of(awsProps.region()))
                       .credentialsProvider(StaticCredentialsProvider.create(credentials))
                       .httpClient(UrlConnectionHttpClient.builder().build())
                       .build();
    }
}