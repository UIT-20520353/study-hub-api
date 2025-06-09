package com.backend.study_hub_api.controller;

import com.backend.study_hub_api.dto.CategoryDTO;
import com.backend.study_hub_api.dto.common.PaginationDTO;
import com.backend.study_hub_api.dto.criteria.CategoryFilterCriteria;
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
    @Operation(summary = "Get categories with filter and pagination",
               description = "Retrieve categories with optional filters and pagination")
    public ResponseEntity<PaginationDTO<CategoryDTO.CategoryResponse>> getCategories(
            @ModelAttribute CategoryFilterCriteria criteria) {
        return ResponseEntity.ok(categoryService.getCategoriesWithFilter(criteria));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get category by ID", description = "Retrieve a specific category by ID")
    public ResponseEntity<CategoryDTO.CategoryResponse> getCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @GetMapping("/topic")
    @Operation(summary = "Get categories for topic creation",
               description = "Retrieve categories suitable for creating a new topic")
    public ResponseEntity<List<CategoryDTO.CategoryResponse>> getCategoriesForTopicCreation() {
        return ResponseEntity.ok(categoryService.getCategoriesForTopicCreation());
    }

    @GetMapping("/product")
    @Operation(summary = "Get categories for product creation",
               description = "Retrieve categories suitable for creating a new product")
    public ResponseEntity<List<CategoryDTO.CategoryResponse>> getCategoriesForProductCreation() {
        return ResponseEntity.ok(categoryService.getCategoriesForProductCreation());
    }
}