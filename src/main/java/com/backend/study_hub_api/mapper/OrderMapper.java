package com.backend.study_hub_api.mapper;

import com.backend.study_hub_api.dto.OrderDTO;
import com.backend.study_hub_api.model.Order;
import com.backend.study_hub_api.model.OrderItem;
import com.backend.study_hub_api.model.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderMapper {

    public OrderDTO toDTO(Order order) {
        if (order == null) {
            return null;
        }

        return OrderDTO.builder()
                       .id(order.getId())
                       .orderCode(order.getOrderCode())
                       .status(order.getStatus())
                       .deliveryMethod(order.getDeliveryMethod())
                       .buyer(toUserSummary(order.getBuyer()))
                       .seller(toUserSummary(order.getSeller()))
                       .deliveryAddress(order.getDeliveryAddress())
                       .deliveryPhone(order.getDeliveryPhone())
                       .deliveryNotes(order.getDeliveryNotes())
                       .shippingFee(order.getShippingFee())
                       .productTotal(order.getProductTotal())
                       .totalAmount(order.getTotalAmount())
                       .orderItems(toOrderItemDTOList(order.getOrderItems()))
                       .createdAt(order.getCreatedAt())
                       .updatedAt(order.getUpdatedAt())
                       .confirmedAt(order.getConfirmedAt())
                       .shippingFeeUpdatedAt(order.getShippingFeeUpdatedAt())
                       .deliveredAt(order.getDeliveredAt())
                       .completedAt(order.getCompletedAt())
                       .cancelledAt(order.getCancelledAt())
                       .cancellationReason(order.getCancellationReason())
                       .cancelledBy(toUserSummary(order.getCancelledBy()))
                       .build();
    }

    public List<OrderDTO> toDTOList(List<Order> orders) {
        if (orders == null) {
            return null;
        }

        return orders.stream()
                     .map(this::toDTO)
                     .collect(Collectors.toList());
    }

    private OrderDTO.UserSummaryDTO toUserSummary(User user) {
        if (user == null) {
            return null;
        }

        return OrderDTO.UserSummaryDTO.builder()
                                      .id(user.getId())
                                      .fullName(user.getFullName())
                                      .email(user.getEmail())
                                      .avatarUrl(user.getAvatarUrl())
                                      .phone(user.getPhone())
                                      .build();
    }

    private List<OrderDTO.OrderItemDTO> toOrderItemDTOList(List<OrderItem> orderItems) {
        if (orderItems == null) {
            return null;
        }

        return orderItems.stream()
                         .map(this::toOrderItemDTO)
                         .collect(Collectors.toList());
    }

    private OrderDTO.OrderItemDTO toOrderItemDTO(OrderItem orderItem) {
        if (orderItem == null) {
            return null;
        }

        return OrderDTO.OrderItemDTO.builder()
                                    .id(orderItem.getId())
                                    .productId(orderItem.getProduct().getId())
                                    .productTitle(orderItem.getProductTitle())
                                    .productImageUrl(orderItem.getProductImageUrl())
                                    .itemPrice(orderItem.getItemPrice())
                                    .createdAt(orderItem.getCreatedAt())
                                    .build();
    }
}