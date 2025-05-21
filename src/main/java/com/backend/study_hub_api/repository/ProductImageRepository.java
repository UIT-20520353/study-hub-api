package com.backend.study_hub_api.repository;

import com.backend.study_hub_api.model.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {

    List<ProductImage> findByProductIdOrderByIsPrimaryDescCreatedAtAsc(Long productId);

    Optional<ProductImage> findByProductIdAndIsPrimaryTrue(Long productId);

    void deleteByProductId(Long productId);

    long countByProductId(Long productId);

    @Modifying
    @Query("UPDATE ProductImage pi SET pi.isPrimary = false WHERE pi.product.id = :productId")
    void unsetAllPrimaryImages(@Param("productId") Long productId);

    boolean existsByIdAndProductId(Long imageId, Long productId);

    @Query("SELECT CASE WHEN COUNT(pi) > 0 THEN true ELSE false END " +
           "FROM ProductImage pi " +
           "WHERE pi.id = :imageId AND pi.product.seller.id = :sellerId")
    boolean existsByIdAndProductSellerId(@Param("imageId") Long imageId, @Param("sellerId") Long sellerId);
}