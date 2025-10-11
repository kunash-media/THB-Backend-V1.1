package com.thb.bakery.controller;

import com.thb.bakery.dto.request.InventoryRequestDto;
import com.thb.bakery.dto.response.InventoryResponseDto;
import com.thb.bakery.service.InventoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {
    private static final Logger logger = LoggerFactory.getLogger(InventoryController.class);

    @Autowired
    private InventoryService service;

    @PostMapping("/create-inventory")
    public ResponseEntity<InventoryResponseDto> create(@RequestBody InventoryRequestDto requestDto) {
        logger.info("Received create request for: {}", requestDto.getName());
        InventoryResponseDto response = service.createItem(requestDto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-inventory-by-id/{id}")
    public ResponseEntity<InventoryResponseDto> getById(@PathVariable Long id) {
        logger.debug("Get request for ID: {}", id);
        InventoryResponseDto response = service.getItemById(id);
        return response != null ? ResponseEntity.ok(response) : ResponseEntity.notFound().build();
    }

    @GetMapping("/get-All-items")
    public ResponseEntity<List<InventoryResponseDto>> getAll() {
        logger.debug("Get all items request");
        return ResponseEntity.ok(service.getAllItems());
    }

    @PutMapping("update-inventory/{id}")
    public ResponseEntity<InventoryResponseDto> update(@PathVariable Long id, @RequestBody InventoryRequestDto requestDto) {
        logger.info("Update request for ID: {}", id);
        InventoryResponseDto response = service.updateItem(id, requestDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete-inventory/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        logger.info("Delete request for ID: {}", id);
        service.deleteItem(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/bulk-upload")
    public ResponseEntity<List<InventoryResponseDto>> bulkUpload(@RequestBody List<InventoryRequestDto> requestDtos) {
        logger.info("Bulk upload request for {} items", requestDtos.size());
        return ResponseEntity.ok(service.bulkUpload(requestDtos));
    }

    @GetMapping("/dashboard/get-dashboard")
    public ResponseEntity<InventoryResponseDto> getDashboard() {
        logger.debug("Dashboard request");
        return ResponseEntity.ok(service.getDashboard());
    }
}
