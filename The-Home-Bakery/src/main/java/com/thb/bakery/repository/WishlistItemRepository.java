package com.thb.bakery.repository;

import com.thb.bakery.entity.WishlistItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WishlistItemRepository extends JpaRepository<WishlistItemEntity, Long> {

    // Basic finds
    List<WishlistItemEntity> findByUserId(Long userId);

    Optional<WishlistItemEntity> findByUserIdAndProductProductId(Long userId, Long productId);
    Optional<WishlistItemEntity> findByUserIdAndSnackSnackId(Long userId, Long snackId);
    Optional<WishlistItemEntity> findByUserIdAndCustomizeCakeId(Long userId, Long customizeCakeId);

    // Delete by user
    void deleteByUserId(Long userId);

    // Get all with items loaded (avoids N+1 problem)
    @Query("SELECT w FROM WishlistItemEntity w " +
            "LEFT JOIN FETCH w.product " +
            "LEFT JOIN FETCH w.snack " +
            "LEFT JOIN FETCH w.customizeCake " +
            "WHERE w.userId = :userId")
    List<WishlistItemEntity> findByUserIdWithItems(@Param("userId") Long userId);

    // Count items for a user
    long countByUserId(Long userId);
}