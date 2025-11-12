package com.thb.bakery.repository;

import com.thb.bakery.entity.CustomizeCakeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface CustomizeCakeRepository extends JpaRepository<CustomizeCakeEntity, Long> {

    // Find active product by ID
    Optional<CustomizeCakeEntity> findByIdAndIsActiveTrue(Long id);

    // Find all active products
    List<CustomizeCakeEntity> findByIsActiveTrue();

    // Find all active products with pagination
    Page<CustomizeCakeEntity> findByIsActiveTrue(Pageable pageable);

    // Search active products
    @Query("SELECT c FROM CustomizeCakeEntity c WHERE c.isActive = true AND " +
            "(LOWER(c.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(c.category) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(c.subcategory) LIKE LOWER(CONCAT('%', :query, '%')))")
    List<CustomizeCakeEntity> searchActiveProducts(@Param("query") String query);

    // Find by category
    List<CustomizeCakeEntity> findByCategoryAndIsActiveTrue(String category);

    // Find by subcategory
    List<CustomizeCakeEntity> findBySubcategoryAndIsActiveTrue(String subcategory);

    // Find by category and subcategory
    List<CustomizeCakeEntity> findByCategoryAndSubcategoryAndIsActiveTrue(String category, String subcategory);

    // Find by minimum discount
    @Query("SELECT c FROM CustomizeCakeEntity c WHERE c.isActive = true AND c.discount >= :minDiscount")
    List<CustomizeCakeEntity> findByDiscountGreaterThanEqual(@Param("minDiscount") BigDecimal minDiscount);

    // Count active products
    long countByIsActiveTrue();

    // Check if title exists

    boolean existsByTitleAndIsActiveTrue(String title);

    // ========== DELETED/INACTIVE PRODUCTS ==========

    // Find all inactive (deleted) products
    List<CustomizeCakeEntity> findByIsActiveFalse();

    // Find all inactive products with pagination
    Page<CustomizeCakeEntity> findByIsActiveFalse(Pageable pageable);

    // Count inactive products
    long countByIsActiveFalse();

    // ========== ALL PRODUCTS (INCLUDING DELETED) ==========

    // Find all products including inactive
    @Query("SELECT c FROM CustomizeCakeEntity c ORDER BY c.createdAt DESC")
    List<CustomizeCakeEntity> findAllIncludingInactive();

    // Find all products with pagination including inactive
    @Query("SELECT c FROM CustomizeCakeEntity c")
    Page<CustomizeCakeEntity> findAllIncludingInactive(Pageable pageable);
}