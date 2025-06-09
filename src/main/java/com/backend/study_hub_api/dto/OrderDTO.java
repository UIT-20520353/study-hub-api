package com.backend.study_hub_api.dto;

import com.backend.study_hub_api.helper.enumeration.DeliveryMethod;
import com.backend.study_hub_api.helper.enumeration.OrderStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {

    private Long id;
    private String orderCode;
    private OrderStatus status;
    private DeliveryMethod deliveryMethod;

    private UserSummaryDTO buyer;
    private UserSummaryDTO seller;

    private String deliveryAddress;
    private String deliveryPhone;
    private String deliveryNotes;

    private Integer shippingFee;
    private Integer productTotal;
    private Integer totalAmount;

    private List<OrderItemDTO> orderItems;

    // Timestamps
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant updatedAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant confirmedAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant shippingFeeUpdatedAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant deliveredAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant completedAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant cancelledAt;

    // Cancellation info
    private String cancellationReason;
    private UserSummaryDTO cancelledBy;

    // Helper methods for frontend
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

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserSummaryDTO {
        private Long id;
        private String fullName;
        private String email;
        private String avatarUrl;
        private String phone;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItemDTO {
        private Long id;
        private Long productId;
        private String productTitle;
        private String productImageUrl;
        private Integer itemPrice;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
        private Instant createdAt;
    }
}