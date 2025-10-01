package com.bakery.The.Home.Bakery.repository;

import com.bakery.The.Home.Bakery.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    // Find orders by user ID
    List<OrderEntity> findByUser_UserId(Long userId);

    // Find orders by order status
    List<OrderEntity> findByOrderStatus(String orderStatus);

    // Find orders by date range
    List<OrderEntity> findByOrderDateBetween(LocalDate startDate, LocalDate endDate);

    // Find orders by user and status
    List<OrderEntity> findByUser_UserIdAndOrderStatus(Long userId, String orderStatus);

    // Find orders by payment method
    List<OrderEntity> findByPaymentMethod(String paymentMethod);

    // Check if coupon code exists for user
    @Query("SELECT COUNT(o) > 0 FROM OrderEntity o WHERE o.couponAppliedCode = :couponCode AND o.user.userId = :userId")
    boolean existsByCouponAppliedCodeAndUserId(@Param("couponCode") String couponCode, @Param("userId") Long userId);

    // Dashboard queries
    @Query("SELECT COUNT(o) FROM OrderEntity o WHERE o.orderDate = :date")
    long countOrdersByDate(LocalDate date);

    @Query("SELECT COUNT(o) FROM OrderEntity o WHERE YEAR(o.orderDate) = :year AND MONTH(o.orderDate) = :month")
    long countOrdersByMonth(int year, int month);

    @Query("SELECT COALESCE(SUM(o.totalAmount), 0) FROM OrderEntity o")
    BigDecimal getTotalSales();

    @Query("SELECT COUNT(o) FROM OrderEntity o")
    long countTotalOrders();

    @Query("SELECT COUNT(o) FROM OrderEntity o WHERE o.orderStatus = :status")
    long countOrdersByStatus(String status);

    @Query("SELECT COUNT(o) FROM OrderEntity o WHERE o.paymentMethod = :paymentMethod")
    long countOrdersByPaymentMethod(String paymentMethod);
}