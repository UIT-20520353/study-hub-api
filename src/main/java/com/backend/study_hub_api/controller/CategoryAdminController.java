package com.backend.study_hub_api.controller;

import com.backend.study_hub_api.dto.CategoryDTO;
import com.backend.study_hub_api.dto.common.PaginationDTO;
import com.backend.study_hub_api.dto.criteria.CategoryFilterCriteria;
import com.backend.study_hub_api.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;
import org.springdoc.core.annotations.ParameterObject;

@RestController
@RequestMapping("/api/admin/categories")
@RequiredArgsConstructor
@Tag(name = "Category Management", description = "Endpoints for category management (Admin only)")
public class CategoryAdminController {

    private final CategoryService categoryService;

    @PostMapping
    @Operation(summary = "Create new category", description = "Create a new product category")
    public ResponseEntity<CategoryDTO.CategoryResponse> createCategory(
            @Valid @RequestBody CategoryDTO.CreateCategoryRequest request) {
        return ResponseEntity.ok(categoryService.createCategory(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get category by ID", description = "Retrieve a category by its ID")
    public ResponseEntity<CategoryDTO.CategoryResponse> getCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @GetMapping
    @Operation(summary = "Get all categories", description = "Retrieve all categories (including inactive)")
    public ResponseEntity<PaginationDTO<CategoryDTO.CategoryResponse>> getAllCategories(@ModelAttribute CategoryFilterCriteria criteria) {
        return ResponseEntity.ok(categoryService.getCategoriesWithFilter(criteria));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update category", description = "Update an existing category")
    public ResponseEntity<CategoryDTO.CategoryResponse> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryDTO.UpdateCategoryRequest request) {
        return ResponseEntity.ok(categoryService.updateCategory(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete category", description = "Soft delete a category (set inactive)")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}