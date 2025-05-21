package com.backend.study_hub_api.controller;

import com.backend.study_hub_api.dto.CategoryDTO;
import com.backend.study_hub_api.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/common/categories")
@RequiredArgsConstructor
@Tag(name = "Public Categories", description = "Public endpoints for categories")
public class CategoryPublicController {

    private final CategoryService categoryService;

    @GetMapping
    @Operation(summary = "Get active categories", description = "Retrieve all active categories for public use")
    public ResponseEntity<List<CategoryDTO.CategoryResponse>> getActiveCategories() {
        return ResponseEntity.ok(categoryService.getActiveCategories());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get category by ID", description = "Retrieve a specific category by ID")
    public ResponseEntity<CategoryDTO.CategoryResponse> getCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }
}