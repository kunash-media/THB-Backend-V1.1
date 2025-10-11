package com.thb.bakery.repository;

import com.thb.bakery.entity.InventoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryRepository extends JpaRepository<InventoryEntity, Long> {

    @Query(value = "SELECT COUNT(*) FROM inventory WHERE current_stock < min_threshold", nativeQuery = true)
    Integer countLowStockItems();

    @Query(value = "SELECT SUM(current_stock * unit_price) FROM inventory", nativeQuery = true)
    Double getTotalValue();

    @Query(value = "SELECT DISTINCT category FROM inventory", nativeQuery = true)
    List<String> getAllCategories();

    @Query(value = "SELECT COUNT(*) FROM inventory WHERE current_stock < :lowThreshold", nativeQuery = true)
    Integer countByStockLevelLow(@Param("lowThreshold") Integer lowThreshold);

    @Query(value = "SELECT COUNT(*) FROM inventory WHERE current_stock >= :lowThreshold AND current_stock < :mediumThreshold", nativeQuery = true)
    Integer countByStockLevelMedium(@Param("lowThreshold") Integer lowThreshold, @Param("mediumThreshold") Integer mediumThreshold);

    @Query(value = "SELECT COUNT(*) FROM inventory WHERE current_stock >= :mediumThreshold", nativeQuery = true)
    Integer countByStockLevelHigh(@Param("mediumThreshold") Integer mediumThreshold);
}