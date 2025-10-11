package com.thb.bakery.repository;

import com.thb.bakery.entity.RawMaterialEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RawMaterialRepository extends JpaRepository<RawMaterialEntity, Long> {

    List<RawMaterialEntity> findByCategory(String category);
    List<RawMaterialEntity> findByStatus(String status);
    List<RawMaterialEntity> findByVendorName(String vendorName);

}