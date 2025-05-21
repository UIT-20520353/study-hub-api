package com.backend.study_hub_api.model;

import com.backend.study_hub_api.helper.enumeration.DeliveryMethod;
import com.backend.study_hub_api.helper.enumeration.ProductCondition;
import com.backend.study_hub_api.helper.enumeration.ProductStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "t_products")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    private User seller;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "price", nullable = false)
    private Integer price;

    @Column(name = "condition", nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductCondition condition;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductStatus status = ProductStatus.AVAILABLE;

    @Column(name = "address", columnDefinition = "TEXT")
    private String address;

    @Column(name = "delivery_method")
    @Enumerated(EnumType.STRING)
    private DeliveryMethod deliveryMethod = DeliveryMethod.BOTH;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<ProductImage> images;

    // Helper methods
    public String getPrimaryImageUrl() {
        if (images != null && !images.isEmpty()) {
            return images.stream()
                         .filter(img -> Boolean.TRUE.equals(img.getIsPrimary()))
                         .map(ProductImage::getImageUrl)
                         .findFirst()
                         .orElse(images.get(0).getImageUrl());
        }
        return null;
    }

    public boolean isAvailable() {
        return ProductStatus.AVAILABLE.equals(this.status);
    }

    public boolean isSold() {
        return ProductStatus.SOLD.equals(this.status);
    }

    public void addImage(ProductImage image) {
        image.setProduct(this);
        this.images.add(image);

        // Nếu đây là ảnh đầu tiên, set làm primary
        if (this.images.size() == 1) {
            image.setIsPrimary(true);
        }
    }

    public void removeImage(ProductImage image) {
        this.images.remove(image);
        image.setProduct(null);

        // Nếu xóa ảnh primary và còn ảnh khác, set ảnh đầu tiên làm primary
        if (Boolean.TRUE.equals(image.getIsPrimary()) && !this.images.isEmpty()) {
            this.images.get(0).setIsPrimary(true);
        }
    }

    public void markAsSold() {
        this.status = ProductStatus.SOLD;
    }

    public void markAsAvailable() {
        this.status = ProductStatus.AVAILABLE;
    }

    public void markAsPending() {
        this.status = ProductStatus.PENDING;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
        if (status == null) {
            status = ProductStatus.AVAILABLE;
        }
        if (deliveryMethod == null) {
            deliveryMethod = DeliveryMethod.BOTH;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }
}