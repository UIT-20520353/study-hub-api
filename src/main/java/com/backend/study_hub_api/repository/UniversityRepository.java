package com.backend.study_hub_api.repository;

import com.backend.study_hub_api.model.University;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UniversityRepository extends JpaRepository<University, Long> {
    boolean existsByName(String name);
}