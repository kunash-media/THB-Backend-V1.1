package com.thb.bakery.service;

import com.thb.bakery.dto.request.POSOrderRequestDTO;
import com.thb.bakery.dto.response.POSOrderResponseDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface POSService {
    /**
     * Creates a new POS order
     * @param requestDTO Order details
     * @return Created order response
     */
    POSOrderResponseDTO createOrder(POSOrderRequestDTO requestDTO);

    /**
     * Retrieves all POS orders with pagination
     * @param pageable Pagination information (page number, size)
     * @return List of orders for the requested page
     */
    List<POSOrderResponseDTO> getAllOrders(Pageable pageable);

    /**
     * Retrieves a POS order by its ID
     * @param orderId Order ID
     * @return Order response
     */
    POSOrderResponseDTO getOrderById(String orderId);

    /**
     * Updates an existing POS order
     * @param orderId Order ID
     * @param requestDTO Updated order details
     * @return Updated order response
     */
    POSOrderResponseDTO updateOrder(Long orderId, POSOrderRequestDTO requestDTO);

    /**
     * Partially updates a POS order
     * @param orderId Order ID
     * @param updates Map of fields to update
     * @return Updated order response
     */
    POSOrderResponseDTO patchOrder(Long orderId, Map<String, Object> updates);

    /**
     * Deletes a POS order
     * @param orderId Order ID
     */
    void deleteOrder(Long orderId);

    /**
     * Retrieves orders by customer name
     * @param customerName Customer name
     * @return List of orders for the customer
     */
    List<POSOrderResponseDTO> getOrdersByCustomerName(String customerName);
}