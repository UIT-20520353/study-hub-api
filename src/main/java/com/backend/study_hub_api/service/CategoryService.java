package com.backend.study_hub_api.service;

import com.backend.study_hub_api.dto.CategoryDTO;
import com.backend.study_hub_api.dto.common.PaginationDTO;
import com.backend.study_hub_api.dto.criteria.CategoryFilterCriteria;
import com.backend.study_hub_api.model.Category;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryService {

    CategoryDTO.CategoryResponse createCategory(CategoryDTO.CreateCategoryRequest request);
    CategoryDTO.CategoryResponse getCategoryById(Long id);
    PaginationDTO<CategoryDTO.CategoryResponse> getAllCategories(Pageable pageable);
    List<CategoryDTO.CategoryResponse> getActiveCategories();
    CategoryDTO.CategoryResponse updateCategory(Long id, CategoryDTO.UpdateCategoryRequest request);
    void deleteCategory(Long id);
    CategoryDTO.CategoryResponse mapToDTO(Category category);
    PaginationDTO<CategoryDTO.CategoryResponse> getCategoriesWithFilter(CategoryFilterCriteria criteria);
    Category getCategoryByIdOrThrow(Long id);
    List<CategoryDTO.CategoryResponse> getCategoriesForTopicCreation();
    List<CategoryDTO.CategoryResponse> getCategoriesForProductCreation();

}