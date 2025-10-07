package com.thb.bakery.service;


import com.thb.bakery.dto.request.ProductReviewDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface ProductReviewService {

    /**
     * Create a new product review
     * @param reviewDTO The review data to create
     * @return The created review DTO
     */
    ProductReviewDTO createReview(ProductReviewDTO reviewDTO);

    /**
     * Update an existing review
     * @param reviewId The ID of the review to update
     * @param reviewDTO The updated review data
     * @return The updated review DTO
     */
    ProductReviewDTO updateReview(Long reviewId, ProductReviewDTO reviewDTO);

    /**
     * Delete a review by ID
     * @param reviewId The ID of the review to delete
     */
    void deleteReview(Long reviewId);

    /**
     * Get a review by ID
     * @param reviewId The ID of the review
     * @return The review DTO
     */
    ProductReviewDTO getReviewById(Long reviewId);

    /**
     * Get all reviews for a specific product
     * @param productId The product ID
     * @param pageable Pagination information
     * @return Page of review DTOs
     */
    Page<ProductReviewDTO> getReviewsByProductId(Long productId, Pageable pageable);

    /**
     * Get all reviews by a specific user
     * @param userId The user ID
     * @param pageable Pagination information
     * @return Page of review DTOs
     */
    Page<ProductReviewDTO> getReviewsByUserId(Long userId, Pageable pageable);

    /**
     * Get reviews by product and rating
     * @param productId The product ID
     * @param rating The rating filter
     * @param pageable Pagination information
     * @return Page of review DTOs
     */
    Page<ProductReviewDTO> getReviewsByProductIdAndRating(Long productId, Integer rating, Pageable pageable);

    /**
     * Calculate average rating for a product
     * @param productId The product ID
     * @return Average rating
     */
    Double getAverageRatingByProductId(Long productId);

    /**
     * Get total review count for a product
     * @param productId The product ID
     * @return Total review count
     */
    Long getTotalReviewCountByProductId(Long productId);

    /**
     * Get rating distribution for a product
     * @param productId The product ID
     * @return Map of rating to count
     */
    Map<Integer, Long> getRatingDistributionByProductId(Long productId);

    /**
     * Check if user has already reviewed a product
     * @param userId The user ID
     * @param productId The product ID
     * @return true if user has reviewed, false otherwise
     */
    boolean hasUserReviewedProduct(Long userId, Long productId);

    /**
     * Approve a review
     * @param reviewId The review ID
     * @return Updated review DTO
     */
    ProductReviewDTO approveReview(Long reviewId);

    /**
     * Reject a review
     * @param reviewId The review ID
     * @return Updated review DTO
     */
    ProductReviewDTO rejectReview(Long reviewId);

    /**
     * Get all approved reviews
     * @param pageable Pagination information
     * @return Page of approved review DTOs
     */
    Page<ProductReviewDTO> getAllApprovedReviews(Pageable pageable);

    /**
     * Get reviews pending approval
     * @param pageable Pagination information
     * @return Page of pending review DTOs
     */
    Page<ProductReviewDTO> getPendingApprovalReviews(Pageable pageable);

    /**
     * Mark review as helpful
     * @param reviewId The review ID
     * @return Updated review DTO
     */
    ProductReviewDTO markReviewAsHelpful(Long reviewId);

    /**
     * Get verified purchase reviews for a product
     * @param productId The product ID
     * @param pageable Pagination information
     * @return Page of verified purchase review DTOs
     */
    Page<ProductReviewDTO> getVerifiedPurchaseReviewsByProductId(Long productId, Pageable pageable);
}