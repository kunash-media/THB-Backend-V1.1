package com.thb.bakery.controller;

import com.thb.bakery.dto.request.CustomizeCakeRequestDTO;
import com.thb.bakery.dto.response.CustomizeCakeResponseDTO;
import com.thb.bakery.service.CustomizeCakeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/thb-admin/api/customize-cakes")
@CrossOrigin("*")
public class CustomizeCakeController {
    private static final Logger logger = LoggerFactory.getLogger(CustomizeCakeController.class);

    @Autowired
    private CustomizeCakeService customizeCakeService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> createProduct(
            @RequestParam(value = "productImage", required = false) MultipartFile productImage,
            @RequestParam("title") String title,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "subcategory", required = false) String subcategory,
            @RequestParam(value = "discount", required = false) BigDecimal discount,
            @RequestParam("weights") List<String> weights,
            @RequestParam("oldPrices") List<BigDecimal> oldPrices,
            @RequestParam("newPrices") List<BigDecimal> newPrices) {

        logger.info("Received request to create product with title: {}", title);

        try {
            CustomizeCakeRequestDTO requestDTO = new CustomizeCakeRequestDTO();
            requestDTO.setProductImage(productImage);
            requestDTO.setTitle(title);
            requestDTO.setCategory(category);
            requestDTO.setSubcategory(subcategory);
            requestDTO.setDiscount(discount);
            requestDTO.setWeights(weights);
            requestDTO.setOldPrices(oldPrices);
            requestDTO.setNewPrices(newPrices);
            requestDTO.setIsActive(true);

            CustomizeCakeResponseDTO responseDTO = customizeCakeService.createProduct(requestDTO);

            // Add image URL to response
            if (responseDTO.getProductImage() != null && responseDTO.getProductImage().length > 0) {
                Map<String, Object> enhancedData = enhanceResponseWithImageUrl(responseDTO);

                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Customize cake created successfully");
                response.put("data", enhancedData);
                response.put("timestamp", LocalDateTime.now());

                logger.info("Successfully created product with ID: {}", responseDTO.getId());
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Customize cake created successfully");
                response.put("data", responseDTO);
                response.put("timestamp", LocalDateTime.now());

                logger.info("Successfully created product with ID: {}", responseDTO.getId());
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            }
        } catch (Exception e) {
            logger.error("Error creating product: {}", e.getMessage(), e);

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            errorResponse.put("timestamp", LocalDateTime.now());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateProduct(
            @PathVariable Long id,
            @RequestParam(value = "productImage", required = false) MultipartFile productImage,
            @RequestParam("title") String title,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "subcategory", required = false) String subcategory,
            @RequestParam(value = "discount", required = false) BigDecimal discount,
            @RequestParam("weights") List<String> weights,
            @RequestParam("oldPrices") List<BigDecimal> oldPrices,
            @RequestParam("newPrices") List<BigDecimal> newPrices) {

        logger.info("Received request to update product with ID: {}", id);

        try {
            CustomizeCakeRequestDTO requestDTO = new CustomizeCakeRequestDTO();
            requestDTO.setProductImage(productImage);
            requestDTO.setTitle(title);
            requestDTO.setCategory(category);
            requestDTO.setSubcategory(subcategory);
            requestDTO.setDiscount(discount);
            requestDTO.setWeights(weights);
            requestDTO.setOldPrices(oldPrices);
            requestDTO.setNewPrices(newPrices);

            CustomizeCakeResponseDTO responseDTO = customizeCakeService.updateProduct(id, requestDTO);

            // Add image URL to response
            if (responseDTO.getProductImage() != null && responseDTO.getProductImage().length > 0) {
                Map<String, Object> enhancedData = enhanceResponseWithImageUrl(responseDTO);

                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Customize cake updated successfully");
                response.put("data", enhancedData);
                response.put("timestamp", LocalDateTime.now());

                logger.info("Successfully updated product with ID: {}", id);
                return ResponseEntity.ok(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Customize cake updated successfully");
                response.put("data", responseDTO);
                response.put("timestamp", LocalDateTime.now());

                logger.info("Successfully updated product with ID: {}", id);
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            logger.error("Error updating product with ID {}: {}", id, e.getMessage(), e);

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            errorResponse.put("timestamp", LocalDateTime.now());

            HttpStatus status = e.getMessage().contains("not found") ?
                    HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;
            return ResponseEntity.status(status).body(errorResponse);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getProductById(@PathVariable Long id) {
        logger.debug("Received request to get product with ID: {}", id);

        try {
            CustomizeCakeResponseDTO responseDTO = customizeCakeService.getProductById(id);

            // Add image URL to response
            if (responseDTO.getProductImage() != null && responseDTO.getProductImage().length > 0) {
                Map<String, Object> enhancedData = enhanceResponseWithImageUrl(responseDTO);

                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Customize cake retrieved successfully");
                response.put("data", enhancedData);
                response.put("timestamp", LocalDateTime.now());

                logger.debug("Successfully retrieved product with ID: {}", id);
                return ResponseEntity.ok(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Customize cake retrieved successfully");
                response.put("data", responseDTO);
                response.put("timestamp", LocalDateTime.now());

                logger.debug("Successfully retrieved product with ID: {}", id);
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            logger.error("Error retrieving product with ID {}: {}", id, e.getMessage());

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            errorResponse.put("timestamp", LocalDateTime.now());

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllProducts() {
        logger.debug("Received request to get all products");

        try {
            List<CustomizeCakeResponseDTO> products = customizeCakeService.getAllProducts();

            // Add image URLs to all products
            List<Map<String, Object>> enhancedProducts = products.stream()
                    .map(this::enhanceResponseWithImageUrl)
                    .collect(java.util.stream.Collectors.toList());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Customize cakes retrieved successfully");
            response.put("data", enhancedProducts);
            response.put("timestamp", LocalDateTime.now());

            logger.debug("Successfully retrieved {} products", products.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error retrieving products: {}", e.getMessage(), e);

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error retrieving products");
            errorResponse.put("timestamp", LocalDateTime.now());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/pageable")
    public ResponseEntity<Map<String, Object>> getAllProductsWithPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortOrder) {

        logger.debug("Received request to get products with pagination - page: {}, size: {}, sortBy: {}, sortOrder: {}",
                page, size, sortBy, sortOrder);

        try {
            Sort sort = sortOrder.equalsIgnoreCase("asc") ?
                    Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
            Pageable pageable = PageRequest.of(page, size, sort);

            Page<CustomizeCakeResponseDTO> productsPage = customizeCakeService.getAllProducts(pageable);

            // Add image URLs to all products in page
            Page<Map<String, Object>> enhancedPage = productsPage.map(this::enhanceResponseWithImageUrl);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Customize cakes retrieved successfully");
            response.put("data", enhancedPage);
            response.put("timestamp", LocalDateTime.now());

            logger.debug("Successfully retrieved {} products on page {}",
                    productsPage.getNumberOfElements(), page);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error retrieving products with pagination: {}", e.getMessage(), e);

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error retrieving products");
            errorResponse.put("timestamp", LocalDateTime.now());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchProducts(@RequestParam("q") String query) {
        logger.debug("Received search request with query: '{}'", query);

        try {
            List<CustomizeCakeResponseDTO> products = customizeCakeService.searchProducts(query);

            // Add image URLs to all products
            List<Map<String, Object>> enhancedProducts = products.stream()
                    .map(this::enhanceResponseWithImageUrl)
                    .collect(java.util.stream.Collectors.toList());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Search completed successfully");
            response.put("data", enhancedProducts);
            response.put("timestamp", LocalDateTime.now());

            logger.debug("Search completed successfully, found {} products", products.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error searching products with query '{}': {}", query, e.getMessage(), e);

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error searching products");
            errorResponse.put("timestamp", LocalDateTime.now());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<Map<String, Object>> getProductsByCategory(@PathVariable String category) {
        logger.debug("Received request to get products by category: '{}'", category);

        try {
            List<CustomizeCakeResponseDTO> products = customizeCakeService.getProductsByCategory(category);

            // Add image URLs to all products
            List<Map<String, Object>> enhancedProducts = products.stream()
                    .map(this::enhanceResponseWithImageUrl)
                    .collect(java.util.stream.Collectors.toList());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Customize cakes retrieved successfully");
            response.put("data", enhancedProducts);
            response.put("timestamp", LocalDateTime.now());

            logger.debug("Successfully retrieved {} products for category '{}'", products.size(), category);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error retrieving products by category '{}': {}", category, e.getMessage(), e);

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error retrieving products by category");
            errorResponse.put("timestamp", LocalDateTime.now());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/subcategory/{subcategory}")
    public ResponseEntity<Map<String, Object>> getProductsBySubcategory(@PathVariable String subcategory) {
        logger.debug("Received request to get products by subcategory: '{}'", subcategory);

        try {
            List<CustomizeCakeResponseDTO> products = customizeCakeService.getProductsBySubcategory(subcategory);

            // Add image URLs to all products
            List<Map<String, Object>> enhancedProducts = products.stream()
                    .map(this::enhanceResponseWithImageUrl)
                    .collect(java.util.stream.Collectors.toList());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Customize cakes retrieved successfully");
            response.put("data", enhancedProducts);
            response.put("timestamp", LocalDateTime.now());

            logger.debug("Successfully retrieved {} products for subcategory '{}'", products.size(), subcategory);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error retrieving products by subcategory '{}': {}", subcategory, e.getMessage(), e);

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error retrieving products by subcategory");
            errorResponse.put("timestamp", LocalDateTime.now());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/filter")
    public ResponseEntity<Map<String, Object>> getProductsByCategoryAndSubcategory(
            @RequestParam String category,
            @RequestParam String subcategory) {

        logger.debug("Received request to get products by category: '{}' and subcategory: '{}'", category, subcategory);

        try {
            List<CustomizeCakeResponseDTO> products = customizeCakeService.getProductsByCategoryAndSubcategory(category, subcategory);

            // Add image URLs to all products
            List<Map<String, Object>> enhancedProducts = products.stream()
                    .map(this::enhanceResponseWithImageUrl)
                    .collect(java.util.stream.Collectors.toList());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Customize cakes retrieved successfully");
            response.put("data", enhancedProducts);
            response.put("timestamp", LocalDateTime.now());

            logger.debug("Successfully retrieved {} products for category '{}' and subcategory '{}'",
                    products.size(), category, subcategory);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error retrieving products by category '{}' and subcategory '{}': {}",
                    category, subcategory, e.getMessage(), e);

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error retrieving products");
            errorResponse.put("timestamp", LocalDateTime.now());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/discount")
    public ResponseEntity<Map<String, Object>> getProductsWithMinimumDiscount(@RequestParam BigDecimal min) {
        logger.debug("Received request to get products with minimum discount: {}", min);

        try {
            List<CustomizeCakeResponseDTO> products = customizeCakeService.getProductsWithMinimumDiscount(min);

            // Add image URLs to all products
            List<Map<String, Object>> enhancedProducts = products.stream()
                    .map(this::enhanceResponseWithImageUrl)
                    .collect(java.util.stream.Collectors.toList());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Customize cakes retrieved successfully");
            response.put("data", enhancedProducts);
            response.put("timestamp", LocalDateTime.now());

            logger.debug("Successfully retrieved {} products with discount >= {}", products.size(), min);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error retrieving products with minimum discount {}: {}", min, e.getMessage(), e);

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error retrieving products");
            errorResponse.put("timestamp", LocalDateTime.now());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteProduct(@PathVariable Long id) {
        logger.info("Received request to delete product with ID: {}", id);

        try {
            customizeCakeService.deleteProduct(id);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Customize cake deleted successfully");
            response.put("timestamp", LocalDateTime.now());

            logger.info("Successfully deleted product with ID: {}", id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error deleting product with ID {}: {}", id, e.getMessage(), e);

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            errorResponse.put("timestamp", LocalDateTime.now());

            HttpStatus status = e.getMessage().contains("not found") ?
                    HttpStatus.NOT_FOUND : HttpStatus.INTERNAL_SERVER_ERROR;
            return ResponseEntity.status(status).body(errorResponse);
        }
    }

    @DeleteMapping("/{id}/permanent")
    public ResponseEntity<Map<String, Object>> permanentDeleteProduct(@PathVariable Long id) {
        logger.info("Received request to permanently delete product with ID: {}", id);

        try {
            customizeCakeService.permanentDeleteProduct(id);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Customize cake permanently deleted successfully");
            response.put("timestamp", LocalDateTime.now());

            logger.info("Successfully permanently deleted product with ID: {}", id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error permanently deleting product with ID {}: {}", id, e.getMessage(), e);

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            errorResponse.put("timestamp", LocalDateTime.now());

            HttpStatus status = e.getMessage().contains("not found") ?
                    HttpStatus.NOT_FOUND : HttpStatus.INTERNAL_SERVER_ERROR;
            return ResponseEntity.status(status).body(errorResponse);
        }
    }

    @PatchMapping("/{id}/toggle-status")
    public ResponseEntity<Map<String, Object>> toggleProductStatus(@PathVariable Long id) {
        logger.info("Received request to toggle status for product with ID: {}", id);

        try {
            CustomizeCakeResponseDTO responseDTO = customizeCakeService.toggleProductStatus(id);

            // Add image URL to response
            if (responseDTO.getProductImage() != null && responseDTO.getProductImage().length > 0) {
                Map<String, Object> enhancedData = enhanceResponseWithImageUrl(responseDTO);

                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Status toggled successfully");
                response.put("data", enhancedData);
                response.put("timestamp", LocalDateTime.now());

                logger.info("Successfully toggled status for product with ID: {}", id);
                return ResponseEntity.ok(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Status toggled successfully");
                response.put("data", responseDTO);
                response.put("timestamp", LocalDateTime.now());

                logger.info("Successfully toggled status for product with ID: {}", id);
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            logger.error("Error toggling status for product with ID {}: {}", id, e.getMessage(), e);

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            errorResponse.put("timestamp", LocalDateTime.now());

            HttpStatus status = e.getMessage().contains("not found") ?
                    HttpStatus.NOT_FOUND : HttpStatus.INTERNAL_SERVER_ERROR;
            return ResponseEntity.status(status).body(errorResponse);
        }
    }

    @GetMapping("/count")
    public ResponseEntity<Map<String, Object>> getActiveProductsCount() {
        logger.debug("Received request to get active products count");

        try {
            long count = customizeCakeService.getActiveProductsCount();

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Count retrieved successfully");
            response.put("data", count);
            response.put("timestamp", LocalDateTime.now());

            logger.debug("Successfully retrieved active products count: {}", count);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error retrieving active products count: {}", e.getMessage(), e);

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error retrieving count");
            errorResponse.put("timestamp", LocalDateTime.now());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // IMAGE SERVING ENDPOINTS - Serve binary images as URLs
    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getProductImage(@PathVariable Long id) {
        logger.debug("Fetching image for product ID: {}", id);

        try {
            CustomizeCakeResponseDTO product = customizeCakeService.getProductById(id);

            if (product.getProductImage() != null && product.getProductImage().length > 0) {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.IMAGE_JPEG); // Adjust based on your image type
                headers.setContentLength(product.getProductImage().length);
                headers.set("Content-Disposition", "inline; filename=\"product-" + id + ".jpg\"");

                logger.debug("Successfully served image for product ID: {}", id);
                return new ResponseEntity<>(product.getProductImage(), headers, HttpStatus.OK);
            } else {
                logger.warn("No image found for product ID: {}", id);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Error fetching product image for ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}/image/thumbnail")
    public ResponseEntity<byte[]> getProductImageThumbnail(@PathVariable Long id) {
        logger.debug("Fetching thumbnail image for product ID: {}", id);

        try {
            CustomizeCakeResponseDTO product = customizeCakeService.getProductById(id);

            if (product.getProductImage() != null && product.getProductImage().length > 0) {
                // For thumbnail, you might want to resize the image
                // For now, returning the same image
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.IMAGE_JPEG);
                headers.setContentLength(product.getProductImage().length);
                headers.set("Content-Disposition", "inline; filename=\"product-thumbnail-" + id + ".jpg\"");

                logger.debug("Successfully served thumbnail for product ID: {}", id);
                return new ResponseEntity<>(product.getProductImage(), headers, HttpStatus.OK);
            } else {
                logger.warn("No image found for product ID: {}", id);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Error fetching product thumbnail for ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Helper method to enhance response with image URL
    private Map<String, Object> enhanceResponseWithImageUrl(CustomizeCakeResponseDTO responseDTO) {
        Map<String, Object> enhancedData = new HashMap<>();

        // Copy all existing properties
        enhancedData.put("id", responseDTO.getId());
        enhancedData.put("title", responseDTO.getTitle());
        enhancedData.put("category", responseDTO.getCategory());
        enhancedData.put("subcategory", responseDTO.getSubcategory());
        enhancedData.put("discount", responseDTO.getDiscount());
        enhancedData.put("weights", responseDTO.getWeights());
        enhancedData.put("oldPrices", responseDTO.getOldPrices());
        enhancedData.put("newPrices", responseDTO.getNewPrices());
        enhancedData.put("isActive", responseDTO.getIsActive());
        enhancedData.put("createdAt", responseDTO.getCreatedAt());
        enhancedData.put("updatedAt", responseDTO.getUpdatedAt());

        // Add image URL instead of byte array
        if (responseDTO.getProductImage() != null && responseDTO.getProductImage().length > 0) {
            enhancedData.put("imageUrl", "/thb-admin/api/customize-cakes/" + responseDTO.getId() + "/image");
            enhancedData.put("thumbnailUrl", "/thb-admin/api/customize-cakes/" + responseDTO.getId() + "/image/thumbnail");
            // You can still include the byte array if needed, but typically you'd exclude it
            // enhancedData.put("productImage", responseDTO.getProductImage());
        } else {
            enhancedData.put("imageUrl", null);
            enhancedData.put("thumbnailUrl", null);
        }

        return enhancedData;
    }
}