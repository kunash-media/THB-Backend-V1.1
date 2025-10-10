package com.thb.bakery.repository;

import com.thb.bakery.entity.AttendanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<AttendanceEntity, Long> {

    // Find attendance by staff and specific date
    Optional<AttendanceEntity> findByStaffStaffidAndAttendanceDate(Long staffId, LocalDate date);

    // Find attendance by staff and date range
    List<AttendanceEntity> findByStaffStaffidAndAttendanceDateBetween(Long staffId, LocalDate startDate, LocalDate endDate);

    // Find all attendance for a specific date
    List<AttendanceEntity> findByAttendanceDate(LocalDate date);

    // Find attendance by date range
    List<AttendanceEntity> findByAttendanceDateBetween(LocalDate startDate, LocalDate endDate);

    // Find attendance by staff for a specific month
    @Query("SELECT a FROM AttendanceEntity a WHERE a.staff.staffid = :staffId AND YEAR(a.attendanceDate) = :year AND MONTH(a.attendanceDate) = :month")
    List<AttendanceEntity> findByStaffAndMonth(@Param("staffId") Long staffId, @Param("year") int year, @Param("month") int month);

    // Check if attendance already exists for staff on date
    boolean existsByStaffStaffidAndAttendanceDate(Long staffId, LocalDate date);
}