package com.thb.bakery.controller;

import com.thb.bakery.dto.request.RawMaterialRequestDto;
import com.thb.bakery.dto.response.RawMaterialResponseDto;
import com.thb.bakery.entity.MaterialHistoryEntity;
import com.thb.bakery.service.RawMaterialService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/raw-materials")
public class RawMaterialController {

    private static final Logger logger = LoggerFactory.getLogger(RawMaterialController.class);
    private final RawMaterialService rawMaterialService;

    public RawMaterialController(RawMaterialService rawMaterialService) {
        this.rawMaterialService = rawMaterialService;
    }

    @PostMapping("/create-raw-material")
    public ResponseEntity<RawMaterialResponseDto> createRawMaterial(@RequestBody RawMaterialRequestDto requestDto) {
        logger.info("POST /api/raw-materials - Creating raw material: {}", requestDto.getName());
        try {
            RawMaterialResponseDto response = rawMaterialService.createRawMaterial(requestDto);
            logger.info("Raw material created successfully with ID: {}", response.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            logger.error("Error creating raw material: {}", e.getMessage(), e);
            throw e;
        }
    }

    @PutMapping("/update-raw-material/{id}")
    public ResponseEntity<RawMaterialResponseDto> updateRawMaterial(
            @PathVariable Long id,
            @RequestBody RawMaterialRequestDto requestDto) {
        logger.info("PUT /api/raw-materials/{} - Updating raw material", id);
        try {
            RawMaterialResponseDto response = rawMaterialService.updateRawMaterial(id, requestDto);
            logger.info("Raw material updated successfully with ID: {}", id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error updating raw material with ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping("/get-raw-material-by-Id/{id}")
    public ResponseEntity<RawMaterialResponseDto> getRawMaterialById(@PathVariable Long id) {
        logger.info("GET /api/raw-materials/{} - Fetching raw material", id);
        try {
            RawMaterialResponseDto response = rawMaterialService.getRawMaterialById(id);
            logger.info("Raw material fetched successfully: {}", response.getName());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error fetching raw material with ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping("/get-all-raw-materials")
    public ResponseEntity<List<RawMaterialResponseDto>> getAllRawMaterials() {
        logger.info("GET /api/raw-materials - Fetching all raw materials");
        try {
            List<RawMaterialResponseDto> response = rawMaterialService.getAllRawMaterials();
            logger.info("Total raw materials fetched: {}", response.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error fetching all raw materials: {}", e.getMessage(), e);
            throw e;
        }
    }

    @DeleteMapping("/delete-raw-material/{id}")
    public ResponseEntity<Void> deleteRawMaterial(@PathVariable Long id) {
        logger.info("DELETE /api/raw-materials/{} - Deleting raw material", id);
        try {
            rawMaterialService.deleteRawMaterial(id);
            logger.info("Raw material deleted successfully with ID: {}", id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Error deleting raw material with ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping("/get-raw-materials-by-category/category/{category}")
    public ResponseEntity<List<RawMaterialResponseDto>> getRawMaterialsByCategory(@PathVariable String category) {
        logger.info("GET /api/raw-materials/category/{} - Fetching materials by category", category);
        try {
            List<RawMaterialResponseDto> response = rawMaterialService.getRawMaterialsByCategory(category);
            logger.info("Total materials found for category {}: {}", category, response.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error fetching materials by category {}: {}", category, e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping("/get-raw-materials-by-status/status/{status}")
    public ResponseEntity<List<RawMaterialResponseDto>> getRawMaterialsByStatus(@PathVariable String status) {
        logger.info("GET /api/raw-materials/status/{} - Fetching materials by status", status);
        try {
            List<RawMaterialResponseDto> response = rawMaterialService.getRawMaterialsByStatus(status);
            logger.info("Total materials found for status {}: {}", status, response.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error fetching materials by status {}: {}", status, e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping("/get-raw-materials-by-vendor/vendor/{vendorName}")
    public ResponseEntity<List<RawMaterialResponseDto>> getRawMaterialsByVendor(@PathVariable String vendorName) {
        logger.info("GET /api/raw-materials/vendor/{} - Fetching materials by vendor", vendorName);
        try {
            List<RawMaterialResponseDto> response = rawMaterialService.getRawMaterialsByVendor(vendorName);
            logger.info("Total materials found for vendor {}: {}", vendorName, response.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error fetching materials by vendor {}: {}", vendorName, e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping("/get-material-histroy/{id}/history")
    public ResponseEntity<List<MaterialHistoryEntity>> getMaterialHistory(@PathVariable Long id) {
        logger.info("GET /api/raw-materials/{}/history - Fetching material history", id);
        try {
            List<MaterialHistoryEntity> response = rawMaterialService.getMaterialHistory(id);
            logger.info("Total history records found for material {}: {}", id, response.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error fetching material history for ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }
}

