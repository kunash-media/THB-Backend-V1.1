package com.thb.bakery.repository;

import com.thb.bakery.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    List<OrderEntity> findByUser_UserId(Long userId);
    List<OrderEntity> findByOrderStatus(String orderStatus);
    List<OrderEntity> findByOrderDateBetween(LocalDate startDate, LocalDate endDate);

    @Query("SELECT COUNT(o) FROM OrderEntity o WHERE o.orderDate = :date")
    Long countOrdersByDate(LocalDate date);

    @Query("SELECT COUNT(o) FROM OrderEntity o WHERE YEAR(o.orderDate) = :year AND MONTH(o.orderDate) = :month")
    Long countOrdersByMonth(int year, int month);

    @Query("SELECT SUM(o.totalAmount) FROM OrderEntity o")
    Double getTotalSales();

    @Query("SELECT COUNT(o) FROM OrderEntity o")
    Long countTotalOrders();

    @Query("SELECT COUNT(o) FROM OrderEntity o WHERE o.orderStatus = :status")
    Long countOrdersByStatus(String status);

    @Query("SELECT COUNT(o) FROM OrderEntity o WHERE o.paymentMethod = :method")
    Long countOrdersByPaymentMethod(String method);


     // ======= new method added ==========//
    @Query("SELECT COUNT(o) FROM OrderEntity o WHERE o.user.userId = :userId")
    long countOrdersByUserId(@Param("userId") Long userId);

    @Query("SELECT SUM(o.totalAmount) FROM OrderEntity o WHERE o.user.userId = :userId")
    BigDecimal sumSpendsByUserId(@Param("userId") Long userId);
}