package com.backend.study_hub_api.model;

import com.backend.study_hub_api.helper.enumeration.DeliveryMethod;
import com.backend.study_hub_api.helper.enumeration.OrderStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "t_orders")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id", nullable = false)
    private User buyer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    private User seller;

    @Column(name = "order_code", nullable = false, unique = true, length = 20)
    private String orderCode;

    @Column(name = "shipping_fee")
    private Integer shippingFee;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.PENDING;

    @Column(name = "delivery_method", nullable = false)
    @Enumerated(EnumType.STRING)
    private DeliveryMethod deliveryMethod;

    @Column(name = "delivery_address", columnDefinition = "TEXT", nullable = false)
    private String deliveryAddress;

    @Column(name = "delivery_phone", length = 20)
    private String deliveryPhone;

    @Column(name = "delivery_notes", columnDefinition = "TEXT")
    private String deliveryNotes;

    @Column(name = "confirmed_at")
    private Instant confirmedAt;

    @Column(name = "shipping_fee_updated_at")
    private Instant shippingFeeUpdatedAt;

    @Column(name = "delivered_at")
    private Instant deliveredAt;

    @Column(name = "completed_at")
    private Instant completedAt;

    @Column(name = "cancelled_at")
    private Instant cancelledAt;

    @Column(name = "cancellation_reason", columnDefinition = "TEXT")
    private String cancellationReason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cancelled_by")
    private User cancelledBy;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<OrderItem> orderItems;

    public boolean isPending() {
        return OrderStatus.PENDING.equals(this.status);
    }

    public boolean isConfirmed() {
        return OrderStatus.CONFIRMED.equals(this.status);
    }

    public boolean isShipping() {
        return OrderStatus.SHIPPING.equals(this.status);
    }

    public boolean isDelivered() {
        return OrderStatus.DELIVERED.equals(this.status);
    }

    public boolean isCompleted() {
        return OrderStatus.COMPLETED.equals(this.status);
    }

    public boolean isCancelled() {
        return OrderStatus.CANCELLED.equals(this.status);
    }

    public boolean isShippingFeeUpdated() {
        return OrderStatus.SHIPPING_FEE_UPDATED.equals(this.status);
    }

    public void markAsConfirmed() {
        this.status = OrderStatus.CONFIRMED;
        this.confirmedAt = Instant.now();
    }

    public void markAsShipping() {
        this.status = OrderStatus.SHIPPING;
    }

    public void markAsDelivered() {
        this.status = OrderStatus.DELIVERED;
        this.deliveredAt = Instant.now();
    }

    public void markAsCompleted() {
        this.status = OrderStatus.COMPLETED;
        this.completedAt = Instant.now();
    }

    public void markAsCancelled(User cancelledByUser, String reason) {
        this.status = OrderStatus.CANCELLED;
        this.cancelledAt = Instant.now();
        this.cancelledBy = cancelledByUser;
        this.cancellationReason = reason;
    }

    public void updateShippingFee(Integer fee) {
        this.shippingFee = fee;
        this.status = OrderStatus.SHIPPING_FEE_UPDATED;
        this.shippingFeeUpdatedAt = Instant.now();
    }

    public Integer getTotalAmount() {
        if (orderItems == null || orderItems.isEmpty()) {
            return 0;
        }

        Integer productTotal = orderItems.stream()
                                         .mapToInt(item -> item.getProduct().getPrice())
                                         .sum();

        return productTotal + (shippingFee != null ? shippingFee : 0);
    }

    public Integer getProductTotal() {
        if (orderItems == null || orderItems.isEmpty()) {
            return 0;
        }

        return orderItems.stream()
                         .mapToInt(item -> item.getProduct().getPrice())
                         .sum();
    }

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
        if (status == null) {
            status = OrderStatus.PENDING;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }
}