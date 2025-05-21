package com.backend.study_hub_api.service;

import com.backend.study_hub_api.dto.CategoryDTO;
import com.backend.study_hub_api.model.Category;

import java.util.List;

public interface CategoryService {

    CategoryDTO.CategoryResponse createCategory(CategoryDTO.CreateCategoryRequest request);

    CategoryDTO.CategoryResponse getCategoryById(Long id);

    List<CategoryDTO.CategoryResponse> getAllCategories();

    List<CategoryDTO.CategoryResponse> getActiveCategories();

    CategoryDTO.CategoryResponse updateCategory(Long id, CategoryDTO.UpdateCategoryRequest request);

    void deleteCategory(Long id);

    CategoryDTO.CategoryResponse mapToDTO(Category category);
}