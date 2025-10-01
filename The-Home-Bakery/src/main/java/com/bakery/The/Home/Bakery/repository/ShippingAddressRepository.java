package com.bakery.The.Home.Bakery.repository;


import com.bakery.The.Home.Bakery.entity.ShippingAddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShippingAddressRepository extends JpaRepository<ShippingAddressEntity, Long> {
    List<ShippingAddressEntity> findByUserUserId(Long userId);
}