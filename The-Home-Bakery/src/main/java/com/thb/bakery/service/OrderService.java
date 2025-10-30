package com.thb.bakery.service;

import com.thb.bakery.dto.request.CreateOrderRequest;
import com.thb.bakery.dto.request.UpdateOrderRequest;
import com.thb.bakery.dto.response.OrderResponse;


import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface OrderService {

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