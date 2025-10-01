package com.bakery.The.Home.Bakery.repository;

import com.bakery.The.Home.Bakery.entity.WishlistItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WishlistItemRepository extends JpaRepository<WishlistItemEntity, Long> {
    List<WishlistItemEntity> findByUserId(Long userId);
    Optional<WishlistItemEntity> findByUserIdAndProductId(Long userId, Long productId);
    void deleteByUserId(Long userId);
}
