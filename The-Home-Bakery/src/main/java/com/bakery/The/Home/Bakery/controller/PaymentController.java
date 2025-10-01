package com.bakery.The.Home.Bakery.controller;

import com.bakery.The.Home.Bakery.dto.request.PaymentRequest;
import com.bakery.The.Home.Bakery.dto.request.PaymentVerificationRequest;
import com.bakery.The.Home.Bakery.dto.response.PaymentResponse;
import com.bakery.The.Home.Bakery.dto.response.PaymentVerificationResponse;
import com.bakery.The.Home.Bakery.entity.PaymentOrder;
import com.bakery.The.Home.Bakery.service.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    @Autowired
    private PaymentService paymentService;

    /**
     * Create a new payment order for a specific user
     */
    @PostMapping("/create-order")
    public ResponseEntity<?> createPaymentOrder(@RequestBody PaymentRequest request) {
        try {
            logger.info("Creating payment order for userId: {} with amount: {}",
                    request.getUserId(), request.getAmount());
            PaymentResponse response = paymentService.createPaymentOrder(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error creating payment order: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating payment order: " + e.getMessage());
        }
    }

    /**
     * Verify payment after successful payment
     */
    @PostMapping("/verify")
    public ResponseEntity<?> verifyPayment(@RequestBody PaymentVerificationRequest request) {
        try {
            logger.info("Verifying payment for order: {}", request.getRazorpayOrderId());
            PaymentVerificationResponse response = paymentService.verifyPayment(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error verifying payment: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error verifying payment: " + e.getMessage());
        }
    }

    /**
     * Get payment order by Razorpay order ID (existing functionality)
     */
    @GetMapping("/order/{razorpayOrderId}")
    public ResponseEntity<?> getPaymentOrder(@PathVariable String razorpayOrderId) {
        try {
            logger.info("Fetching payment order by Razorpay ID: {}", razorpayOrderId);
            PaymentOrder paymentOrder = paymentService.getPaymentOrderByRazorpayId(razorpayOrderId);
            return ResponseEntity.ok(paymentOrder);
        } catch (Exception e) {
            logger.error("Error fetching payment order: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Payment order not found: " + e.getMessage());
        }
    }

    /**
     * Get payment order by Razorpay order ID for a specific user
     */
    @GetMapping("/user/{userId}/order/{razorpayOrderId}")
    public ResponseEntity<?> getPaymentOrderByUser(
            @PathVariable Long userId,
            @PathVariable String razorpayOrderId) {
        try {
            logger.info("Fetching payment order: {} for userId: {}", razorpayOrderId, userId);
            PaymentOrder paymentOrder = paymentService.getPaymentOrderByRazorpayIdAndUserId(razorpayOrderId, userId);
            return ResponseEntity.ok(paymentOrder);
        } catch (Exception e) {
            logger.error("Error fetching payment order for user: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Payment order not found: " + e.getMessage());
        }
    }

    /**
     * Get all payment orders for a specific user
     */
    @GetMapping("/user/{userId}/orders")
    public ResponseEntity<?> getPaymentOrdersByUser(@PathVariable Long userId) {
        try {
            logger.info("Fetching all payment orders for userId: {}", userId);
            List<PaymentOrder> paymentOrders = paymentService.getPaymentOrdersByUserId(userId);
            return ResponseEntity.ok(paymentOrders);
        } catch (Exception e) {
            logger.error("Error fetching payment orders for user: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error fetching payment orders: " + e.getMessage());
        }
    }

    /**
     * Get all payment orders for a specific user by status
     */
    @GetMapping("/user/{userId}/orders/status/{status}")
    public ResponseEntity<?> getPaymentOrdersByUserAndStatus(
            @PathVariable Long userId,
            @PathVariable String status) {
        try {
            logger.info("Fetching payment orders for userId: {} with status: {}", userId, status);
            List<PaymentOrder> paymentOrders = paymentService.getPaymentOrdersByUserIdAndStatus(userId, status);
            return ResponseEntity.ok(paymentOrders);
        } catch (Exception e) {
            logger.error("Error fetching payment orders for user by status: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error fetching payment orders: " + e.getMessage());
        }
    }

    /**
     * Get all payment orders (admin functionality)
     */
    @GetMapping("/orders")
    public ResponseEntity<?> getAllPaymentOrders() {
        try {
            logger.info("Fetching all payment orders");
            List<PaymentOrder> paymentOrders = paymentService.getAllPaymentOrders();
            return ResponseEntity.ok(paymentOrders);
        } catch (Exception e) {
            logger.error("Error fetching all payment orders: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching payment orders: " + e.getMessage());
        }
    }

    /**
     * Update payment status by Razorpay order ID
     */
    @PutMapping("/order/{razorpayOrderId}/status/{status}")
    public ResponseEntity<?> updatePaymentStatus(
            @PathVariable String razorpayOrderId,
            @PathVariable String status) {
        try {
            logger.info("Updating payment status for order: {} to status: {}", razorpayOrderId, status);
            PaymentOrder updatedOrder = paymentService.updatePaymentStatus(razorpayOrderId, status);
            return ResponseEntity.ok(updatedOrder);
        } catch (Exception e) {
            logger.error("Error updating payment status: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error updating payment status: " + e.getMessage());
        }
    }
}