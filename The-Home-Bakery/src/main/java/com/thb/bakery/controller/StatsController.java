
package com.thb.bakery.controller;

import com.thb.bakery.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/stats")
public class StatsController {

    private static final Logger logger = LoggerFactory.getLogger(StatsController.class);
    private final OrderService orderService;

    public StatsController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/total-sales")
    public ResponseEntity<Map<String, Object>> getTotalSales() {
        try {
            logger.info("Fetching total sales");
            Map<String, Object> stats = orderService.getOrderStatistics();

            // Extract total sales from statistics
            BigDecimal totalSales = (BigDecimal) stats.getOrDefault("allTimeSales", BigDecimal.ZERO);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("totalSales", totalSales);
            response.put("currency", "INR");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error fetching total sales", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", "Failed to fetch total sales: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    // Add these endpoints to your existing StatsController

    @GetMapping("/sales-trend")
    public ResponseEntity<Map<String, Object>> getSalesTrendData() {
        try {
            logger.info("Fetching sales trend data");
            Map<String, Object> trendData = orderService.getSalesTrendData();
            return ResponseEntity.ok(trendData);
        } catch (Exception e) {
            logger.error("Error fetching sales trend data", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", "Failed to fetch sales trend data: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/category-distribution")
    public ResponseEntity<Map<String, Object>> getProductCategoryDistribution() {
        try {
            logger.info("Fetching product category distribution");
            Map<String, Object> distributionData = orderService.getProductCategoryDistribution();
            return ResponseEntity.ok(distributionData);
        } catch (Exception e) {
            logger.error("Error fetching product category distribution", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", "Failed to fetch category distribution: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/total-orders")
    public ResponseEntity<Map<String, Object>> getTotalOrders() {
        try {
            logger.info("Fetching total orders");
            Map<String, Object> stats = orderService.getOrderStatistics();

            // Extract total orders from statistics
            Long totalOrders = (Long) stats.getOrDefault("totalOrders", 0L);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("totalOrders", totalOrders);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error fetching total orders", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", "Failed to fetch total orders: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/average-order-value")
    public ResponseEntity<Map<String, Object>> getAverageOrderValue() {
        try {
            logger.info("Fetching average order value");
            Map<String, Object> stats = orderService.getOrderStatistics();

            // Calculate average order value
            BigDecimal totalSales = (BigDecimal) stats.getOrDefault("allTimeSales", BigDecimal.ZERO);
            Long totalOrders = (Long) stats.getOrDefault("totalOrders", 0L);

            BigDecimal averageOrderValue = totalOrders > 0 ?
                    totalSales.divide(BigDecimal.valueOf(totalOrders), 2, RoundingMode.HALF_UP) :
                    BigDecimal.ZERO;

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("averageOrderValue", averageOrderValue);
            response.put("currency", "INR");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error fetching average order value", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", "Failed to fetch average order value: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboardStats() {
        try {
            logger.info("Fetching all dashboard stats");
            Map<String, Object> stats = orderService.getOrderStatistics();

            // Extract and calculate all required metrics
            BigDecimal totalSales = (BigDecimal) stats.getOrDefault("allTimeSales", BigDecimal.ZERO);
            Long totalOrders = (Long) stats.getOrDefault("totalOrders", 0L);
            BigDecimal averageOrderValue = totalOrders > 0 ?
                    totalSales.divide(BigDecimal.valueOf(totalOrders), 2, RoundingMode.HALF_UP) :
                    BigDecimal.ZERO;

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("totalSales", totalSales);
            response.put("totalOrders", totalOrders);
            response.put("averageOrderValue", averageOrderValue);
            response.put("currency", "INR");

            // Include additional stats if available
            if (stats.containsKey("orderStatus")) {
                response.put("orderStatus", stats.get("orderStatus"));
            }
            if (stats.containsKey("paymentMethods")) {
                response.put("paymentMethods", stats.get("paymentMethods"));
            }

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error fetching dashboard stats", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", "Failed to fetch dashboard stats: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
