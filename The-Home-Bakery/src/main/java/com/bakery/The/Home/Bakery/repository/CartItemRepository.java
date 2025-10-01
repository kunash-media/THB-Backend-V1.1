package com.bakery.The.Home.Bakery.repository;

import com.bakery.The.Home.Bakery.entity.CartItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItemEntity, Long> {
    List<CartItemEntity> findByUserId(Long userId);
    Optional<CartItemEntity> findByUserIdAndProductIdAndSize(Long userId, Long productId, String size);

    void deleteByUserId(Long userId);
}