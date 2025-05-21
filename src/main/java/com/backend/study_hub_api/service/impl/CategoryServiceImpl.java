package com.backend.study_hub_api.service.impl;

import com.backend.study_hub_api.dto.CategoryDTO;
import com.backend.study_hub_api.helper.exception.BadRequestException;
import com.backend.study_hub_api.model.Category;
import com.backend.study_hub_api.repository.CategoryRepository;
import com.backend.study_hub_api.service.CategoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.backend.study_hub_api.helper.constant.Message.CATEGORY_NAME_ALREADY_EXISTS;
import static com.backend.study_hub_api.helper.constant.Message.CATEGORY_NOT_FOUND;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    CategoryRepository categoryRepository;

    @Override
    @Transactional
    public CategoryDTO.CategoryResponse createCategory(CategoryDTO.CreateCategoryRequest request) {
        if (categoryRepository.existsByName(request.getName())) {
            throw new BadRequestException(CATEGORY_NAME_ALREADY_EXISTS);
        }

        Category category = Category.builder()
                                    .name(request.getName())
                                    .isActive(true)
                                    .build();

        Category savedCategory = categoryRepository.save(category);
        return mapToDTO(savedCategory);
    }

    @Override
    public CategoryDTO.CategoryResponse getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                                              .orElseThrow(() -> new BadRequestException(CATEGORY_NOT_FOUND));
        return mapToDTO(category);
    }

    @Override
    public List<CategoryDTO.CategoryResponse> getAllCategories() {
        return categoryRepository.findAllSortedByName()
                                 .stream()
                                 .map(this::mapToDTO)
                                 .toList();
    }

    @Override
    public List<CategoryDTO.CategoryResponse> getActiveCategories() {
        return categoryRepository.findByIsActiveTrue()
                                 .stream()
                                 .map(this::mapToDTO)
                                 .toList();
    }

    @Override
    @Transactional
    public CategoryDTO.CategoryResponse updateCategory(Long id, CategoryDTO.UpdateCategoryRequest request) {
        Category category = categoryRepository.findById(id)
                                              .orElseThrow(() -> new BadRequestException(CATEGORY_NOT_FOUND));

        if (categoryRepository.existsByNameAndIdNot(request.getName(), id)) {
            throw new BadRequestException(CATEGORY_NAME_ALREADY_EXISTS);
        }

        category.setName(request.getName());
        if (request.getIsActive() != null) {
            category.setIsActive(request.getIsActive());
        }

        Category savedCategory = categoryRepository.save(category);
        return mapToDTO(savedCategory);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                                              .orElseThrow(() -> new BadRequestException(CATEGORY_NOT_FOUND));

        category.setIsActive(false);
        categoryRepository.save(category);
    }

    @Override
    public CategoryDTO.CategoryResponse mapToDTO(Category category) {
        return CategoryDTO.CategoryResponse.builder()
                                           .id(category.getId())
                                           .name(category.getName())
                                           .isActive(category.getIsActive())
                                           .createdAt(category.getCreatedAt())
                                           .updatedAt(category.getUpdatedAt())
                                           .build();
    }
}