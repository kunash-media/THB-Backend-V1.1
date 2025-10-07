package com.thb.bakery.service.serviceImpl;

import com.thb.bakery.dto.request.ProductReviewDTO;
import com.thb.bakery.entity.ProductEntity;
import com.thb.bakery.entity.ProductReviewEntity;
import com.thb.bakery.entity.UserEntity;
import com.thb.bakery.repository.ProductRepository;
import com.thb.bakery.repository.ProductReviewRepository;
import com.thb.bakery.repository.UserRepository;
import com.thb.bakery.service.ProductReviewService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class ProductReviewServiceImpl implements ProductReviewService {

    @Autowired
    private ProductReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public ProductReviewDTO createReview(ProductReviewDTO reviewDTO) {
        // Validate user exists
        UserEntity user = userRepository.findById(reviewDTO.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + reviewDTO.getUserId()));

        // Validate product exists
        ProductEntity product = productRepository.findById(reviewDTO.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + reviewDTO.getProductId()));

        // Check if user has already reviewed this product
        Optional<ProductReviewEntity> existingReview = reviewRepository.findByUserIdAndProductId(
                reviewDTO.getUserId(), reviewDTO.getProductId());

        if (existingReview.isPresent()) {
            throw new IllegalStateException("User has already reviewed this product");
        }

        // Create new review entity
        ProductReviewEntity reviewEntity = new ProductReviewEntity();
        reviewEntity.setUser(user);
        reviewEntity.setProduct(product);
        reviewEntity.setRating(reviewDTO.getRating());
        reviewEntity.setReviewTitle(reviewDTO.getReviewTitle());
        reviewEntity.setReviewComment(reviewDTO.getReviewComment());
        reviewEntity.setVerifiedPurchase(reviewDTO.isVerifiedPurchase());
        reviewEntity.setApproved(true); // Auto-approve new reviews, can be changed based on business logic

        // Save the review
        ProductReviewEntity savedReview = reviewRepository.save(reviewEntity);

        // Convert to DTO and return
        return convertToDTO(savedReview);
    }

    @Override
    public ProductReviewDTO updateReview(Long reviewId, ProductReviewDTO reviewDTO) {
        ProductReviewEntity existingReview = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("Review not found with ID: " + reviewId));

        // Update fields
        existingReview.setRating(reviewDTO.getRating());
        existingReview.setReviewTitle(reviewDTO.getReviewTitle());
        existingReview.setReviewComment(reviewDTO.getReviewComment());

        // Save updated review
        ProductReviewEntity updatedReview = reviewRepository.save(existingReview);

        return convertToDTO(updatedReview);
    }

    @Override
    public void deleteReview(Long reviewId) {
        if (!reviewRepository.existsById(reviewId)) {
            throw new EntityNotFoundException("Review not found with ID: " + reviewId);
        }
        reviewRepository.deleteById(reviewId);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductReviewDTO getReviewById(Long reviewId) {
        ProductReviewEntity review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("Review not found with ID: " + reviewId));

        return convertToDTO(review);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductReviewDTO> getReviewsByProductId(Long productId, Pageable pageable) {
        Page<ProductReviewEntity> reviews = reviewRepository.findByProductId(productId, pageable);
        return reviews.map(this::convertToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductReviewDTO> getReviewsByUserId(Long userId, Pageable pageable) {
        Page<ProductReviewEntity> reviews = reviewRepository.findByUserId(userId, pageable);
        return reviews.map(this::convertToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductReviewDTO> getReviewsByProductIdAndRating(Long productId, Integer rating, Pageable pageable) {
        Page<ProductReviewEntity> reviews = reviewRepository.findByProductIdAndRating(productId, rating, pageable);
        return reviews.map(this::convertToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Double getAverageRatingByProductId(Long productId) {
        Double average = reviewRepository.calculateAverageRatingByProductId(productId);
        return average != null ? Math.round(average * 100.0) / 100.0 : 0.0;
    }

    @Override
    @Transactional(readOnly = true)
    public Long getTotalReviewCountByProductId(Long productId) {
        return reviewRepository.countReviewsByProductId(productId);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<Integer, Long> getRatingDistributionByProductId(Long productId) {
        List<Object[]> results = reviewRepository.getRatingDistributionByProductId(productId);
        Map<Integer, Long> distribution = new HashMap<>();

        // Initialize all ratings with 0 count
        for (int i = 1; i <= 5; i++) {
            distribution.put(i, 0L);
        }

        // Fill with actual counts
        for (Object[] result : results) {
            Integer rating = (Integer) result[0];
            Long count = (Long) result[1];
            distribution.put(rating, count);
        }

        return distribution;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasUserReviewedProduct(Long userId, Long productId) {
        return reviewRepository.findByUserIdAndProductId(userId, productId).isPresent();
    }

    @Override
    public ProductReviewDTO approveReview(Long reviewId) {
        ProductReviewEntity review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("Review not found with ID: " + reviewId));

        review.setApproved(true);
        ProductReviewEntity updatedReview = reviewRepository.save(review);

        return convertToDTO(updatedReview);
    }

    @Override
    public ProductReviewDTO rejectReview(Long reviewId) {
        ProductReviewEntity review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("Review not found with ID: " + reviewId));

        review.setApproved(false);
        ProductReviewEntity updatedReview = reviewRepository.save(review);

        return convertToDTO(updatedReview);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductReviewDTO> getAllApprovedReviews(Pageable pageable) {
        Page<ProductReviewEntity> reviews = reviewRepository.findAllApprovedReviews(pageable);
        return reviews.map(this::convertToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductReviewDTO> getPendingApprovalReviews(Pageable pageable) {
        Page<ProductReviewEntity> reviews = reviewRepository.findPendingApprovalReviews(pageable);
        return reviews.map(this::convertToDTO);
    }

    @Override
    public ProductReviewDTO markReviewAsHelpful(Long reviewId) {
        ProductReviewEntity review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("Review not found with ID: " + reviewId));

        review.setHelpfulCount(review.getHelpfulCount() + 1);
        ProductReviewEntity updatedReview = reviewRepository.save(review);

        return convertToDTO(updatedReview);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductReviewDTO> getVerifiedPurchaseReviewsByProductId(Long productId, Pageable pageable) {
        Page<ProductReviewEntity> reviews = reviewRepository.findVerifiedPurchaseReviewsByProductId(productId, pageable);
        return reviews.map(this::convertToDTO);
    }


    public Page<ProductReviewEntity> getRecentReviews(Pageable pageable) {
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        return reviewRepository.findRecentReviews(thirtyDaysAgo, pageable);
    }

    /**
     * Convert ProductReviewEntity to ProductReviewDTO
     * @param entity The entity to convert
     * @return The converted DTO
     */
    private ProductReviewDTO convertToDTO(ProductReviewEntity entity) {
        ProductReviewDTO dto = new ProductReviewDTO();
        dto.setReviewId(entity.getReviewId());
        dto.setUserId(entity.getUser().getUserId());
        dto.setProductId(entity.getProduct().getProductId());
        dto.setRating(entity.getRating());
        dto.setReviewTitle(entity.getReviewTitle());
        dto.setReviewComment(entity.getReviewComment());
        dto.setVerifiedPurchase(entity.isVerifiedPurchase());
        dto.setApproved(entity.isApproved());
        dto.setHelpfulCount(entity.getHelpfulCount());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());

        // Set user and product information
        dto.setCustomerName(entity.getUser().getCustomerName());
        dto.setProductTitle(entity.getProduct().getProductName());

        return dto;
    }
}