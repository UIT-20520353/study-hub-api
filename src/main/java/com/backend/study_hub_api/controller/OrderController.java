package com.backend.study_hub_api.controller;

import com.backend.study_hub_api.dto.OrderCountDTO;
import com.backend.study_hub_api.dto.OrderDTO;
import com.backend.study_hub_api.dto.request.CancelOrderRequest;
import com.backend.study_hub_api.dto.request.CreateOrderRequest;
import com.backend.study_hub_api.dto.request.UpdateShippingFeeRequest;
import com.backend.study_hub_api.helper.enumeration.OrderStatus;
import com.backend.study_hub_api.model.Order;
import com.backend.study_hub_api.service.OrderService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/user/orders")
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderController {
    OrderService orderService;

    @PostMapping
    public ResponseEntity<Order> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        return ResponseEntity.ok(orderService.createOrder(request));
    }

    @PutMapping("/{orderId}/confirm")
    public ResponseEntity<Order> confirmOrder(@PathVariable Long orderId) {
        Order order = orderService.confirmOrder(orderId);
        return ResponseEntity.ok(order);
    }

    @PutMapping("/{orderId}/shipping-fee")
    public ResponseEntity<Order> updateShippingFee(
            @PathVariable Long orderId,
            @Valid @RequestBody UpdateShippingFeeRequest request) {
        Order order = orderService.updateShippingFee(orderId, request.getShippingFee());
        return ResponseEntity.ok(order);
    }

    @PutMapping("/{orderId}/ship")
    public ResponseEntity<Order> markAsShipping(@PathVariable Long orderId) {
        Order order = orderService.markAsShipping(orderId);
        return ResponseEntity.ok(order);
    }

    @PutMapping("/{orderId}/deliver")
    public ResponseEntity<Order> markAsDelivered(@PathVariable Long orderId) {
        Order order = orderService.markAsDelivered(orderId);
        return ResponseEntity.ok(order);
    }

    @PutMapping("/{orderId}/complete")
    public ResponseEntity<Order> completeOrder(@PathVariable Long orderId) {
        Order order = orderService.completeOrder(orderId);
        return ResponseEntity.ok(order);
    }

    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<Order> cancelOrder(
            @PathVariable Long orderId,
            @Valid @RequestBody CancelOrderRequest request) {
        Order order = orderService.cancelOrder(orderId, request.getReason());
        return ResponseEntity.ok(order);
    }

    @GetMapping("/bought")
    public ResponseEntity<List<OrderDTO>> getOrdersByStatus(@RequestParam(value = "status", required = true) OrderStatus status) {
        List<OrderDTO> orders = orderService.getOrdersByStatusAndBuyer(status);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/sold")
    public ResponseEntity<List<OrderDTO>> getOrdersByStatusAndSeller(@RequestParam(value = "status", required = true) OrderStatus status) {
        List<OrderDTO> orders = orderService.getOrdersByStatusAndSeller(status);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/bought/count")
    public ResponseEntity<OrderCountDTO> getOrderCountByStatusForBuyer() {
        OrderCountDTO orderCounts = orderService.getOrderCountByStatusForBuyer();
        return ResponseEntity.ok(orderCounts);
    }

    @GetMapping("/sold/count")
    public ResponseEntity<OrderCountDTO> getOrderCountByStatusForSeller() {
        OrderCountDTO orderCounts = orderService.getOrderCountByStatusForSeller();
        return ResponseEntity.ok(orderCounts);
    }
}
