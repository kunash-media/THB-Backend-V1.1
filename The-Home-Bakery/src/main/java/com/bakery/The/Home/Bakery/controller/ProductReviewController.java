package com.bakery.The.Home.Bakery.controller;

import com.bakery.The.Home.Bakery.dto.request.ProductReviewDTO;
import com.bakery.The.Home.Bakery.service.ProductReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/reviews")

public class ProductReviewController {

    @Autowired
    private ProductReviewService reviewService;

    /**
     * Create a new product review
     * @param reviewDTO - Review details including userId, productId, rating, comment, title
     * @return ResponseEntity with created review details
     */
    @PostMapping("/create-review")
    public ResponseEntity<?> createReview(@RequestBody ProductReviewDTO reviewDTO) {
        try {
            ProductReviewDTO createdReview = reviewService.createReview(reviewDTO);
            return new ResponseEntity<>(createdReview, HttpStatus.CREATED);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to create review: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Update an existing review
     * @param reviewId - Review ID to update
     * @param reviewDTO - Updated review details
     * @return Updated review details
     */
    @PutMapping("update-review/{reviewId}")
    public ResponseEntity<?> updateReview(@PathVariable Long reviewId,
                                          @RequestBody ProductReviewDTO reviewDTO) {
        try {
            ProductReviewDTO updatedReview = reviewService.updateReview(reviewId, reviewDTO);
            return new ResponseEntity<>(updatedReview, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to update review: " + e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Delete a review by ID
     * @param reviewId - Review ID to delete
     * @return Success message
     */
    @DeleteMapping("/delete-review/{reviewId}")
    public ResponseEntity<?> deleteReview(@PathVariable Long reviewId) {
        try {
            reviewService.deleteReview(reviewId);
            return new ResponseEntity<>("Review deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to delete review: " + e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Get a specific review by ID
     * @param reviewId - Review ID to fetch
     * @return Review details
     */
    @GetMapping("/get-review/{reviewId}")
    public ResponseEntity<?> getReviewById(@PathVariable Long reviewId) {
        try {
            ProductReviewDTO review = reviewService.getReviewById(reviewId);
            return new ResponseEntity<>(review, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Review not found: " + e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Get all reviews for a specific product with pagination
     * @param productId - Product ID to fetch reviews for
     * @param page - Page number (default: 0)
     * @param size - Page size (default: 10)
     * @param sortBy - Sort field (default: createdAt)
     * @param sortDir - Sort direction (default: desc)
     * @return Paginated list of reviews for the product
     */
    @GetMapping("/product-review/{productId}")
    public ResponseEntity<?> getReviewsByProduct(@PathVariable Long productId,
                                                 @RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "10") int size,
                                                 @RequestParam(defaultValue = "createdAt") String sortBy,
                                                 @RequestParam(defaultValue = "desc") String sortDir) {
        try {
            Sort sort = sortDir.equalsIgnoreCase("desc") ?
                    Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);

            Page<ProductReviewDTO> reviews = reviewService.getReviewsByProductId(productId, pageable);
            return new ResponseEntity<>(reviews, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to fetch product reviews: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get all reviews by a specific user with pagination
     * @param userId - User ID to fetch reviews for
     * @param page - Page number (default: 0)
     * @param size - Page size (default: 10)
     * @param sortBy - Sort field (default: createdAt)
     * @param sortDir - Sort direction (default: desc)
     * @return Paginated list of reviews by the user
     */
    @GetMapping("/user-review/{userId}")
    public ResponseEntity<?> getReviewsByUser(@PathVariable Long userId,
                                              @RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "10") int size,
                                              @RequestParam(defaultValue = "createdAt") String sortBy,
                                              @RequestParam(defaultValue = "desc") String sortDir) {
        try {
            Sort sort = sortDir.equalsIgnoreCase("desc") ?
                    Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);

            Page<ProductReviewDTO> reviews = reviewService.getReviewsByUserId(userId, pageable);
            return new ResponseEntity<>(reviews, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to fetch user reviews: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get reviews by product and rating with pagination
     * @param productId - Product ID
     * @param rating - Rating filter (1-5)
     * @param page - Page number (default: 0)
     * @param size - Page size (default: 10)
     * @return Paginated list of reviews filtered by rating
     */
    @GetMapping("/product-review/{productId}/rating/{rating}")
    public ResponseEntity<?> getReviewsByProductAndRating(@PathVariable Long productId,
                                                          @PathVariable Integer rating,
                                                          @RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
            Page<ProductReviewDTO> reviews = reviewService.getReviewsByProductIdAndRating(productId, rating, pageable);
            return new ResponseEntity<>(reviews, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to fetch reviews by rating: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get average rating for a product
     * @param productId - Product ID to calculate average rating
     * @return Average rating as Double
     */
    @GetMapping("/product-review/{productId}/average-rating")
    public ResponseEntity<?> getAverageRatingForProduct(@PathVariable Long productId) {
        try {
            Double averageRating = reviewService.getAverageRatingByProductId(productId);
            return new ResponseEntity<>(averageRating, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to calculate average rating: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get total review count for a product
     * @param productId - Product ID
     * @return Total review count
     */
    @GetMapping("/product-review/{productId}/count")
    public ResponseEntity<?> getTotalReviewCount(@PathVariable Long productId) {
        try {
            Long totalCount = reviewService.getTotalReviewCountByProductId(productId);
            return new ResponseEntity<>(totalCount, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to get review count: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get rating distribution for a product (1-5 stars with counts)
     * @param productId - Product ID
     * @return Map of rating to count
     */
    @GetMapping("/product-review/{productId}/rating-distribution")
    public ResponseEntity<?> getRatingDistribution(@PathVariable Long productId) {
        try {
            Map<Integer, Long> distribution = reviewService.getRatingDistributionByProductId(productId);
            return new ResponseEntity<>(distribution, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to get rating distribution: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Check if user has already reviewed a product
     * @param userId - User ID
     * @param productId - Product ID
     * @return Boolean indicating if review exists
     */
    @GetMapping("/user/{userId}/product/{productId}/exists")
    public ResponseEntity<?> hasUserReviewedProduct(@PathVariable Long userId, @PathVariable Long productId) {
        try {
            boolean hasReviewed = reviewService.hasUserReviewedProduct(userId, productId);
            return new ResponseEntity<>(hasReviewed, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to check review status: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Approve a review (Admin functionality)
     * @param reviewId - Review ID to approve
     * @return Approved review details
     */
    @PutMapping("/{reviewId}/approve")
    public ResponseEntity<?> approveReview(@PathVariable Long reviewId) {
        try {
            ProductReviewDTO approvedReview = reviewService.approveReview(reviewId);
            return new ResponseEntity<>(approvedReview, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to approve review: " + e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Reject a review (Admin functionality)
     * @param reviewId - Review ID to reject
     * @return Rejected review details
     */
    @PutMapping("/{reviewId}/reject")
    public ResponseEntity<?> rejectReview(@PathVariable Long reviewId) {
        try {
            ProductReviewDTO rejectedReview = reviewService.rejectReview(reviewId);
            return new ResponseEntity<>(rejectedReview, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to reject review: " + e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Get all approved reviews with pagination (Admin functionality)
     * @param page - Page number (default: 0)
     * @param size - Page size (default: 10)
     * @return Paginated list of approved reviews
     */
    @GetMapping("/approved")
    public ResponseEntity<?> getAllApprovedReviews(@RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
            Page<ProductReviewDTO> reviews = reviewService.getAllApprovedReviews(pageable);
            return new ResponseEntity<>(reviews, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to fetch approved reviews: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get pending approval reviews with pagination (Admin functionality)
     * @param page - Page number (default: 0)
     * @param size - Page size (default: 10)
     * @return Paginated list of pending reviews
     */
    @GetMapping("/pending")
    public ResponseEntity<?> getPendingApprovalReviews(@RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
            Page<ProductReviewDTO> reviews = reviewService.getPendingApprovalReviews(pageable);
            return new ResponseEntity<>(reviews, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to fetch pending reviews: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Mark a review as helpful (increment helpful count)
     * @param reviewId - Review ID to mark as helpful
     * @return Updated review with incremented helpful count
     */
    @PutMapping("/{reviewId}/helpful")
    public ResponseEntity<?> markReviewAsHelpful(@PathVariable Long reviewId) {
        try {
            ProductReviewDTO updatedReview = reviewService.markReviewAsHelpful(reviewId);
            return new ResponseEntity<>(updatedReview, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to mark review as helpful: " + e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Get verified purchase reviews for a product with pagination
     * @param productId - Product ID
     * @param page - Page number (default: 0)
     * @param size - Page size (default: 10)
     * @return Paginated list of verified purchase reviews
     */
    @GetMapping("/product/{productId}/verified")
    public ResponseEntity<?> getVerifiedPurchaseReviews(@PathVariable Long productId,
                                                        @RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
            Page<ProductReviewDTO> reviews = reviewService.getVerifiedPurchaseReviewsByProductId(productId, pageable);
            return new ResponseEntity<>(reviews, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to fetch verified purchase reviews: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}