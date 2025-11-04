package com.thb.bakery.repository;

import com.thb.bakery.entity.ExpenseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<ExpenseEntity, Long> {

    // Find by status
    List<ExpenseEntity> findByStatus(String status);

    // Find by category
    List<ExpenseEntity> findByCategory(String category);

    // Find by staff name
    List<ExpenseEntity> findByStaffNameContainingIgnoreCase(String staffName);

    // Find by date range
    List<ExpenseEntity> findByDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    // Complex search query
    @Query("SELECT e FROM ExpenseEntity e WHERE " +
            "(:searchTerm IS NULL OR LOWER(e.staffName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(e.note) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +
            "AND (:category IS NULL OR :category = 'ALL' OR e.category = :category) " +
            "AND (:staffName IS NULL OR LOWER(e.staffName) LIKE LOWER(CONCAT('%', :staffName, '%'))) " +
            "AND (:dateFilter IS NULL OR CAST(e.date AS string) LIKE CONCAT(:dateFilter, '%')) " +
            "AND e.status != 'DELETED'")
    Page<ExpenseEntity> searchExpenses(
            @Param("searchTerm") String searchTerm,
            @Param("category") String category,
            @Param("staffName") String staffName,
            @Param("dateFilter") String dateFilter,
            Pageable pageable
    );

    // Get all active expenses
    List<ExpenseEntity> findByStatusNot(String status);

    // Find expenses by month and year
    @Query("SELECT e FROM ExpenseEntity e WHERE " +
            "YEAR(e.date) = :year AND MONTH(e.date) = :month " +
            "AND e.status != 'DELETED'")
    List<ExpenseEntity> findByMonthAndYear(
            @Param("month") int month,
            @Param("year") int year
    );

    // Calculate total amount for a period
    @Query("SELECT SUM(e.amount) FROM ExpenseEntity e WHERE " +
            "e.date BETWEEN :startDate AND :endDate " +
            "AND e.status != 'DELETED'")
    Double calculateTotalAmount(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
}