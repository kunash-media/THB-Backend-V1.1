package com.thb.bakery.repository;

import com.thb.bakery.entity.MaterialHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MaterialHistoryRepository extends JpaRepository<MaterialHistoryEntity, Long> {

    List<MaterialHistoryEntity> findByMaterialId(Long materialId);
    List<MaterialHistoryEntity> findByMaterialIdOrderByDateDesc(Long materialId);

}