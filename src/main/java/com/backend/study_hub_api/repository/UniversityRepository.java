package com.backend.study_hub_api.repository;

import com.backend.study_hub_api.helper.enumeration.UniversityStatus;
import com.backend.study_hub_api.model.University;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UniversityRepository extends JpaRepository<University, Long>, JpaSpecificationExecutor<University> {

    boolean existsByEmailDomain(String emailDomain);

    boolean existsByEmailDomainAndIdNot(String emailDomain, Long id);

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Long id);

    List<University> findByStatusOrderByNameAsc(UniversityStatus status);

    List<University> findByIsActiveTrueOrderByNameAsc();

    @Query("SELECT u FROM University u WHERE u.status != 'DELETED' AND u.status != 'INACTIVE' ORDER BY u.name ASC")
    List<University> findAllActiveUniversities();

    Optional<University> findByEmailDomainIgnoreCase(String emailDomain);

    Optional<University> findByNameIgnoreCase(String name);
}