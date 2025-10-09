package com.thb.bakery.repository;

import com.thb.bakery.entity.POSEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface POSRepository extends JpaRepository<POSEntity, Long> {

    Optional<POSEntity> findByOrderId(String orderId);
    List<POSEntity> findByCustomerName(String customerName);
    Page<POSEntity> findAll(Pageable pageable);
}