package com.thb.bakery.service;

import com.thb.bakery.dto.request.CreateOrderRequest;
import com.thb.bakery.dto.request.UpdateOrderRequest;
import com.thb.bakery.dto.response.OrderResponse;


import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface OrderService {

    //NEW for dashboard--
    Map<String, Object> getSalesTrendData();

    //NEW for dashboard--
    Map<String, Object> getProductCategoryDistribution();


    // below 3 fields are newly added fields;;
    BigDecimal getTotalSales();
    Long getTotalOrdersCount();
    Map<String, Object> getSalesStatistics();
    //----------

    // Core CRUD operations
    OrderResponse createOrder(CreateOrderRequest request);

    OrderResponse getOrderById(Long orderId);

    List<OrderResponse> getAllOrders();

    OrderResponse updateOrder(Long orderId, UpdateOrderRequest request);

    void deleteOrder(Long orderId);

    List<OrderResponse> getOrdersByUserId(Long userId);

    List<OrderResponse> getOrdersByStatus(String orderStatus);

//    List<OrderResponse> getOrdersByDateRange(LocalDate startDate, LocalDate endDate);

    OrderResponse updateOrderStatus(Long orderId, String status);

    Map<String, Object> getOrderStatistics();

    Map<String, String> cancelOrder(Long orderId);
}