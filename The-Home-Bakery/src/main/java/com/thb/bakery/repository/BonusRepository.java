package com.thb.bakery.repository;

import com.thb.bakery.entity.BonusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BonusRepository extends JpaRepository<BonusEntity, Long> {


    // ADD THIS METHOD: Delete all attendance records for a specific staff member
    @Modifying
    @Query("DELETE FROM AttendanceEntity a WHERE a.staff.staffid = :staffId")
    void deleteByStaffId(@Param("staffId") Long staffId);

}
