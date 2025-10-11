package com.thb.bakery.repository;


import com.thb.bakery.entity.PartyItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PartyItemRepository extends JpaRepository<PartyItemEntity, Long> {

    List<PartyItemEntity> findByOrderItem_OrderItemId(Long orderItemId);
}