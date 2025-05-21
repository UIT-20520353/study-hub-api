package com.backend.study_hub_api.repository;

import com.backend.study_hub_api.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Long id);

    List<Category> findByIsActiveTrue();

    @Query("SELECT c FROM Category c ORDER BY c.name ASC")
    List<Category> findAllSortedByName();

    Optional<Category> findByNameIgnoreCase(String name);
}