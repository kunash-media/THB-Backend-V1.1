package com.thb.bakery.repository;

import com.thb.bakery.entity.OrderEntity;
import com.thb.bakery.entity.OrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {


    // Order retrieval methods
    List<OrderEntity> findByUser_UserId(Long userId);
    List<OrderEntity> findByOrderStatus(String orderStatus);

    // Sales statistics methods
    //NEW
    @Query("SELECT COALESCE(SUM(o.totalAmount), 0) FROM OrderEntity o WHERE o.orderStatus != 'cancelled'")
    BigDecimal getTotalSales();

    //NEW
    @Query("SELECT COUNT(o) FROM OrderEntity o WHERE o.orderStatus != 'cancelled'")
    Long countTotalOrders();


    //NEW
    @Query("SELECT COUNT(o) FROM OrderEntity o WHERE o.orderStatus = :status")
    Long countOrdersByStatus(String status);

    //NEW
    @Query("SELECT COUNT(o) FROM OrderEntity o WHERE o.paymentMethod = :method")
    Long countOrdersByPaymentMethod(String method);

    //new dashboard start--
// Add these methods to your OrderRepository

    @Query("SELECT COALESCE(SUM(o.totalAmount), 0) FROM OrderEntity o " +
            "WHERE o.orderDateTime LIKE CONCAT(:date, '%') AND o.orderStatus != 'cancelled'")
    BigDecimal getSalesByDate(@Param("date") String date);

    // For product category distribution - simplified version
    @Query("SELECT p.productCategory, COUNT(oi) FROM OrderItemEntity oi " +
            "JOIN oi.product p " +
            "WHERE oi.order.orderStatus != 'cancelled' " +
            "GROUP BY p.productCategory")
    List<Object[]> getProductCategoryCounts();

    // Alternative if the above doesn't work - get all order items and process in service
    @Query("SELECT oi FROM OrderItemEntity oi " +
            "JOIN FETCH oi.product p " +
            "WHERE oi.order.orderStatus != 'cancelled'")
    List<OrderItemEntity> getOrderItemsWithProducts();
    //new dashboard end--

    // Add these methods to your OrderRepository interface
    @Query("SELECT COALESCE(SUM(o.totalAmount), 0) FROM OrderEntity o WHERE DATE(o.orderDateTime) = CURRENT_DATE AND o.orderStatus != 'cancelled'")
    BigDecimal getTodaySales();

    @Query("SELECT COUNT(o) FROM OrderEntity o WHERE DATE(o.orderDateTime) = CURRENT_DATE AND o.orderStatus != 'cancelled'")
    Long countTodayOrders();

    @Query("SELECT o FROM OrderEntity o WHERE o.orderStatus != 'cancelled' ORDER BY o.orderDateTime DESC LIMIT :limit")
    List<OrderEntity> findRecentOrders(@Param("limit") int limit);

    // For sales trend data (last 7 days)
    @Query("SELECT DATE(o.orderDateTime) as date, COALESCE(SUM(o.totalAmount), 0) as sales " +
            "FROM OrderEntity o " +
            "WHERE o.orderDateTime >= :startDate AND o.orderStatus != 'cancelled' " +
            "GROUP BY DATE(o.orderDateTime) " +
            "ORDER BY DATE(o.orderDateTime) DESC")
    List<Object[]> getSalesTrendLast7Days(@Param("startDate") String startDate);

    // User-specific statistics
    @Query("SELECT COUNT(o) FROM OrderEntity o WHERE o.user.userId = :userId")
    long countOrdersByUserId(@Param("userId") Long userId);

    @Query("SELECT SUM(o.totalAmount) FROM OrderEntity o WHERE o.user.userId = :userId")
    BigDecimal sumSpendsByUserId(@Param("userId") Long userId);

    // FIXED: Date-based queries for String date fields
    @Query("SELECT o FROM OrderEntity o WHERE o.orderDateTime BETWEEN :startDate AND :endDate")
    List<OrderEntity> findByOrderDateBetween(@Param("startDate") String startDate,
                                             @Param("endDate") String endDate);

    @Query("SELECT COUNT(o) FROM OrderEntity o WHERE o.orderDateTime LIKE CONCAT(:date, '%')")
    Long countOrdersByDate(@Param("date") String date);

    @Query("SELECT COUNT(o) FROM OrderEntity o WHERE o.orderDateTime LIKE CONCAT(:year, '-', :month, '%')")
    Long countOrdersByMonth(@Param("year") String year, @Param("month") String month);

    @Query("SELECT COALESCE(SUM(o.totalAmount), 0) FROM OrderEntity o WHERE o.orderDateTime BETWEEN :startDate AND :endDate")
    BigDecimal getSalesByDateRange(@Param("startDate") String startDate,
                                   @Param("endDate") String endDate);

    // Additional utility methods
    @Query("SELECT o FROM OrderEntity o WHERE o.orderDateTime LIKE %:date%")
    List<OrderEntity> findByOrderDateTimeContaining(@Param("date") String date);

    List<OrderEntity> findByOrderType(String orderType);
    List<OrderEntity> findByPaymentMethod(String paymentMethod);

    @Query("SELECT o FROM OrderEntity o WHERE o.orderStatus = :status AND o.orderDateTime BETWEEN :startDate AND :endDate")
    List<OrderEntity> findByStatusAndDateRange(@Param("status") String status,
                                               @Param("startDate") String startDate,
                                               @Param("endDate") String endDate);




    //========================================================
    //                  OLD version Methods                  //
    //========================================================
//    List<OrderEntity> findByUser_UserId(Long userId);
//    List<OrderEntity> findByOrderStatus(String orderStatus);
//
//    @Query("SELECT SUM(o.totalAmount) FROM OrderEntity o")
//    Double getTotalSales();
//
//    @Query("SELECT COUNT(o) FROM OrderEntity o WHERE o.orderStatus = :status")
//    Long countOrdersByStatus(String status);
//
//    @Query("SELECT COUNT(o) FROM OrderEntity o WHERE o.paymentMethod = :method")
//    Long countOrdersByPaymentMethod(String method);
//
//
//     // ======= new method added ==========//
//    @Query("SELECT COUNT(o) FROM OrderEntity o WHERE o.user.userId = :userId")
//    long countOrdersByUserId(@Param("userId") Long userId);
//
//    @Query("SELECT SUM(o.totalAmount) FROM OrderEntity o WHERE o.user.userId = :userId")
//    BigDecimal sumSpendsByUserId(@Param("userId") Long userId);


}