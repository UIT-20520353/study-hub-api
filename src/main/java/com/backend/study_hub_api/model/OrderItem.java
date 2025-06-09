package com.backend.study_hub_api.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "t_order_items")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "created_at")
    private Instant createdAt;

    public Integer getItemPrice() {
        return product != null ? product.getPrice() : 0;
    }

    public String getProductTitle() {
        return product != null ? product.getTitle() : "";
    }

    public String getProductImageUrl() {
        return product != null ? product.getPrimaryImageUrl() : null;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
    }
}