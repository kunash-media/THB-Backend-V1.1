package com.thb.bakery.controller;


import com.thb.bakery.dto.request.POSOrderRequestDTO;
import com.thb.bakery.dto.response.POSOrderResponseDTO;
import com.thb.bakery.service.POSService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/pos")
public class POSController {

    private static final Logger logger = LoggerFactory.getLogger(POSController.class);

    @Autowired
    private POSService posService;

    /**
     * Create a new POS order
     * POST /api/pos
     * @param requestDTO - Order details
     * @return Created order response with HTTP 201 status
     */
    @PostMapping("/create-order")
    public ResponseEntity<POSOrderResponseDTO> createOrder(@RequestBody POSOrderRequestDTO requestDTO) {
        logger.info("POST /api/pos - Creating new order for customer: {}", requestDTO.getCustomerName());

        POSOrderResponseDTO response = posService.createOrder(requestDTO);

        logger.info("Order created successfully with ID: {}", response.getOrderId());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Get all POS orders with pagination
     * GET /api/pos/get-all-orders?page={page}&size={size}
     * @param page Page number (default 0)
     * @param size Page size (default 10)
     * @return List of orders for the requested page with HTTP 200 status
     */
    @GetMapping("/get-all-orders")
    public ResponseEntity<List<POSOrderResponseDTO>> getAllOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        logger.info("GET /api/pos/get-all-orders - Fetching orders with page={}, size={}", page, size);

        Pageable pageable = PageRequest.of(page, size);
        List<POSOrderResponseDTO> orders = posService.getAllOrders(pageable);

        logger.info("Returning {} orders for page {}", orders.size(), page);
        return ResponseEntity.ok(orders);
    }

    /**
     * Get POS order by ID
     * GET /api/pos/{orderId}
     * @param orderId - Order ID
     * @return Order details with HTTP 200 status
     */
    @GetMapping("/get-order-by-orderId/{orderId}")
    public ResponseEntity<POSOrderResponseDTO> getOrderById(@PathVariable String orderId) {
        logger.info("GET /api/pos/{} - Fetching order by ID", orderId);

        POSOrderResponseDTO order = posService.getOrderById(orderId);

        logger.info("Returning order with ID: {}", orderId);
        return ResponseEntity.ok(order);
    }

    /**
     * Update POS order completely
     * PUT /api/pos/{orderId}
     * @param orderId - Order ID
     * @param requestDTO - Updated order details
     * @return Updated order response with HTTP 200 status
     */
    @PutMapping("/{orderId}")
    public ResponseEntity<POSOrderResponseDTO> updateOrder(@PathVariable Long orderId,
                                                           @RequestBody POSOrderRequestDTO requestDTO) {
        logger.info("PUT /api/pos/{} - Updating order completely", orderId);

        POSOrderResponseDTO response = posService.updateOrder(orderId, requestDTO);

        logger.info("Order updated successfully with ID: {}", orderId);
        return ResponseEntity.ok(response);
    }

    /**
     * Patch POS order (partial update)
     * PATCH /api/pos/{orderId}
     * @param orderId - Order ID
     * @param updates - Map of fields to update
     * @return Updated order response with HTTP 200 status
     */
    @PatchMapping("/{orderId}")
    public ResponseEntity<POSOrderResponseDTO> patchOrder(@PathVariable Long orderId,
                                                          @RequestBody Map<String, Object> updates) {
        logger.info("PATCH /api/pos/{} - Patching order with fields: {}", orderId, updates.keySet());

        POSOrderResponseDTO response = posService.patchOrder(orderId, updates);

        logger.info("Order patched successfully with ID: {}", orderId);
        return ResponseEntity.ok(response);
    }

    /**
     * Delete POS order
     * DELETE /api/pos/{orderId}
     * @param orderId - Order ID to delete
     * @return HTTP 204 No Content status
     */
    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long orderId) {
        logger.info("DELETE /api/pos/{} - Deleting order", orderId);

        posService.deleteOrder(orderId);

        logger.info("Order deleted successfully with ID: {}", orderId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get orders by customer name
     * GET /api/pos/customer/{customerName}
     * @param customerName - Customer name
     * @return List of orders for the customer with HTTP 200 status
     */
    @GetMapping("/customer/{customerName}")
    public ResponseEntity<List<POSOrderResponseDTO>> getOrdersByCustomerName(@PathVariable String customerName) {
        logger.info("GET /api/pos/customer/{} - Fetching orders by customer name", customerName);

        List<POSOrderResponseDTO> orders = posService.getOrdersByCustomerName(customerName);

        logger.info("Returning {} orders for customer: {}", orders.size(), customerName);
        return ResponseEntity.ok(orders);
    }
}