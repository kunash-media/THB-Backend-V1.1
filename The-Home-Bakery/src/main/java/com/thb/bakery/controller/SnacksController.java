package com.thb.bakery.controller;

import com.thb.bakery.dto.request.SnacksDTO;
import com.thb.bakery.entity.SnacksEntity;
import com.thb.bakery.repository.SnacksRepository;
import com.thb.bakery.service.SnacksService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/snacks")
public class SnacksController {

    private static final Logger logger = LoggerFactory.getLogger(SnacksController.class);

    @Autowired private SnacksService snacksService;
    @Autowired private SnacksRepository snacksRepository;
    @Autowired private ObjectMapper objectMapper;

    @PostMapping(value = "/create-snack", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SnacksDTO> createSnack(
            @RequestPart("productData") String productData,
            @RequestPart(value = "productMainImage", required = false) MultipartFile productMainImage) {
        logger.info("Creating new snack");
        try {
            SnacksDTO created = snacksService.create(productData, productMainImage);
//            logger.info("Snack created with ID: {}", created.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            logger.error("Error creating snack: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/get-all-snacks")
    public ResponseEntity<Page<SnacksDTO>> getAllSnacks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        logger.info("Fetching all snacks - page: {}, size: {}", page, size);
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<SnacksDTO> result = snacksService.getAll(pageable);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Error retrieving snacks", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{snackId}")
    public ResponseEntity<SnacksDTO> getSnackById(@PathVariable Long snackId) {
        logger.info("Fetching snack ID: {}", snackId);
        try {
            SnacksDTO snack = snacksService.getById(snackId);
            return ResponseEntity.ok(snack);
        } catch (Exception e) {
            logger.error("Snack not found: {}", snackId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<Page<SnacksDTO>> getByCategory(
            @PathVariable String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        logger.info("Fetching by category: {}", category);
        try {
            return ResponseEntity.ok(snacksService.getByCategory(category, PageRequest.of(page, size)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/subcategory/{subcategory}")
    public ResponseEntity<Page<SnacksDTO>> getBySubcategory(
            @PathVariable String subcategory,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        logger.info("Fetching by subcategory: {}", subcategory);
        try {
            return ResponseEntity.ok(snacksService.getBySubcategory(subcategory, PageRequest.of(page, size)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/search/{name}")
    public ResponseEntity<List<SnacksDTO>> searchByName(@PathVariable String name) {
        logger.info("Searching snacks: {}", name);
        try {
            return ResponseEntity.ok(snacksService.searchByName(name));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping(value = "/update-snack/{snackId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SnacksDTO> updateSnack(
            @PathVariable Long snackId,
            @RequestPart("productData") String productData,
            @RequestPart(value = "productMainImage", required = false) MultipartFile productMainImage) {
        logger.info("Updating snack ID: {}", snackId);
        try {
            return ResponseEntity.ok(snacksService.update(snackId, productData, productMainImage));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PatchMapping(value = "/update-snack/{snackId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SnacksDTO> patchSnack(
            @PathVariable Long snackId,
            @RequestPart(value = "productData", required = false) String productData,
            @RequestPart(value = "productMainImage", required = false) MultipartFile productMainImage) {
        logger.info("Patching snack ID: {}", snackId);
        try {
            return ResponseEntity.ok(snacksService.patch(snackId, productData, productMainImage));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/delete-snack/{snackId}")
    public ResponseEntity<Void> deleteSnack(@PathVariable Long snackId) {
        logger.info("Deleting snack ID: {}", snackId);
        try {
            snacksService.delete(snackId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{snackId}/image")
    public ResponseEntity<byte[]> getImage(@PathVariable Long snackId) {
        logger.info("Fetching image for ID: {}", snackId);
        try {
            Optional<SnacksEntity> opt = snacksRepository.findById(snackId);
            if (opt.isPresent() && opt.get().getProductMainImage() != null) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(opt.get().getProductMainImage());
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
