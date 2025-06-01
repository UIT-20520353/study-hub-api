package com.backend.study_hub_api.repository;

import com.backend.study_hub_api.helper.enumeration.VerificationType;
import com.backend.study_hub_api.model.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {

    @Query("SELECT vc FROM VerificationCode vc WHERE " +
           "vc.user.id = :userId AND " +
           "vc.code = :code AND " +
           "vc.type = :type AND " +
           "vc.isUsed = false AND " +
           "vc.expiredAt > :currentTime")
    Optional<VerificationCode> findValidCode(@Param("userId") Long userId,
                                             @Param("code") String code,
                                             @Param("type") VerificationType type,
                                             @Param("currentTime") Instant currentTime);

    @Modifying
    @Query("UPDATE VerificationCode vc SET vc.isUsed = true WHERE " +
           "vc.user.id = :userId AND vc.type = :type AND vc.isUsed = false")
    void markAllAsUsedForUser(@Param("userId") Long userId,
                              @Param("type") VerificationType type);

    @Modifying
    @Query("DELETE FROM VerificationCode vc WHERE vc.expiredAt < :currentTime")
    void deleteExpiredCodes(@Param("currentTime") Instant currentTime);

    @Query("SELECT COUNT(vc) > 0 FROM VerificationCode vc WHERE " +
           "vc.user.id = :userId AND " +
           "vc.type = :type AND " +
           "vc.isUsed = false AND " +
           "vc.expiredAt > :currentTime")
    boolean hasValidCode(@Param("userId") Long userId,
                         @Param("type") VerificationType type,
                         @Param("currentTime") Instant currentTime);
}