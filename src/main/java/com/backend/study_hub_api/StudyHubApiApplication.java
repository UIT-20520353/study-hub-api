package com.backend.study_hub_api;

import com.backend.study_hub_api.config.ApplicationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableConfigurationProperties({ ApplicationProperties.class })
@EnableScheduling
public class StudyHubApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(StudyHubApiApplication.class, args);
	}

}
