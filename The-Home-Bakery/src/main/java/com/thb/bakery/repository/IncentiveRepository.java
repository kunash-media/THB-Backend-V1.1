package com.thb.bakery.repository;

import com.thb.bakery.entity.IncentiveEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IncentiveRepository extends JpaRepository<IncentiveEntity, Long> {

    @Modifying
    @Query("DELETE FROM IncentiveEntity i WHERE i.staff.staffid = :staffId")
    void deleteByStaffId(@Param("staffId") Long staffId);
}
