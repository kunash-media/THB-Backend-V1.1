package com.thb.bakery.repository;

import com.thb.bakery.entity.CustomizationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomizationRepository extends JpaRepository<CustomizationEntity, Long> {

    Page<CustomizationEntity> findAll(Pageable pageable);

}