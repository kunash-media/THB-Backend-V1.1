package com.bakery.The.Home.Bakery.repository;

import com.bakery.The.Home.Bakery.entity.ProductReviewEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductReviewRepository extends JpaRepository<ProductReviewEntity, Long> {

    /**
     * Find all reviews for a specific product
     */
    @Query("SELECT pr FROM ProductReviewEntity pr WHERE pr.product.productId = :productId AND pr.isApproved = true ORDER BY pr.createdAt DESC")
    Page<ProductReviewEntity> findByProductId(@Param("productId") Long productId, Pageable pageable);

    /**
     * Find all reviews by a specific user
     */
    @Query("SELECT pr FROM ProductReviewEntity pr WHERE pr.user.userId = :userId ORDER BY pr.createdAt DESC")
    Page<ProductReviewEntity> findByUserId(@Param("userId") Long userId, Pageable pageable);

    /**
     * Find review by user and product (to check if user already reviewed the product)
     */
    @Query("SELECT pr FROM ProductReviewEntity pr WHERE pr.user.userId = :userId AND pr.product.productId = :productId")
    Optional<ProductReviewEntity> findByUserIdAndProductId(@Param("userId") Long userId, @Param("productId") Long productId);

    /**
     * Find reviews by rating for a specific product
     */
    @Query("SELECT pr FROM ProductReviewEntity pr WHERE pr.product.productId = :productId AND pr.rating = :rating AND pr.isApproved = true ORDER BY pr.createdAt DESC")
    Page<ProductReviewEntity> findByProductIdAndRating(@Param("productId") Long productId, @Param("rating") Integer rating, Pageable pageable);

    /**
     * Calculate average rating for a product
     */
    @Query("SELECT AVG(pr.rating) FROM ProductReviewEntity pr WHERE pr.product.productId = :productId AND pr.isApproved = true")
    Double calculateAverageRatingByProductId(@Param("productId") Long productId);

    /**
     * Count total reviews for a product
     */
    @Query("SELECT COUNT(pr) FROM ProductReviewEntity pr WHERE pr.product.productId = :productId AND pr.isApproved = true")
    Long countReviewsByProductId(@Param("productId") Long productId);

    /**
     * Find all approved reviews
     */
    @Query("SELECT pr FROM ProductReviewEntity pr WHERE pr.isApproved = true ORDER BY pr.createdAt DESC")
    Page<ProductReviewEntity> findAllApprovedReviews(Pageable pageable);

    /**
     * Find reviews pending approval
     */
    @Query("SELECT pr FROM ProductReviewEntity pr WHERE pr.isApproved = false ORDER BY pr.createdAt DESC")
    Page<ProductReviewEntity> findPendingApprovalReviews(Pageable pageable);

    /**
     * Find verified purchase reviews for a product
     */
    @Query("SELECT pr FROM ProductReviewEntity pr WHERE pr.product.productId = :productId AND pr.isVerifiedPurchase = true AND pr.isApproved = true ORDER BY pr.createdAt DESC")
    Page<ProductReviewEntity> findVerifiedPurchaseReviewsByProductId(@Param("productId") Long productId, Pageable pageable);

    /**
     * Get rating distribution for a product
     */
    @Query("SELECT pr.rating, COUNT(pr) FROM ProductReviewEntity pr WHERE pr.product.productId = :productId AND pr.isApproved = true GROUP BY pr.rating ORDER BY pr.rating DESC")
    List<Object[]> getRatingDistributionByProductId(@Param("productId") Long productId);

    /**
     * Find recent reviews (last 30 days)
     */
    @Query("SELECT pr FROM ProductReviewEntity pr WHERE pr.createdAt >= :thirtyDaysAgo AND pr.isApproved = true ORDER BY pr.createdAt DESC")
    Page<ProductReviewEntity> findRecentReviews(@Param("thirtyDaysAgo") LocalDateTime thirtyDaysAgo, Pageable pageable);
}