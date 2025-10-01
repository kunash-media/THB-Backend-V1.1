package com.bakery.The.Home.Bakery.controller;

import com.bakery.The.Home.Bakery.dto.request.ShippingAddressDTO;
import com.bakery.The.Home.Bakery.service.ShippingAddressService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
public class ShippingAddressController {

    private static final Logger logger = LoggerFactory.getLogger(ShippingAddressController.class);
    private final ShippingAddressService service;

    public ShippingAddressController(ShippingAddressService service) {
        this.service = service;
    }

    @PostMapping("/create-address/{userId}")
    public ResponseEntity<ShippingAddressDTO> createAddress(
            @PathVariable Long userId,
            @RequestBody ShippingAddressDTO addressDTO) {
        logger.info("Received request to create shipping address for user ID: {}", userId);
        logger.debug("Address creation details - City: {}, State: {}, Pincode: {}",
                addressDTO.getShippingCity(), addressDTO.getShippingState(), addressDTO.getShippingPincode());

        try {
            ShippingAddressDTO createdAddress = service.createAddress(userId, addressDTO);
            logger.info("Shipping address created successfully with ID: {} for user ID: {}",
                    createdAddress.getShippingId(), userId);
            return ResponseEntity.ok(createdAddress);
        } catch (RuntimeException e) {
            logger.error("Error creating shipping address for user ID {}: {}", userId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            logger.error("Unexpected error creating shipping address for user ID {}: {}", userId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/get-address-by-userId/{userId}")
    public ResponseEntity<List<ShippingAddressDTO>> getAddressesByUserId(
            @PathVariable Long userId) {
        logger.info("Received request to fetch shipping addresses for user ID: {}", userId);

        try {
            List<ShippingAddressDTO> addresses = service.getAddressesByUserId(userId);
            logger.info("Successfully retrieved {} shipping addresses for user ID: {}", addresses.size(), userId);
            return ResponseEntity.ok(addresses);
        } catch (RuntimeException e) {
            logger.warn("Error fetching shipping addresses for user ID {}: {}", userId, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            logger.error("Unexpected error fetching shipping addresses for user ID {}: {}", userId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/get-all-address")
    public ResponseEntity<List<ShippingAddressDTO>> getAllAddresses() {
        logger.info("Received request to fetch all shipping addresses");

        try {
            List<ShippingAddressDTO> addresses = service.getAllAddresses();
            logger.info("Successfully retrieved {} shipping addresses", addresses.size());
            return ResponseEntity.ok(addresses);
        } catch (Exception e) {
            logger.error("Error fetching all shipping addresses: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PatchMapping("/patch-address/{userId}/{shippingId}")
    public ResponseEntity<ShippingAddressDTO> updateAddress(
            @PathVariable Long userId,
            @PathVariable Long shippingId,
            @RequestBody ShippingAddressDTO addressDTO) {
        logger.info("Received request to update shipping address ID: {} for user ID: {}", shippingId, userId);
        logger.debug("Update details - City: {}, State: {}, Pincode: {}",
                addressDTO.getShippingCity(), addressDTO.getShippingState(), addressDTO.getShippingPincode());

        try {
            ShippingAddressDTO updatedAddress = service.updateAddress(userId, shippingId, addressDTO);
            logger.info("Shipping address ID: {} updated successfully for user ID: {}", shippingId, userId);
            return ResponseEntity.ok(updatedAddress);
        } catch (RuntimeException e) {
            logger.error("Error updating shipping address ID {} for user ID {}: {}",
                    shippingId, userId, e.getMessage(), e);

            if (e.getMessage().contains("not found") || e.getMessage().contains("does not belong")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            } else if (e.getMessage().contains("No valid fields")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
        } catch (Exception e) {
            logger.error("Unexpected error updating shipping address ID {} for user ID {}: {}",
                    shippingId, userId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/delete-address/{userId}/{shippingId}")
    public ResponseEntity<Void> deleteAddress(
            @PathVariable Long userId,
            @PathVariable Long shippingId) {
        logger.info("Received request to delete shipping address ID: {} for user ID: {}", shippingId, userId);

        try {
            service.deleteAddress(userId, shippingId);
            logger.info("Shipping address ID: {} deleted successfully for user ID: {}", shippingId, userId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            logger.error("Error deleting shipping address ID {} for user ID {}: {}",
                    shippingId, userId, e.getMessage(), e);

            if (e.getMessage().contains("not found") || e.getMessage().contains("does not belong")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        } catch (Exception e) {
            logger.error("Unexpected error deleting shipping address ID {} for user ID {}: {}",
                    shippingId, userId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Exception handler for more specific error handling
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException e) {
        logger.error("Runtime exception in ShippingAddressController: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        logger.error("Unexpected exception in ShippingAddressController: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An unexpected error occurred: " + e.getMessage());
    }
}