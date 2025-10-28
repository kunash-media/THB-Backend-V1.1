package com.thb.bakery.repository;

import com.thb.bakery.entity.CartItemEntity;
import com.thb.bakery.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface CartItemRepository extends JpaRepository<CartItemEntity, Long> {


    // Basic find methods
    List<CartItemEntity> findByUserId(Long userId);

    Optional<CartItemEntity> findByUserIdAndProductAndSize(Long userId, ProductEntity product, String size);

    @Query("SELECT c FROM CartItemEntity c WHERE c.userId = :userId AND c.product.productId = :productId AND c.size = :size")
    Optional<CartItemEntity> findByUserIdAndProductIdAndSize(@Param("userId") Long userId, @Param("productId") Long productId, @Param("size") String size);

    // Delete methods
    void deleteByUserId(Long userId);

    // Eager fetch with product and addons
    @Query("SELECT c FROM CartItemEntity c JOIN FETCH c.product LEFT JOIN FETCH c.addons WHERE c.userId = :userId")
    List<CartItemEntity> findByUserIdWithProductAndAddons(@Param("userId") Long userId);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM CartItemEntity c WHERE c.userId = :userId AND c.product.productId = :productId AND c.size = :size")
    boolean existsByUserIdAndProductIdAndSize(@Param("userId") Long userId, @Param("productId") Long productId, @Param("size") String size);

    int countByUserId(Long userId);

    @Modifying
    @Query("DELETE FROM CartItemEntity c WHERE c.userId = :userId AND c.product.productId = :productId AND c.size = :size")
    void deleteByUserIdAndProductIdAndSize(@Param("userId") Long userId, @Param("productId") Long productId, @Param("size") String size);

}