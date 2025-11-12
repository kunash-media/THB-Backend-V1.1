package com.thb.bakery.repository;

import com.thb.bakery.entity.SnacksEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SnacksRepository extends JpaRepository<SnacksEntity, Long> {

    Page<SnacksEntity> findByProductCategory(String category, Pageable pageable);
    Page<SnacksEntity> findByProductSubcategory(String subcategory, Pageable pageable);
    Page<SnacksEntity> findByProductNameContainingIgnoreCase(String name, Pageable pageable);

}