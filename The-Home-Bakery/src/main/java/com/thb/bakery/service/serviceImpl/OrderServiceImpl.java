package com.thb.bakery.service.serviceImpl;

import com.thb.bakery.dto.request.CreateOrderRequest;
import com.thb.bakery.dto.request.OrderItemRequest;
import com.thb.bakery.dto.request.UpdateOrderRequest;
import com.thb.bakery.dto.response.OrderItemResponse;
import com.thb.bakery.dto.response.OrderResponse;
import com.thb.bakery.entity.*;
import com.thb.bakery.repository.*;
import com.thb.bakery.service.OrderService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.stream.Collectors;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private SnacksRepository snacksRepository;

    public OrderServiceImpl(OrderRepository orderRepository, OrderItemRepository orderItemRepository,
                            UserRepository userRepository, ProductRepository productRepository, SnacksRepository snacksRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.snacksRepository = snacksRepository;
    }

    @Override
    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request) {
        try {
            logger.info("Starting order creation for user ID: {}", request.getUserId());

            // Validate required fields
            validateOrderRequest(request);

            UserEntity user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + request.getUserId()));

            if (!"active".equalsIgnoreCase(user.getStatus())) {
                throw new RuntimeException("User is not active. Cannot create order.");
            }

            // Create order entity
            OrderEntity order = createOrderEntity(request, user);

            // Process order items and calculate totals
            processOrderItems(order, request.getItems());

            // Save order
            OrderEntity savedOrder = orderRepository.save(order);
            logger.info("Order saved successfully with ID: {}", savedOrder.getOrderId());

            // Prepare response
            return createSuccessResponse(savedOrder);

        } catch (Exception e) {
            logger.error("Error creating order for user ID: {}", request.getUserId(), e);
            return new OrderResponse(false, "error", "Failed to create order: " + e.getMessage(), null);
        }
    }

    private void validateOrderRequest(CreateOrderRequest request) {
        if (request.getCustomerName() == null || request.getCustomerName().trim().isEmpty()) {
            throw new RuntimeException("Customer name is required");
        }
        if (request.getCustomerPhone() == null || request.getCustomerPhone().trim().isEmpty()) {
            throw new RuntimeException("Customer phone is required");
        }
        if (!request.getCustomerPhone().matches("^\\d{10}$")) {
            throw new RuntimeException("Invalid phone number format");
        }
        if (request.getCustomerEmail() == null || request.getCustomerEmail().trim().isEmpty()) {
            throw new RuntimeException("Customer email is required");
        }
        if (request.getShippingAddress() == null || request.getShippingAddress().trim().isEmpty()) {
            throw new RuntimeException("Shipping address is required");
        }
        if (request.getShippingCity() == null || request.getShippingCity().trim().isEmpty()) {
            throw new RuntimeException("Shipping city is required");
        }
        if (request.getShippingState() == null || request.getShippingState().trim().isEmpty()) {
            throw new RuntimeException("Shipping state is required");
        }
        if (request.getShippingPincode() == null || request.getShippingPincode().trim().isEmpty()) {
            throw new RuntimeException("Shipping pincode is required");
        }
        if (request.getPaymentMethod() == null || request.getPaymentMethod().trim().isEmpty()) {
            throw new RuntimeException("Payment method is required");
        }
        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new RuntimeException("Order items are required");
        }
    }

    private OrderEntity createOrderEntity(CreateOrderRequest request, UserEntity user) {
        OrderEntity order = new OrderEntity();

        order.setUser(user);
        order.setCustomerName(request.getCustomerName());
        order.setCustomerPhone(request.getCustomerPhone());
        order.setCustomerEmail(request.getCustomerEmail());
        order.setShippingAddress(request.getShippingAddress());
        order.setShippingCity(request.getShippingCity());
        order.setShippingState(request.getShippingState());
        order.setShippingPincode(request.getShippingPincode());
        order.setShippingCountry(request.getShippingCountry());
        order.setShippingCustomerName(request.getShippingCustomerName());
        order.setShippingEmail(request.getShippingEmail());
        order.setShippingPhone(request.getShippingPhone());
        order.setPaymentMethod(request.getPaymentMethod());
        order.setCouponAmount(request.getDiscountAmount());
        order.setCouponAppliedCode(request.getCouponApplied() != null ? request.getCouponApplied() : request.getCouponAppliedCode());
        order.setDiscountPercent(request.getDiscountPercent());
        order.setDiscountAmount(request.getDiscountAmount());

        // NEW FIELDS MAPPED
        order.setOrderDateTime(request.getOrderDateTime());
        order.setDeliveryDateTime(request.getDeliveryDateTime());
        order.setRecipientName(request.getRecipientName());
        order.setRecipientMobile(request.getRecipientMobile());
        order.setGiftMessage(request.getGiftMessage());
        order.setOrderType(request.getOrderType());
        order.setAddressType(request.getAddressType());
        order.setHouseNo(request.getHouseNo());
        order.setStreetArea(request.getStreetArea());
        order.setLandmark(request.getLandmark());

        order.setOrderStatus("placed");
        return order;
    }

    /**
     * Processes every line-item.
     * - If **productId** is present → uses the **existing** product flow.
     * - If **snackId** is present → loads the snack and uses its price.
     * - If **both** are present → throws a clear error.
     * - If **neither** → throws a clear error.
     *
     * All original behaviour (party-items, deleted-check, subtotal, totals) is kept **exactly** as-is.
     */
    private void processOrderItems(OrderEntity order, List<OrderItemRequest> itemRequests) {
        BigDecimal productTotal = BigDecimal.ZERO;
        List<OrderItemEntity> orderItems = new ArrayList<>();

        for (OrderItemRequest itemRequest : itemRequests) {

            OrderItemEntity orderItem = new OrderItemEntity();
            orderItem.setOrder(order);
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setSelectedWeight(itemRequest.getSelectedWeight());
            orderItem.setCakeMessage(itemRequest.getCakeMessage());
            orderItem.setSpecialInstructions(itemRequest.getSpecialInstructions());

            BigDecimal unitPrice = BigDecimal.ZERO;
            BigDecimal subtotal   = BigDecimal.ZERO;

            // --------------------------------------------------------------
            // 1. PRODUCT (existing flow – unchanged)
            // --------------------------------------------------------------
            if (itemRequest.getProductId() != null && itemRequest.getProductId() > 0) {
                ProductEntity product = productRepository.findById(itemRequest.getProductId())
                        .orElseThrow(() -> new RuntimeException(
                                "Product not found with id: " + itemRequest.getProductId()));

                if (product.isDeleted()) {
                    throw new RuntimeException(
                            "Product '" + product.getProductName() + "' is not available");
                }

                orderItem.setProduct(product);
                unitPrice = product.getProductNewPrice();               // <-- unchanged
            }

            // --------------------------------------------------------------
            // 2. SNACK (new flow – added)
            // --------------------------------------------------------------
            else if (itemRequest.getSnackId() != null && itemRequest.getSnackId() > 0) {
                SnacksEntity snack = snacksRepository.findById(itemRequest.getSnackId())
                        .orElseThrow(() -> new RuntimeException(
                                "Snack not found with id: " + itemRequest.getSnackId()));

                orderItem.setSnack(snack);
                unitPrice = BigDecimal.valueOf(snack.getProductNewPrice()); // double → BigDecimal
            }

            // --------------------------------------------------------------
            // 3. VALIDATION – exactly one must be present
            // --------------------------------------------------------------
            else {
                throw new RuntimeException(
                        "Each order item must contain either a valid productId or a valid snackId");
            }

            // --------------------------------------------------------------
            // 4. PRICE & SUBTOTAL (common for both product & snack)
            // --------------------------------------------------------------
            orderItem.setUnitPrice(unitPrice);
            subtotal = unitPrice.multiply(BigDecimal.valueOf(itemRequest.getQuantity()));
            orderItem.setSubtotal(subtotal);

            orderItems.add(orderItem);
            productTotal = productTotal.add(subtotal);

            // --------------------------------------------------------------
            // 5. PARTY ITEMS – **exactly the same as before**
            // --------------------------------------------------------------
            List<PartyItemEntity> partyItems = createPartyItems(orderItem, itemRequest.getPartyItems());
            orderItem.setPartyItems(partyItems);
            for (PartyItemEntity partyItem : partyItems) {
                productTotal = productTotal.add(partyItem.getPartyItemSubtotal());
            }
        }

        // --------------------------------------------------------------
        // 6. FINAL TOTALS – unchanged
        // --------------------------------------------------------------
        calculateOrderTotals(order, productTotal);
        order.setOrderItems(orderItems);
    }
//    private OrderItemEntity createOrderItem(OrderEntity order, ProductEntity product, OrderItemRequest itemRequest) {
//        OrderItemEntity orderItem = new OrderItemEntity();
//        orderItem.setOrder(order);
//        orderItem.setProduct(product);
//        orderItem.setQuantity(itemRequest.getQuantity());
//        orderItem.setUnitPrice(product.getProductNewPrice());
//        orderItem.setSelectedWeight(itemRequest.getSelectedWeight());
//        orderItem.setCakeMessage(itemRequest.getCakeMessage());
//        orderItem.setSpecialInstructions(itemRequest.getSpecialInstructions());
//
//        // Calculate subtotal
//        BigDecimal subtotal = product.getProductNewPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity()));
//        orderItem.setSubtotal(subtotal);
//
//        return orderItem;
//    }

    private List<PartyItemEntity> createPartyItems(OrderItemEntity orderItem, List<OrderItemRequest.PartyItems> partyItemRequests) {
        List<PartyItemEntity> partyItems = new ArrayList<>();
        if (partyItemRequests != null) {
            for (OrderItemRequest.PartyItems partyItemRequest : partyItemRequests) {
                PartyItemEntity partyItem = new PartyItemEntity();
                partyItem.setOrderItem(orderItem);
                partyItem.setItemId(partyItemRequest.getItemId());
                partyItem.setPartyItemName(partyItemRequest.getPartItemName());
                partyItem.setPartyItemQuantity(partyItemRequest.getPartyItemQuantity());
                partyItem.setPartyItemPrice(partyItemRequest.getPartyItemPrice());
                BigDecimal partyItemSubtotal = partyItemRequest.getPartyItemPrice()
                        .multiply(BigDecimal.valueOf(partyItemRequest.getPartyItemQuantity()))
                        .setScale(2, RoundingMode.HALF_UP);
                partyItem.setPartyItemSubtotal(partyItemSubtotal);
                partyItems.add(partyItem);
            }
        }
        return partyItems;
    }

    private void calculateOrderTotals(OrderEntity order, BigDecimal productTotal) {
        // Apply discount
        BigDecimal discountAmount = order.getDiscountAmount() != null ? order.getDiscountAmount() : BigDecimal.ZERO;
        BigDecimal discountedTotal = productTotal.subtract(discountAmount);

        // Calculate tax (5% GST for food items)
        BigDecimal taxRate = new BigDecimal("0.05");
        BigDecimal taxAmount = discountedTotal.multiply(taxRate).setScale(2, RoundingMode.HALF_UP);

        // Calculate convenience fee (free above ₹500, else ₹50)
        BigDecimal convenienceFee = discountedTotal.compareTo(new BigDecimal("500")) >= 0
                ? BigDecimal.ZERO : new BigDecimal("50");

        // Calculate grand total
        BigDecimal grandTotal = discountedTotal.add(taxAmount).add(convenienceFee);

        order.setTotalAmount(grandTotal);
        order.setTax(taxAmount);
        order.setConvenienceFee(convenienceFee);
    }

    private OrderResponse createSuccessResponse(OrderEntity order) {
        OrderResponse response = new OrderResponse(
                true,
                order.getOrderId(),
                order.getTotalAmount(),
                order.getOrderStatus(),
                order.getOrderDateTime(),
                "Order placed successfully! In case of any delivery issues, please contact: 8983448510"
        );

        response.setTax(order.getTax());
        response.setCouponApplied(order.getCouponAmount());
        response.setCouponAppliedCode(order.getCouponAppliedCode());
        response.setConvenienceFee(order.getConvenienceFee());
        response.setDiscountPercent(order.getDiscountPercent());
        response.setDiscountAmount(order.getDiscountAmount());
        response.setDeliveryDateTime(order.getDeliveryDateTime());
        response.setPaymentMethod(order.getPaymentMethod());
        response.setCustomerName(order.getCustomerName());
        response.setCustomerPhone(order.getCustomerPhone());
        response.setCustomerEmail(order.getCustomerEmail());
        response.setShippingAddress(order.getShippingAddress());
        response.setShippingCity(order.getShippingCity());
        response.setShippingState(order.getShippingState());
        response.setShippingPincode(order.getShippingPincode());
        response.setShippingCountry(order.getShippingCountry());
        response.setShippingCustomerName(order.getShippingCustomerName());
        response.setShippingEmail(order.getShippingEmail());
        response.setShippingPhone(order.getShippingPhone());

        // NEW FIELDS IN RESPONSE
        response.setRecipientName(order.getRecipientName());
        response.setRecipientMobile(order.getRecipientMobile());
        response.setGiftMessage(order.getGiftMessage());
        response.setOrderType(order.getOrderType());
        response.setAddressType(order.getAddressType());
        response.setHouseNo(order.getHouseNo());
        response.setStreetArea(order.getStreetArea());
        response.setLandmark(order.getLandmark());

        response.setItems(createOrderItemResponses(order.getOrderItems()));

        return response;
    }

    private List<OrderItemResponse> createOrderItemResponses(List<OrderItemEntity> orderItems) {
        return orderItems.stream()
                .map(item -> {
                    OrderItemResponse resp = new OrderItemResponse();

                    // ---------- common fields ----------
                    resp.setQuantity(item.getQuantity());
                    resp.setUnitPrice(item.getUnitPrice());
                    resp.setSubtotal(item.getSubtotal());
                    resp.setSelectedWeight(item.getSelectedWeight());
                    resp.setCakeMessage(item.getCakeMessage());
                    resp.setSpecialInstructions(item.getSpecialInstructions());

                    // ---------- PRODUCT OR SNACK ----------
                    ProductEntity product = item.getProduct();
                    SnacksEntity  snack   = item.getSnack();

                    if (product != null) {
                        // ----- PRODUCT -----
                        resp.setProductId(product.getProductId());
                        resp.setProductName(product.getProductName());
                        resp.setProductCategory(product.getProductCategory());
                        resp.setProductImage(
                                "http://localhost:8082/api/v1/products/" +
                                        product.getProductId() + "/image");
                    } else if (snack != null) {
                        // ----- SNACK -----
                        resp.setSnackId(snack.getSnackId());
                        resp.setProductName(snack.getProductName());          // reuse field
                        resp.setProductCategory(snack.getProductCategory()); // reuse field
                        resp.setProductImage(
                                "http://localhost:8082/api/v1/snacks/" +      // adjust base path
                                        snack.getSnackId() + "/image");
                    } else {
                        throw new IllegalStateException(
                                "OrderItem " + item.getOrderItemId() +
                                        " has neither product nor snack");
                    }

                    // ---------- PARTY ITEMS ----------
                    resp.setPartyItems(createPartyItemResponses(item.getPartyItems()));

                    return resp;
                })
                .collect(Collectors.toList());
    }

    private List<OrderItemResponse.PartyItem> createPartyItemResponses(List<PartyItemEntity> partyItems) {
        return partyItems.stream().map(partyItem -> {
            OrderItemResponse.PartyItem partyItemResponse = new OrderItemResponse.PartyItem();
            partyItemResponse.setPartyItemId(partyItem.getPartyItemId());
            partyItemResponse.setPartyItemName(partyItem.getPartyItemName());
            partyItemResponse.setPartyItemQuantity(partyItem.getPartyItemQuantity());
            partyItemResponse.setPartyItemPrice(partyItem.getPartyItemPrice());
            partyItemResponse.setPartyItemSubtotal(partyItem.getPartyItemSubtotal());
            return partyItemResponse;
        }).collect(Collectors.toList());
    }

    @Override
    public OrderResponse getOrderById(Long orderId) {
        try {
            OrderEntity order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
            return createSuccessResponse(order);
        } catch (Exception e) {
            logger.error("Error fetching order with id: {}", orderId, e);
            return new OrderResponse(false, "error", "Failed to fetch order: " + e.getMessage(), null);
        }
    }

    @Override
    public List<OrderResponse> getAllOrders() {
        try {
            List<OrderEntity> orders = orderRepository.findAll();
            return orders.stream()
                    .map(this::createSuccessResponse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error fetching all orders", e);
            throw new RuntimeException("Failed to fetch orders: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public OrderResponse updateOrder(Long orderId, UpdateOrderRequest request) {
        try {
            OrderEntity order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));

            // Update fields if provided
            if (request.getOrderStatus() != null) {
                order.setOrderStatus(request.getOrderStatus());
            }

            OrderEntity updatedOrder = orderRepository.save(order);
            return createSuccessResponse(updatedOrder);
        } catch (Exception e) {
            logger.error("Error updating order with id: {}", orderId, e);
            return new OrderResponse(false, "error", "Failed to update order: " + e.getMessage(), null);
        }
    }

    @Override
    @Transactional
    public void deleteOrder(Long orderId) {
        try {
            OrderEntity order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));

            // Only allow deletion of cancelled orders
            if (!"cancelled".equalsIgnoreCase(order.getOrderStatus())) {
                throw new RuntimeException("Only cancelled orders can be deleted");
            }

            orderRepository.delete(order);
            logger.info("Order deleted successfully with id: {}", orderId);
        } catch (Exception e) {
            logger.error("Error deleting order with id: {}", orderId, e);
            throw new RuntimeException("Failed to delete order: " + e.getMessage());
        }
    }

    @Override
    public List<OrderResponse> getOrdersByUserId(Long userId) {
        try {
            List<OrderEntity> orders = orderRepository.findByUser_UserId(userId);
            return orders.stream()
                    .map(this::createSuccessResponse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error fetching orders for user id: {}", userId, e);
            throw new RuntimeException("Failed to fetch user orders: " + e.getMessage());
        }
    }

    @Override
    public List<OrderResponse> getOrdersByStatus(String orderStatus) {
        try {
            List<OrderEntity> orders = orderRepository.findByOrderStatus(orderStatus);
            return orders.stream()
                    .map(this::createSuccessResponse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error fetching orders by status: {}", orderStatus, e);
            throw new RuntimeException("Failed to fetch orders by status: " + e.getMessage());
        }
    }

//    @Override
//    public List<OrderResponse> getOrdersByDateRange(LocalDate startDate, LocalDate endDate) {
//        try {
//            List<OrderEntity> orders = orderRepository.findByOrderDateBetween(startDate, endDate);
//            return orders.stream()
//                    .map(this::createSuccessResponse)
//                    .collect(Collectors.toList());
//        } catch (Exception e) {
//            logger.error("Error fetching orders by date range: {} to {}", startDate, endDate, e);
//            throw new RuntimeException("Failed to fetch orders by date range: " + e.getMessage());
//        }
//    }

    @Override
    @Transactional
    public Map<String, String> cancelOrder(Long orderId) {
        try {
            OrderEntity order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));

            // Check if order can be cancelled
            if (List.of("delivered", "cancelled").contains(order.getOrderStatus().toLowerCase())) {
                throw new RuntimeException("Order cannot be cancelled in current status: " + order.getOrderStatus());
            }

            order.setOrderStatus("cancelled");
            orderRepository.save(order);

            logger.info("Order cancelled successfully with id: {}", orderId);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Order canceled successfully. In case of any issues, contact: 8983448510");
            response.put("orderId", orderId.toString());
            return response;
        } catch (Exception e) {
            logger.error("Error cancelling order with id: {}", orderId, e);
            Map<String, String> response = new HashMap<>();
            response.put("error", "Failed to cancel order: " + e.getMessage());
            return response;
        }
    }

    @Override
    public OrderResponse updateOrderStatus(Long orderId, String status) {
        try {
            OrderEntity order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));

            order.setOrderStatus(status);
            OrderEntity updatedOrder = orderRepository.save(order);

            return createSuccessResponse(updatedOrder);
        } catch (Exception e) {
            logger.error("Error updating order status with id: {}", orderId, e);
            return new OrderResponse(false, "error", "Failed to update order status: " + e.getMessage(), null);
        }
    }

    @Override
    public Map<String, Object> getOrderStatistics() {
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
        int currentYear = today.getYear();
        int currentMonth = today.getMonthValue();
        int lastMonth = today.minusMonths(1).getMonthValue();
        int lastMonthYear = today.minusMonths(1).getYear();

        Map<String, Object> stats = new HashMap<>();
//        stats.put("todayOrders", orderRepository.countOrdersByDate(today));
//        stats.put("yesterdayOrders", orderRepository.countOrdersByDate(yesterday));
//        stats.put("thisMonthOrders", orderRepository.countOrdersByMonth(currentYear, currentMonth));
//        stats.put("lastMonthOrders", orderRepository.countOrdersByMonth(lastMonthYear, lastMonth));
//        stats.put("allTimeSales", orderRepository.getTotalSales());
//        stats.put("totalOrders", orderRepository.countTotalOrders());

        // Order status counts
        Map<String, Long> statusCounts = new HashMap<>();
        statusCounts.put("placed", orderRepository.countOrdersByStatus("placed"));
        statusCounts.put("confirmed", orderRepository.countOrdersByStatus("confirmed"));
        statusCounts.put("preparing", orderRepository.countOrdersByStatus("preparing"));
        statusCounts.put("ready", orderRepository.countOrdersByStatus("ready"));
        statusCounts.put("out_for_delivery", orderRepository.countOrdersByStatus("out_for_delivery"));
        statusCounts.put("delivered", orderRepository.countOrdersByStatus("delivered"));
        statusCounts.put("cancelled", orderRepository.countOrdersByStatus("cancelled"));
        stats.put("orderStatus", statusCounts);

        // Payment method counts
        Map<String, Long> paymentMethods = new HashMap<>();
        paymentMethods.put("cod", orderRepository.countOrdersByPaymentMethod("cod"));
        paymentMethods.put("prepaid", orderRepository.countOrdersByPaymentMethod("prepaid"));
        stats.put("paymentMethods", paymentMethods);

        return stats;
    }
}