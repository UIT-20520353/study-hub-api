package com.backend.study_hub_api.repository;

import com.backend.study_hub_api.helper.enumeration.ProductCondition;
import com.backend.study_hub_api.helper.enumeration.ProductStatus;
import com.backend.study_hub_api.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    List<Product> findByStatusOrderByViewCountDesc(ProductStatus status);

    List<Product> findBySellerIdOrderByCreatedAtDesc(Long sellerId);

    Page<Product> findBySellerIdOrderByCreatedAtDesc(Long sellerId, Pageable pageable);

    Page<Product> findByCategoryIdAndStatusOrderByCreatedAtDesc(Long categoryId, ProductStatus status, Pageable pageable);

    Page<Product> findByStatusOrderByCreatedAtDesc(ProductStatus status, Pageable pageable);

    default Page<Product> findAvailableProducts(Pageable pageable) {
        return findByStatusOrderByCreatedAtDesc(ProductStatus.AVAILABLE, pageable);
    }

    @Query("SELECT p FROM Product p " +
           "LEFT JOIN FETCH p.seller " +
           "LEFT JOIN FETCH p.category " +
           "LEFT JOIN FETCH p.images " +
           "WHERE p.id = :id")
    Optional<Product> findByIdWithDetails(@Param("id") Long id);

    @Query("SELECT p FROM Product p " +
           "WHERE (:keyword IS NULL OR " +
           "LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "AND p.status = :status " +
           "ORDER BY p.createdAt DESC")
    Page<Product> searchByKeyword(@Param("keyword") String keyword,
                                  @Param("status") ProductStatus status,
                                  Pageable pageable);

    @Query("SELECT p FROM Product p " +
           "WHERE (:keyword IS NULL OR " +
           "LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "AND (:categoryId IS NULL OR p.category.id = :categoryId) " +
           "AND (:minPrice IS NULL OR p.price >= :minPrice) " +
           "AND (:maxPrice IS NULL OR p.price <= :maxPrice) " +
           "AND (:condition IS NULL OR p.condition = :condition) " +
           "AND (:status IS NULL OR p.status = :status) " +
           "AND (:sellerId IS NULL OR p.seller.id = :sellerId)")
    Page<Product> searchProducts(@Param("keyword") String keyword,
                                 @Param("categoryId") Long categoryId,
                                 @Param("minPrice") Integer minPrice,
                                 @Param("maxPrice") Integer maxPrice,
                                 @Param("condition") ProductCondition condition,
                                 @Param("status") ProductStatus status,
                                 @Param("sellerId") Long sellerId,
                                 Pageable pageable);

    @Query("SELECT p FROM Product p " +
           "WHERE p.seller.university = :university " +
           "AND p.status = :status " +
           "ORDER BY p.createdAt DESC")
    Page<Product> findBySellerUniversityAndStatus(@Param("university") String university,
                                                  @Param("status") ProductStatus status,
                                                  Pageable pageable);

    long countBySellerIdAndStatus(Long sellerId, ProductStatus status);

    @Query("SELECT p FROM Product p " +
           "WHERE p.category.id = :categoryId " +
           "AND p.id != :productId " +
           "AND p.status = :status " +
           "ORDER BY p.createdAt DESC")
    Page<Product> findSimilarProducts(@Param("categoryId") Long categoryId,
                                      @Param("productId") Long productId,
                                      @Param("status") ProductStatus status,
                                      Pageable pageable);

    @Query("SELECT p.status as status, COUNT(p) as count FROM Product p GROUP BY p.status")
    List<Object[]> countProductsByStatus();

    List<Product> findTop10ByStatusOrderByCreatedAtDesc(ProductStatus status);

    boolean existsByIdAndSellerId(Long productId, Long sellerId);
}