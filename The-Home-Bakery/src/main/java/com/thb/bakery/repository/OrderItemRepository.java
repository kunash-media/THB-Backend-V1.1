package com.thb.bakery.repository;

import com.thb.bakery.entity.OrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItemEntity, Long> {

    List<OrderItemEntity> findByOrder_OrderId(Long orderId);
    List<OrderItemEntity> findByProduct_ProductId(Long productId);
}