package com.thb.bakery.controller;

import com.thb.bakery.dto.request.CustomizationRequestDTO;
import com.thb.bakery.dto.response.CustomizationResponseDTO;
import com.thb.bakery.service.CustomizationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customizations")
public class CustomizationController {
    private static final Logger logger = LoggerFactory.getLogger(CustomizationController.class);

    @Autowired
    private CustomizationService customizationService;

    @PostMapping("/create-custom-order")
    public ResponseEntity<CustomizationResponseDTO> createCustomization(@RequestBody CustomizationRequestDTO requestDTO) {
        logger.info("Received POST request to create customization");
        CustomizationResponseDTO responseDTO = customizationService.createCustomization(requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/get-custom-order-by-id/{id}")
    public ResponseEntity<CustomizationResponseDTO> getCustomization(@PathVariable Long id) {
        logger.info("Received GET request for customization with ID: {}", id);
        CustomizationResponseDTO responseDTO = customizationService.getCustomizationById(id);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @GetMapping("/get-all-custom-orders")
    public ResponseEntity<List<CustomizationResponseDTO>> getAllCustomizations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        logger.info("Received GET request for all customizations with page: {} and size: {}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<CustomizationResponseDTO> responsePage = customizationService.getAllCustomizations(pageable);
        return new ResponseEntity<>(responsePage.getContent(), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomizationResponseDTO> updateCustomization(@PathVariable Long id, @RequestBody CustomizationRequestDTO requestDTO) {
        logger.info("Received PUT request to update customization with ID: {}", id);
        CustomizationResponseDTO responseDTO = customizationService.updateCustomization(id, requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @PatchMapping("/patch-custom-order-by-id/{id}")
    public ResponseEntity<CustomizationResponseDTO> patchCustomization(@PathVariable Long id, @RequestBody CustomizationRequestDTO requestDTO) {
        logger.info("Received PATCH request to update customization with ID: {}", id);
        CustomizationResponseDTO responseDTO = customizationService.patchCustomization(id, requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @DeleteMapping("/delete-custom-order/{id}")
    public ResponseEntity<Void> deleteCustomization(@PathVariable Long id) {
        logger.info("Received DELETE request for customization with ID: {}", id);
        customizationService.deleteCustomization(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}