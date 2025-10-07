package com.thb.bakery.service;


import com.thb.bakery.dto.request.PaymentRequest;
import com.thb.bakery.dto.request.PaymentVerificationRequest;
import com.thb.bakery.dto.response.PaymentResponse;
import com.thb.bakery.dto.response.PaymentVerificationResponse;
import com.thb.bakery.entity.PaymentOrder;

import java.util.List;

public interface PaymentService {

    /**
     * Create a new payment order
     */
    PaymentResponse createPaymentOrder(PaymentRequest request) throws Exception;

    /**
     * Verify payment signature
     */
    PaymentVerificationResponse verifyPayment(PaymentVerificationRequest request) throws Exception;

    /**
     * Get payment order by Razorpay order ID
     */
    PaymentOrder getPaymentOrderByRazorpayId(String razorpayOrderId);

    /**
     * Get all payment orders
     */
    List<PaymentOrder> getAllPaymentOrders();

    /**
     * Update payment status
     */
    PaymentOrder updatePaymentStatus(String razorpayOrderId, String status);

    // New methods for user-based operations
    List<PaymentOrder> getPaymentOrdersByUserId(Long userId);

    PaymentOrder getPaymentOrderByRazorpayIdAndUserId(String razorpayOrderId, Long userId);

    List<PaymentOrder> getPaymentOrdersByUserIdAndStatus(Long userId, String status);
}