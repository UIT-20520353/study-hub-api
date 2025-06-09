package com.backend.study_hub_api.service;

import com.backend.study_hub_api.dto.OrderCountDTO;
import com.backend.study_hub_api.dto.OrderDTO;
import com.backend.study_hub_api.dto.request.CreateOrderRequest;
import com.backend.study_hub_api.helper.enumeration.OrderStatus;
import com.backend.study_hub_api.model.Order;

import java.util.List;

public interface OrderService {

    Order createOrder(CreateOrderRequest request);

    Order confirmOrder(Long orderId);

    Order updateShippingFee(Long orderId, Integer shippingFee);

    Order markAsShipping(Long orderId);

    Order markAsDelivered(Long orderId);

    Order completeOrder(Long orderId);

    Order cancelOrder(Long orderId, String reason);

    List<OrderDTO> getOrdersByStatusAndBuyer(OrderStatus status);
    List<OrderDTO> getOrdersByStatusAndSeller(OrderStatus status);
    OrderCountDTO getOrderCountByStatusForBuyer();
    OrderCountDTO getOrderCountByStatusForSeller();
}