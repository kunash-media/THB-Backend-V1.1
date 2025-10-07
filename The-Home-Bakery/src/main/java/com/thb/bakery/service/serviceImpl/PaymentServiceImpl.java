package com.thb.bakery.service.serviceImpl;

import com.thb.bakery.Enum.PaymentStatus;
import com.thb.bakery.dto.request.PaymentRequest;
import com.thb.bakery.dto.request.PaymentVerificationRequest;
import com.thb.bakery.dto.response.PaymentResponse;
import com.thb.bakery.dto.response.PaymentVerificationResponse;
import com.thb.bakery.entity.PaymentOrder;
import com.thb.bakery.entity.UserEntity;
import com.thb.bakery.repository.PaymentOrderRepository;
import com.thb.bakery.repository.UserRepository;
import com.thb.bakery.service.PaymentService;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);

    @Autowired
    private PaymentOrderRepository paymentOrderRepository;

    @Autowired
    private UserRepository userRepository;

    @Value("${razorpay.key_id:}")
    private String razorpayKeyId;

    @Value("${razorpay.key_secret:}")
    private String razorpayKeySecret;

    private RazorpayClient razorpayClient;

    // Initialize Razorpay client
    private RazorpayClient getRazorpayClient() throws RazorpayException {
        if (razorpayClient == null) {
            razorpayClient = new RazorpayClient(razorpayKeyId, razorpayKeySecret);
        }
        return razorpayClient;
    }

    @Override
    public PaymentResponse createPaymentOrder(PaymentRequest request) throws Exception {
        logger.info("Creating payment order for user ID: {}", request.getUserId());

        try {
            // Validate userId and get user
            if (request.getUserId() == null) {
                logger.error("User ID is required but was null");
                throw new Exception("User ID is required");
            }

            UserEntity user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> {
                        logger.error("User not found with ID: {}", request.getUserId());
                        return new Exception("User not found with ID: " + request.getUserId());
                    });

            logger.debug("Found user: {} for payment order creation", user.getEmail());

            // Add logging to verify keys are loaded
            logger.info("Attempting to create Razorpay order for userId: {}", request.getUserId());
            logger.debug("Razorpay Key ID: {}", razorpayKeyId);
            logger.debug("Razorpay Key Secret present: {}", !StringUtils.isEmpty(razorpayKeySecret));

            if (!StringUtils.hasText(razorpayKeyId) || !StringUtils.hasText(razorpayKeySecret)) {
                logger.error("Razorpay keys are not properly configured!");
                throw new Exception("Razorpay credentials not configured");
            }

            // Convert amount to paise for Razorpay
            long amountInPaise = Math.round(request.getAmount() * 100);

            // Validate minimum amount (Razorpay minimum is typically 100 paise = ₹1)
            if(amountInPaise < 100) {
                logger.warn("Amount too low: {} paise (minimum 100 paise)", amountInPaise);
                throw new Exception("Amount must be at least ₹1");
            }

            logger.debug("Amount converted to paise: {} (original: {})", amountInPaise, request.getAmount());

            // Create Razorpay order
            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", amountInPaise);    // Convert to paise
            orderRequest.put("currency", request.getCurrency());
            orderRequest.put("receipt", request.getReceipt());

            // Add customer notes
            JSONObject notes = new JSONObject();
            notes.put("customer_name", request.getCustomerName());
            notes.put("customer_email", request.getCustomerEmail());
            notes.put("customer_phone", request.getCustomerPhone());
            notes.put("user_id", request.getUserId().toString());
            orderRequest.put("notes", notes);

            logger.debug("Creating Razorpay order with request: {}", orderRequest.toString());
            Order razorpayOrder = getRazorpayClient().orders.create(orderRequest);

            // Save to database
            PaymentOrder paymentOrder = new PaymentOrder();
            paymentOrder.setUser(user); // Set the user entity
            paymentOrder.setRazorpayOrderId(razorpayOrder.get("id"));
            paymentOrder.setAmount((int) amountInPaise); // Store amount in paise
            paymentOrder.setCurrency(request.getCurrency());
            paymentOrder.setReceipt(request.getReceipt());
            paymentOrder.setCustomerName(request.getCustomerName());
            paymentOrder.setCustomerEmail(request.getCustomerEmail());
            paymentOrder.setCustomerPhone(request.getCustomerPhone());
            paymentOrder.setStatus(PaymentStatus.PENDING);

            PaymentOrder savedPaymentOrder = paymentOrderRepository.save(paymentOrder);
            logger.debug("Payment order saved to database with ID: {}", savedPaymentOrder.getId());

            // Create response
            PaymentResponse response = new PaymentResponse();
            response.setRazorpayOrderId(razorpayOrder.get("id"));
            response.setAmount(request.getAmount());
            response.setCurrency(request.getCurrency());
            response.setReceipt(request.getReceipt());
            response.setStatus(razorpayOrder.get("status"));
            response.setCustomerName(request.getCustomerName());
            response.setCustomerEmail(request.getCustomerEmail());
            response.setCustomerPhone(request.getCustomerPhone());
            response.setRazorpayKeyId(razorpayKeyId);

            logger.info("Payment order created successfully for userId: {} with orderId: {}",
                    request.getUserId(), razorpayOrder.get("id"));
            return response;

        } catch (RazorpayException e) {
            logger.error("Error creating Razorpay order for user {}: {}",
                    request.getUserId(), e.getMessage(), e);
            throw new Exception("Failed to create payment order: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error creating payment order for user {}: {}",
                    request.getUserId(), e.getMessage(), e);
            throw new Exception("Failed to create payment order: " + e.getMessage());
        }
    }

    @Override
    public PaymentVerificationResponse verifyPayment(PaymentVerificationRequest request) throws Exception {
        logger.info("Verifying payment for order: {}", request.getRazorpayOrderId());

        try {
            // Get payment order from database
            PaymentOrder paymentOrder = paymentOrderRepository
                    .findByRazorpayOrderId(request.getRazorpayOrderId())
                    .orElseThrow(() -> {
                        logger.error("Payment order not found: {}", request.getRazorpayOrderId());
                        return new Exception("Payment order not found");
                    });

            logger.debug("Found payment order for verification: {}", paymentOrder.getRazorpayOrderId());

            // Verify signature
            boolean isValidSignature = verifyRazorpaySignature(
                    request.getRazorpayOrderId(),
                    request.getRazorpayPaymentId(),
                    request.getRazorpaySignature()
            );

            PaymentVerificationResponse response = new PaymentVerificationResponse();

            if (isValidSignature) {
                // Update payment order
                paymentOrder.setRazorpayPaymentId(request.getRazorpayPaymentId());
                paymentOrder.setRazorpaySignature(request.getRazorpaySignature());
                paymentOrder.setStatus(PaymentStatus.PAID);
                paymentOrderRepository.save(paymentOrder);

                response.setSuccess(true);
                response.setMessage("Payment verified successfully");
                response.setStatus("PAID");

                logger.info("Payment verified successfully for userId: {} with paymentId: {}",
                        paymentOrder.getUser().getUserId(), request.getRazorpayPaymentId());
            } else {
                paymentOrder.setStatus(PaymentStatus.FAILED);
                paymentOrderRepository.save(paymentOrder);

                response.setSuccess(false);
                response.setMessage("Payment verification failed");
                response.setStatus("FAILED");

                logger.error("Payment verification failed for order: {}", request.getRazorpayOrderId());
            }

            response.setPaymentId(request.getRazorpayPaymentId());
            response.setOrderId(request.getRazorpayOrderId());

            return response;

        } catch (Exception e) {
            logger.error("Error verifying payment for order {}: {}",
                    request.getRazorpayOrderId(), e.getMessage(), e);
            throw new Exception("Payment verification failed: " + e.getMessage());
        }
    }

    private boolean verifyRazorpaySignature(String orderId, String paymentId, String signature) {
        logger.debug("Verifying Razorpay signature for order: {}", orderId);

        try {
            String payload = orderId + "|" + paymentId;
            String expectedSignature = calculateHmacSha256(payload, razorpayKeySecret);
            boolean isValid = signature.equals(expectedSignature);

            logger.debug("Signature verification result for order {}: {}", orderId, isValid ? "VALID" : "INVALID");
            return isValid;

        } catch (Exception e) {
            logger.error("Error verifying signature for order {}: {}", orderId, e.getMessage(), e);
            return false;
        }
    }

    private String calculateHmacSha256(String data, String key) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "HmacSHA256");
        mac.init(secretKeySpec);
        byte[] hash = mac.doFinal(data.getBytes());

        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    @Override
    public PaymentOrder getPaymentOrderByRazorpayId(String razorpayOrderId) {
        logger.info("Fetching payment order by Razorpay ID: {}", razorpayOrderId);

        return paymentOrderRepository.findByRazorpayOrderId(razorpayOrderId)
                .orElseThrow(() -> {
                    logger.error("Payment order not found: {}", razorpayOrderId);
                    return new RuntimeException("Payment order not found: " + razorpayOrderId);
                });
    }

    @Override
    public List<PaymentOrder> getAllPaymentOrders() {
        logger.info("Fetching all payment orders");

        List<PaymentOrder> orders = paymentOrderRepository.findAll();
        logger.debug("Retrieved {} payment orders", orders.size());
        return orders;
    }

    @Override
    public PaymentOrder updatePaymentStatus(String razorpayOrderId, String status) {
        logger.info("Updating payment status for order: {} to: {}", razorpayOrderId, status);

        PaymentOrder paymentOrder = getPaymentOrderByRazorpayId(razorpayOrderId);

        try {
            PaymentStatus paymentStatus = PaymentStatus.valueOf(status.toUpperCase());
            paymentOrder.setStatus(paymentStatus);
            logger.debug("Payment status updated to: {}", paymentStatus);

        } catch (IllegalArgumentException e) {
            logger.error("Invalid payment status provided: {}", status);
            throw new RuntimeException("Invalid payment status: " + status);
        }

        PaymentOrder updatedOrder = paymentOrderRepository.save(paymentOrder);
        logger.info("Payment status updated successfully for order: {}", razorpayOrderId);

        return updatedOrder;
    }

    // New methods for user-based operations
    @Override
    public List<PaymentOrder> getPaymentOrdersByUserId(Long userId) {
        logger.info("Fetching payment orders for user ID: {}", userId);

        // Validate user exists
        userRepository.findById(userId)
                .orElseThrow(() -> {
                    logger.error("User not found with ID: {}", userId);
                    return new RuntimeException("User not found with ID: " + userId);
                });

        List<PaymentOrder> orders = paymentOrderRepository.findByUserUserIdOrderByCreatedAtDesc(userId);
        logger.debug("Retrieved {} payment orders for user ID: {}", orders.size(), userId);

        return orders;
    }

    @Override
    public PaymentOrder getPaymentOrderByRazorpayIdAndUserId(String razorpayOrderId, Long userId) {
        logger.info("Fetching payment order: {} for user ID: {}", razorpayOrderId, userId);

        // Validate user exists
        userRepository.findById(userId)
                .orElseThrow(() -> {
                    logger.error("User not found with ID: {}", userId);
                    return new RuntimeException("User not found with ID: " + userId);
                });

        PaymentOrder order = paymentOrderRepository.findByRazorpayOrderIdAndUserUserId(razorpayOrderId, userId)
                .orElseThrow(() -> {
                    logger.error("Payment order not found for user: {} with orderId: {}", userId, razorpayOrderId);
                    return new RuntimeException("Payment order not found for user: " + userId +
                            " with orderId: " + razorpayOrderId);
                });

        logger.debug("Found payment order for user");
        return order;
    }

    @Override
    public List<PaymentOrder> getPaymentOrdersByUserIdAndStatus(Long userId, String status) {
        logger.info("Fetching payment orders for user ID: {} with status: {}", userId, status);

        // Validate user exists
        userRepository.findById(userId)
                .orElseThrow(() -> {
                    logger.error("User not found with ID: {}", userId);
                    return new RuntimeException("User not found with ID: " + userId);
                });

        PaymentStatus paymentStatus;
        try {
            paymentStatus = PaymentStatus.valueOf(status.toUpperCase());
            logger.debug("Parsed payment status: {}", paymentStatus);

        } catch (IllegalArgumentException e) {
            logger.error("Invalid payment status provided: {}", status);
            throw new RuntimeException("Invalid payment status: " + status);
        }

        List<PaymentOrder> orders = paymentOrderRepository.findByUserUserIdAndStatus(userId, paymentStatus);
        logger.debug("Retrieved {} payment orders for user ID: {} with status: {}",
                orders.size(), userId, status);

        return orders;
    }
}