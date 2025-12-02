package com.thb.bakery.repository;

import com.thb.bakery.entity.StaffEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StaffRepository extends JpaRepository<StaffEntity, Long> {

    @Query("SELECT s FROM StaffEntity s WHERE " +
            "(:search IS NULL OR LOWER(s.name) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(s.email) LIKE LOWER(CONCAT('%', :search, '%'))) " +
            "AND (:role IS NULL OR s.role = :role) " +
            "AND (:status IS NULL OR s.status = :status)")
    List<StaffEntity> findFiltered(@Param("search") String search, @Param("role") String role, @Param("status") String status);


    // NEW METHOD: Find the latest employee by employeeId
    @Query("SELECT s FROM StaffEntity s ORDER BY s.employeeId DESC")
    List<StaffEntity> findAllOrderByEmployeeIdDesc();

    // Find by employeeId
    Optional<StaffEntity> findByEmployeeId(String employeeId);

    // Check if employeeId exists
    boolean existsByEmployeeId(String employeeId);

}