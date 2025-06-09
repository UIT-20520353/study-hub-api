package com.backend.study_hub_api.repository;

import com.backend.study_hub_api.helper.enumeration.OrderStatus;
import com.backend.study_hub_api.model.Order;
import com.backend.study_hub_api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByStatusAndBuyerId(OrderStatus status, Long buyerId);
    List<Order> findByStatusAndSellerId(OrderStatus status, Long sellerId);

    @Query("SELECT o.status, COUNT(o) FROM Order o WHERE o.buyer = :buyer GROUP BY o.status")
    List<Object[]> countOrdersByStatusAndBuyer(@Param("buyer") User buyer);

    @Query("SELECT o.status, COUNT(o) FROM Order o WHERE o.seller = :seller GROUP BY o.status")
    List<Object[]> countOrdersByStatusAndSeller(@Param("seller") User seller);

    default Map<OrderStatus, Long> countByStatusAndBuyer(User buyer) {
        List<Object[]> results = countOrdersByStatusAndBuyer(buyer);
        return results.stream()
                      .collect(Collectors.toMap(
                              result -> (OrderStatus) result[0],
                              result -> (Long) result[1]
                      ));
    }

    default Map<OrderStatus, Long> countByStatusAndSeller(User seller) {
        List<Object[]> results = countOrdersByStatusAndSeller(seller);
        return results.stream()
                      .collect(Collectors.toMap(
                              result -> (OrderStatus) result[0],
                              result -> (Long) result[1]
                      ));
    }

}
