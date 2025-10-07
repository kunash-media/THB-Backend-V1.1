package com.thb.bakery.repository;

import com.thb.bakery.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    Optional<ProductEntity> findBySkuNumber(String skuNumber);

    List<ProductEntity> findByProductNameContainingIgnoreCase(String name);



    @Query("SELECT p FROM ProductEntity p WHERE p.productCategory = :category AND p.deleted = false")
    List<ProductEntity> findByProductCategoryAndNotDeleted(String category);


    // Update to return Page for pagination
    @Query("SELECT p FROM ProductEntity p WHERE p.productCategory = :category AND p.deleted = false")
    Page<ProductEntity> findByProductCategoryAndNotDeleted(String category, Pageable pageable);

    List<ProductEntity> findByProductFoodType(String foodType);


}