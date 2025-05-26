package com.backend.study_hub_api.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user/topics")
@RequiredArgsConstructor
@Tag(name = "Public Topics", description = "Public endpoints for topics")
public class TopicController {

}
