package com.bakery.The.Home.Bakery.repository;

import com.bakery.The.Home.Bakery.entity.ProductEntity;
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

    List<ProductEntity> findByProductFoodType(String foodType);


}