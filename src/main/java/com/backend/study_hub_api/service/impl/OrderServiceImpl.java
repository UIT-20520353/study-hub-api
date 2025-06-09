package com.backend.study_hub_api.service.impl;

import com.backend.study_hub_api.dto.OrderCountDTO;
import com.backend.study_hub_api.dto.OrderDTO;
import com.backend.study_hub_api.dto.OrderNotificationDTO;
import com.backend.study_hub_api.dto.request.CreateOrderRequest;
import com.backend.study_hub_api.dto.request.OrderItemRequest;
import com.backend.study_hub_api.helper.enumeration.NotificationType;
import com.backend.study_hub_api.helper.enumeration.OrderStatus;
import com.backend.study_hub_api.helper.exception.BadRequestException;
import com.backend.study_hub_api.mapper.OrderMapper;
import com.backend.study_hub_api.model.*;
import com.backend.study_hub_api.repository.NotificationRepository;
import com.backend.study_hub_api.repository.OrderRepository;
import com.backend.study_hub_api.repository.ProductRepository;
import com.backend.study_hub_api.service.NotificationService;
import com.backend.study_hub_api.service.OrderService;
import com.backend.study_hub_api.service.ProductService;
import com.backend.study_hub_api.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.backend.study_hub_api.helper.constant.Message.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderServiceImpl implements OrderService {
    OrderRepository orderRepository;
    ProductRepository productRepository;
    UserService userService;
    ProductService productService;
    OrderMapper orderMapper;
    NotificationService notificationService;
    NotificationRepository notificationRepository;

    @Override
    @Transactional
    public Order createOrder(CreateOrderRequest request) {
        User buyer = userService.getCurrentUser();
        List<Product> products = validateAndGetProducts(request.getOrderItems());
        User seller = products.get(0).getSeller();

        if (buyer.getId().equals(seller.getId())) {
            throw new BadRequestException(ORDER_BUYER_SELLER_SAME_ERROR);
        }

        for (Product product : products) {
            if (!product.isAvailable()) {
                throw new BadRequestException(ORDER_PRODUCT_NOT_AVAILABLE_ERROR);
            }
        }

        Order order = Order.builder()
                .buyer(buyer)
                .seller(seller)
                .orderCode(generateOrderCode())
                .deliveryMethod(request.getDeliveryMethod())
                .deliveryAddress(request.getDeliveryAddress())
                .deliveryPhone(request.getDeliveryPhone())
                .deliveryNotes(request.getDeliveryNotes())
                .build();

        List<OrderItem> orderItems = new ArrayList<>();
        for (Product product : products) {
            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .build();
            orderItems.add(orderItem);

            product.markAsPending();
        }
        order.setOrderItems(orderItems);

        Order savedOrder = orderRepository.save(order);
        productRepository.saveAll(products);

        saveOrderNotification(savedOrder, buyer);

        return savedOrder;
    }

    @Override
    @Transactional
    public Order confirmOrder(Long orderId) {
        Order order = getOrderByIdOrThrow(orderId);
        User currentUser = userService.getCurrentUser();

        if (!order.getSeller().getId().equals(currentUser.getId())) {
            throw new BadRequestException(ORDER_ONLY_SELLER_CAN_CONFIRM_ERROR);
        }

        if (!order.isPending()) {
            throw new BadRequestException(ORDER_ONLY_PENDING_CAN_CONFIRM_ERROR);
        }

        order.markAsConfirmed();
        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public Order updateShippingFee(Long orderId, Integer shippingFee) {
        Order order = getOrderByIdOrThrow(orderId);
        User currentUser = userService.getCurrentUser();

        if (!order.getSeller().getId().equals(currentUser.getId())) {
            throw new BadRequestException(ORDER_ONLY_SELLER_CAN_UPDATE_SHIPPING_FEE_ERROR);
        }

        if (!order.isConfirmed()) {
            throw new BadRequestException(ORDER_ONLY_CONFIRMED_CAN_UPDATE_SHIPPING_FEE_ERROR);
        }

        order.updateShippingFee(shippingFee);
        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public Order markAsShipping(Long orderId) {
        Order order = getOrderByIdOrThrow(orderId);
        User currentUser = userService.getCurrentUser();

        if (!order.getSeller().getId().equals(currentUser.getId())) {
            throw new BadRequestException(ORDER_ONLY_SELLER_CAN_MARK_SHIPPING_ERROR);
        }

        if (!order.isConfirmed() && !order.isShippingFeeUpdated()) {
            throw new BadRequestException(ORDER_MUST_BE_CONFIRMED_BEFORE_SHIPPING_ERROR);
        }

        order.markAsShipping();
        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public Order markAsDelivered(Long orderId) {
        Order order = getOrderByIdOrThrow(orderId);
        User currentUser = userService.getCurrentUser();

        if (!order.getSeller().getId().equals(currentUser.getId())) {
            throw new BadRequestException(ORDER_ONLY_SELLER_CAN_MARK_DELIVERED_ERROR);
        }

        if (!order.isShipping()) {
            throw new BadRequestException(ORDER_MUST_BE_SHIPPING_TO_MARK_DELIVERED_ERROR);
        }

        order.markAsDelivered();
        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public Order completeOrder(Long orderId) {
        Order order = getOrderByIdOrThrow(orderId);
        User currentUser = userService.getCurrentUser();

        if (!order.getBuyer().getId().equals(currentUser.getId())) {
            throw new BadRequestException(ORDER_ONLY_BUYER_CAN_COMPLETE_ERROR);
        }

        if (!order.isDelivered()) {
            throw new BadRequestException(ORDER_MUST_BE_DELIVERED_TO_COMPLETE_ERROR);
        }

        order.markAsCompleted();

        List<Product> products = order.getOrderItems().stream()
                .map(OrderItem::getProduct)
                .toList();

        products.forEach(Product::markAsSold);
        productRepository.saveAll(products);

        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public Order cancelOrder(Long orderId, String reason) {
        Order order = getOrderByIdOrThrow(orderId);
        User currentUser = userService.getCurrentUser();

        if (!order.getBuyer().getId().equals(currentUser.getId()) &&
            !order.getSeller().getId().equals(currentUser.getId())) {
            throw new BadRequestException(ORDER_ONLY_BUYER_SELLER_CAN_CANCEL_ERROR);
        }

        if (!order.isPending() && !order.isConfirmed()) {
            throw new BadRequestException(ORDER_ONLY_PENDING_CONFIRMED_CAN_CANCEL_ERROR);
        }

        order.markAsCancelled(currentUser, reason);

        List<Product> products = order.getOrderItems().stream()
                                      .map(OrderItem::getProduct)
                                      .toList();

        products.forEach(Product::markAsAvailable);
        productRepository.saveAll(products);

        return orderRepository.save(order);
    }

    @Override
    public List<OrderDTO> getOrdersByStatusAndBuyer(OrderStatus status) {
        User currentUser = userService.getCurrentUser();
        return orderRepository
                .findByStatusAndBuyerId(status, currentUser.getId())
                .stream()
                .map(orderMapper::toDTO)
                .toList();
    }

    @Override
    public List<OrderDTO> getOrdersByStatusAndSeller(OrderStatus status) {
        User currentUser = userService.getCurrentUser();
        return orderRepository
                .findByStatusAndSellerId(status, currentUser.getId())
                .stream()
                .map(orderMapper::toDTO)
                .toList();
    }

    @Override
    public OrderCountDTO getOrderCountByStatusForBuyer() {
        User currentUser = userService.getCurrentUser();
        Map<OrderStatus, Long> counts = orderRepository.countByStatusAndBuyer(currentUser);
        return buildOrderCountDTO(counts);
    }

    @Override
    public OrderCountDTO getOrderCountByStatusForSeller() {
        User currentUser = userService.getCurrentUser();
        Map<OrderStatus, Long> counts = orderRepository.countByStatusAndSeller(currentUser);
        return buildOrderCountDTO(counts);
    }

    private void saveOrderNotification(Order order, User buyer) {
        String productNames = order.getOrderItems().stream()
                                   .map(item -> item.getProduct().getTitle())
                                   .limit(2)
                                   .reduce((p1, p2) -> p1 + ", " + p2)
                                   .orElse("sản phẩm");

        if (order.getOrderItems().size() > 2) {
            productNames += "...";
        }

        Notification notification = Notification.builder()
                                                .recipient(order.getSeller())
                                                .sender(buyer)
                                                .type(NotificationType.PRODUCT_ORDERED)
                                                .title("Đơn hàng mới")
                                                .content(buyer.getFullName() + " đã đặt mua " + productNames)
                                                .order(order)
                                                .isRead(false)
                                                .build();

        notificationRepository.save(notification);
        notificationService.sendNotificationToUser(order.getSeller().getId(), notification);
    }

    private OrderCountDTO buildOrderCountDTO(Map<OrderStatus, Long> counts) {
        return OrderCountDTO.builder()
                            .pending(counts.getOrDefault(OrderStatus.PENDING, 0L))
                            .confirmed(counts.getOrDefault(OrderStatus.CONFIRMED, 0L))
                            .shipping(counts.getOrDefault(OrderStatus.SHIPPING, 0L))
                            .shippingFeeUpdated(counts.getOrDefault(OrderStatus.SHIPPING_FEE_UPDATED, 0L))
                            .delivered(counts.getOrDefault(OrderStatus.DELIVERED, 0L))
                            .completed(counts.getOrDefault(OrderStatus.COMPLETED, 0L))
                            .cancelled(counts.getOrDefault(OrderStatus.CANCELLED, 0L))
                            .build();
    }

    private List<Product> validateAndGetProducts(List<OrderItemRequest> orderItems) {
        if (orderItems.isEmpty()) {
            throw new BadRequestException(ORDER_ITEMS_EMPTY_ERROR);
        }

        List<Product> products = new ArrayList<>();
        User seller = null;

        for (OrderItemRequest item : orderItems) {
            Product product = productService.getProductByIdOrThrow(item.getProductId());

            if (seller == null) {
                seller = product.getSeller();
            } else if (!seller.getId().equals(product.getSeller().getId())) {
                throw new BadRequestException(ORDER_PRODUCTS_SAME_SELLER_ERROR);
            }

            products.add(product);
        }

        return products;
    }

    private String generateOrderCode() {
        return "ORD" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private Order getOrderByIdOrThrow(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new BadRequestException(ORDER_NOT_FOUND_ERROR));
    }
}