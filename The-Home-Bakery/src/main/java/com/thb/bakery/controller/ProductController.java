package com.thb.bakery.controller;

import com.thb.bakery.dto.request.ProductCreateRequestDTO;
import com.thb.bakery.dto.request.ProductDTO;
import com.thb.bakery.dto.request.ProductDataDTO;
import com.thb.bakery.dto.request.ProductPatchRequestDTO;
import com.thb.bakery.dto.response.ApiResponse;
import com.thb.bakery.entity.ProductEntity;
import com.thb.bakery.repository.ProductRepository;
import com.thb.bakery.service.ProductService;
import com.fasterxml.jackson.core.type.TypeReference;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping(value = "/create-product", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ProductDTO>> createProduct(
            @RequestPart("productData") String productData,
            @RequestPart(value = "productImage", required = false) MultipartFile productImage,
            @RequestPart(value = "productSubImages", required = false) MultipartFile[] productSubImages) {

        logger.info("Creating new product with data: {}", productData);

        try {
            ProductDataDTO productDataDTO = objectMapper.readValue(productData, ProductDataDTO.class);
            logger.debug("Parsed product data: {}", productDataDTO.getProductName());

            validateWeightsAndPrices(productDataDTO);
            ProductCreateRequestDTO requestDTO = mapToCreateRequest(productDataDTO, productImage, productSubImages);
            ProductDTO createdProduct = productService.createProduct(requestDTO);

            logger.info("Product created successfully with ID: {}", createdProduct.getProductId());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Product created successfully", createdProduct));

        } catch (IllegalArgumentException e) {
            logger.error("Invalid input for product creation: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Invalid input: " + e.getMessage()));
        } catch (Exception e) {
            logger.error("Error creating product: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to create product: " + e.getMessage()));
        }
    }

    @GetMapping("/get-all-products")
    public ResponseEntity<ApiResponse<Page<ProductDTO>>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        logger.info("Fetching all products - page: {}, size: {}", page, size);

        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<ProductDTO> productPage = productService.getAllProducts(pageable);

            logger.info("Retrieved {} products out of {} total",
                    productPage.getNumberOfElements(), productPage.getTotalElements());

            return ResponseEntity.ok(ApiResponse.success(
                    String.format("Retrieved %d products successfully", productPage.getNumberOfElements()),
                    productPage));

        } catch (Exception e) {
            logger.error("Error retrieving products: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve products: " + e.getMessage()));
        }
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse<ProductDTO>> getProductById(@PathVariable Long productId) {
        logger.info("Fetching product with ID: {}", productId);

        try {
            ProductDTO product = productService.getProductById(productId);
            logger.info("Product retrieved successfully: {}", product.getProductName());

            return ResponseEntity.ok(ApiResponse.success("Product retrieved successfully", product));

        } catch (IllegalArgumentException e) {
            logger.warn("Product not found with ID: {}", productId);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Invalid input: " + e.getMessage()));
        } catch (Exception e) {
            logger.error("Error retrieving product with ID {}: {}", productId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve product: " + e.getMessage()));
        }
    }

    @PatchMapping(value = "/update-product/{productId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ProductDTO>> patchProduct(
            @PathVariable Long productId,
            @RequestPart(value = "productData", required = false) String productData,
            @RequestPart(value = "productImage", required = false) MultipartFile productImage,
            @RequestPart(value = "productSubImages", required = false) MultipartFile[] productSubImages,
            @RequestPart(value = "existingProductImage", required = false) String existingProductImage,
            @RequestPart(value = "existingProductSubImages", required = false) String existingProductSubImages) {

        logger.info("Partially updating product with ID: {}", productId);

        try {
            ProductPatchRequestDTO patchRequest = buildPatchRequest(
                    productData, productImage, productSubImages, existingProductImage, existingProductSubImages);

            ProductDTO updatedProduct = productService.patchProduct(productId, patchRequest);
            logger.info("Product updated successfully: {}", updatedProduct.getProductName());

            return ResponseEntity.ok(ApiResponse.success("Product updated successfully", updatedProduct));

        } catch (IllegalArgumentException e) {
            logger.warn("Invalid input for product update: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Invalid input: " + e.getMessage()));
        } catch (Exception e) {
            logger.error("Error updating product with ID {}: {}", productId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to update product: " + e.getMessage()));
        }
    }

    @PutMapping(value = "/update-product/{productId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ProductDTO>> updateProduct(
            @PathVariable Long productId,
            @RequestPart("productData") String productData,
            @RequestPart(value = "productImage", required = false) MultipartFile productImage,
            @RequestPart(value = "productSubImages", required = false) MultipartFile[] productSubImages) {

        logger.info("Fully updating product with ID: {}", productId);

        try {
            ProductDataDTO productDataDTO = objectMapper.readValue(productData, ProductDataDTO.class);

            validateWeightsAndPrices(productDataDTO);
            ProductCreateRequestDTO requestDTO = mapToCreateRequest(productDataDTO, productImage, productSubImages);
            ProductDTO updatedProduct = productService.updateProduct(productId, requestDTO);

            logger.info("Product fully updated successfully: {}", updatedProduct.getProductName());
            return ResponseEntity.ok(ApiResponse.success("Product updated successfully", updatedProduct));

        } catch (IllegalArgumentException e) {
            logger.warn("Invalid input for product full update: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Invalid input: " + e.getMessage()));
        } catch (Exception e) {
            logger.error("Error in full update of product with ID {}: {}", productId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to update product: " + e.getMessage()));
        }
    }

    @DeleteMapping("/delete-product/{productId}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Long productId) {
        logger.info("Deleting product with ID: {}", productId);

        try {
            productService.deleteProduct(productId);
            logger.info("Product deleted successfully with ID: {}", productId);

            return ResponseEntity.ok(ApiResponse.success("Product deleted successfully", null));

        } catch (IllegalArgumentException e) {
            logger.warn("Product not found for deletion with ID: {}", productId);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Invalid input: " + e.getMessage()));
        } catch (Exception e) {
            logger.error("Error deleting product with ID {}: {}", productId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to delete product: " + e.getMessage()));
        }
    }

    @GetMapping("/search/{name}")
    public ResponseEntity<ApiResponse<List<ProductDTO>>> getProductsByName(@PathVariable String name) {
        logger.info("Searching products with name: {}", name);

        try {
            List<ProductDTO> products = productService.getProductsByName(name);
            logger.info("Found {} products matching name: {}", products.size(), name);

            return ResponseEntity.ok(ApiResponse.success(
                    String.format("Found %d products matching '%s'", products.size(), name),
                    products));

        } catch (IllegalArgumentException e) {
            logger.warn("Invalid search parameter: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Invalid input: " + e.getMessage()));
        } catch (Exception e) {
            logger.error("Error searching products by name '{}': {}", name, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to search products: " + e.getMessage()));
        }
    }

    @GetMapping("/exists/{productId}")
    public ResponseEntity<ApiResponse<Boolean>> checkProductExists(@PathVariable Long productId) {
        logger.info("Checking if product exists with ID: {}", productId);

        try {
            boolean exists = productService.existsById(productId);
            logger.debug("Product existence check for ID {}: {}", productId, exists);

            return ResponseEntity.ok(ApiResponse.success("Product existence checked", exists));

        } catch (IllegalArgumentException e) {
            logger.warn("Invalid product ID for existence check: {}", productId);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Invalid input: " + e.getMessage()));
        } catch (Exception e) {
            logger.error("Error checking product existence for ID {}: {}", productId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to check product existence: " + e.getMessage()));
        }
    }

    @GetMapping("/count")
    public ResponseEntity<ApiResponse<Long>> getProductCount() {
        logger.info("Getting total product count");

        try {
            long count = productService.getProductCount();
            logger.info("Total products count: {}", count);

            return ResponseEntity.ok(ApiResponse.success("Product count retrieved", count));

        } catch (Exception e) {
            logger.error("Error getting product count: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to get product count: " + e.getMessage()));
        }
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<ApiResponse<Page<ProductDTO>>> getProductsByCategory(
            @PathVariable String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        logger.info("Fetching products by category: {}, page: {}, size: {}", category, page, size);

        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<ProductDTO> productPage = productService.getProductsByCategory(category, pageable);
            logger.info("Found {} products in category '{}', page: {}, total elements: {}",
                    productPage.getNumberOfElements(), category, page, productPage.getTotalElements());

            return ResponseEntity.ok(ApiResponse.success(
                    String.format("Found %d products in category '%s'", productPage.getNumberOfElements(), category),
                    productPage));

        } catch (IllegalArgumentException e) {
            logger.warn("Invalid category parameter: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Invalid input: " + e.getMessage()));
        } catch (Exception e) {
            logger.error("Error fetching products by category '{}': {}", category, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to fetch products by category: " + e.getMessage()));
        }
    }

    @GetMapping("/food-type/{foodType}")
    public ResponseEntity<ApiResponse<List<ProductDTO>>> getProductsByFoodType(@PathVariable String foodType) {
        logger.info("Fetching products by food type: {}", foodType);

        try {
            List<ProductDTO> products = productService.getProductsByFoodType(foodType);
            logger.info("Found {} products with food type: {}", products.size(), foodType);

            return ResponseEntity.ok(ApiResponse.success(
                    String.format("Found %d products with food type '%s'", products.size(), foodType),
                    products));

        } catch (IllegalArgumentException e) {
            logger.warn("Invalid food type parameter: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Invalid input: " + e.getMessage()));
        } catch (Exception e) {
            logger.error("Error fetching products by food type '{}': {}", foodType, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to fetch products by food type: " + e.getMessage()));
        }
    }

    @GetMapping("/{productId}/image")
    public ResponseEntity<byte[]> getProductImage(@PathVariable Long productId) {
        logger.info("Fetching image for product ID: {}", productId);

        try {
            Optional<ProductEntity> product = productRepository.findById(productId);
            if (product.isPresent() && product.get().getProductImage() != null) {
                logger.debug("Product image found for ID: {}", productId);
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(product.get().getProductImage());
            }
            logger.warn("Product image not found for ID: {}", productId);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Error fetching product image for ID {}: {}", productId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{productId}/subimage/{index}")
    public ResponseEntity<byte[]> getProductSubImage(@PathVariable Long productId, @PathVariable int index) {
        logger.info("Fetching sub-image {} for product ID: {}", index, productId);

        try {
            Optional<ProductEntity> product = productRepository.findById(productId);
            if (product.isPresent() && index >= 0 && index < product.get().getProductSubImages().size()) {
                logger.debug("Product sub-image {} found for ID: {}", index, productId);
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(product.get().getProductSubImages().get(index));
            }
            logger.warn("Product sub-image {} not found for ID: {}", index, productId);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Error fetching product sub-image {} for ID {}: {}", index, productId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private void validateWeightsAndPrices(ProductDataDTO productDataDTO) {
        if (productDataDTO.getWeights() != null && productDataDTO.getWeightPrices() != null &&
                productDataDTO.getWeights().size() != productDataDTO.getWeightPrices().size()) {
            logger.warn("Weights and weightPrices size mismatch for product: {}", productDataDTO.getProductName());
            throw new IllegalArgumentException("Weights and weightPrices must have the same size");
        }
    }

    private ProductCreateRequestDTO mapToCreateRequest(ProductDataDTO productDataDTO,
                                                       MultipartFile productImage, MultipartFile[] productSubImages) throws Exception {

        ProductCreateRequestDTO requestDTO = new ProductCreateRequestDTO();
        requestDTO.setProductName(productDataDTO.getProductName());
        requestDTO.setProductCategory(productDataDTO.getProductCategory());
        requestDTO.setProductFoodType(productDataDTO.getProductFoodType());
        requestDTO.setSkuNumber(productDataDTO.getSkuNumber());
        requestDTO.setNameOnCake(productDataDTO.getNameOnCake());
        requestDTO.setOrderCount(productDataDTO.getOrderCount());
        requestDTO.setDescription(productDataDTO.getDescription());
        requestDTO.setProductIngredients(productDataDTO.getProductIngredients());
        requestDTO.setAllergenInfo(productDataDTO.getAllergenInfo());
        requestDTO.setCareInstructions(productDataDTO.getCareInstructions());
        requestDTO.setStorageInstructions(productDataDTO.getStorageInstructions());
        requestDTO.setShelfLife(productDataDTO.getShelfLife());
        requestDTO.setBestServed(productDataDTO.getBestServed());
        requestDTO.setPreparationTime(productDataDTO.getPreparationTime());
        requestDTO.setFlavor(productDataDTO.getFlavor());
        requestDTO.setShape(productDataDTO.getShape());
        requestDTO.setDefaultWeight(productDataDTO.getDefaultWeight());
        requestDTO.setLayers(productDataDTO.getLayers());
        requestDTO.setServes(productDataDTO.getServes());
        requestDTO.setNote(productDataDTO.getNote());
        requestDTO.setProductOldPrice(productDataDTO.getProductOldPrice());
        requestDTO.setProductNewPrice(productDataDTO.getProductNewPrice());
        requestDTO.setWeights(productDataDTO.getWeights());
        requestDTO.setWeightPrices(productDataDTO.getWeightPrices());
        requestDTO.setFeatures(productDataDTO.getFeatures());
        requestDTO.setRatings(productDataDTO.getRatings());
        requestDTO.setReviews(productDataDTO.getReviews());
        requestDTO.setProductDiscount(productDataDTO.getProductDiscount());
        requestDTO.setDeliveryTime(productDataDTO.getDeliveryTime());
        requestDTO.setFreeDeliveryThreshold(productDataDTO.getFreeDeliveryThreshold());

        setImageFields(requestDTO, productImage, productSubImages);

        return requestDTO;
    }

    private ProductPatchRequestDTO buildPatchRequest(String productData, MultipartFile productImage,
                                                     MultipartFile[] productSubImages, String existingProductImage,
                                                     String existingProductSubImages) throws Exception {

        ProductPatchRequestDTO patchRequest = new ProductPatchRequestDTO();

        if (productData != null) {
            ProductDataDTO productDataDTO = objectMapper.readValue(productData, ProductDataDTO.class);
            validateWeightsAndPrices(productDataDTO);

            if (productDataDTO.getProductName() != null) patchRequest.setProductName(productDataDTO.getProductName());
            if (productDataDTO.getProductCategory() != null) patchRequest.setProductCategory(productDataDTO.getProductCategory());
            if (productDataDTO.getProductFoodType() != null) patchRequest.setProductFoodType(productDataDTO.getProductFoodType());
            if (productDataDTO.getSkuNumber() != null) patchRequest.setSkuNumber(productDataDTO.getSkuNumber());
            if (productDataDTO.getNameOnCake() != null) patchRequest.setNameOnCake(productDataDTO.getNameOnCake());
            if (productDataDTO.getOrderCount() != null) patchRequest.setOrderCount(productDataDTO.getOrderCount());
            if (productDataDTO.getDescription() != null) patchRequest.setDescription(productDataDTO.getDescription());
            if (productDataDTO.getProductIngredients() != null) patchRequest.setProductIngredients(productDataDTO.getProductIngredients());
            if (productDataDTO.getAllergenInfo() != null) patchRequest.setAllergenInfo(productDataDTO.getAllergenInfo());
            if (productDataDTO.getCareInstructions() != null) patchRequest.setCareInstructions(productDataDTO.getCareInstructions());
            if (productDataDTO.getStorageInstructions() != null) patchRequest.setStorageInstructions(productDataDTO.getStorageInstructions());
            if (productDataDTO.getShelfLife() != null) patchRequest.setShelfLife(productDataDTO.getShelfLife());
            if (productDataDTO.getBestServed() != null) patchRequest.setBestServed(productDataDTO.getBestServed());
            if (productDataDTO.getPreparationTime() != null) patchRequest.setPreparationTime(productDataDTO.getPreparationTime());
            if (productDataDTO.getFlavor() != null) patchRequest.setFlavor(productDataDTO.getFlavor());
            if (productDataDTO.getShape() != null) patchRequest.setShape(productDataDTO.getShape());
            if (productDataDTO.getDefaultWeight() != null) patchRequest.setDefaultWeight(productDataDTO.getDefaultWeight());
            if (productDataDTO.getLayers() != null) patchRequest.setLayers(productDataDTO.getLayers());
            if (productDataDTO.getServes() != null) patchRequest.setServes(productDataDTO.getServes());
            if (productDataDTO.getNote() != null) patchRequest.setNote(productDataDTO.getNote());
            if (productDataDTO.getProductOldPrice() != null) patchRequest.setProductOldPrice(productDataDTO.getProductOldPrice());
            if (productDataDTO.getProductNewPrice() != null) patchRequest.setProductNewPrice(productDataDTO.getProductNewPrice());
            if (productDataDTO.getWeights() != null) patchRequest.setWeights(productDataDTO.getWeights());
            if (productDataDTO.getWeightPrices() != null) patchRequest.setWeightPrices(productDataDTO.getWeightPrices());
            if (productDataDTO.getFeatures() != null) patchRequest.setFeatures(productDataDTO.getFeatures());
            if (productDataDTO.getRatings() != null) patchRequest.setRatings(productDataDTO.getRatings());
            if (productDataDTO.getReviews() != null) patchRequest.setReviews(productDataDTO.getReviews());
            if (productDataDTO.getProductDiscount() != null) patchRequest.setProductDiscount(productDataDTO.getProductDiscount());
            if (productDataDTO.getDeliveryTime() != null) patchRequest.setDeliveryTime(productDataDTO.getDeliveryTime());
            if (productDataDTO.getFreeDeliveryThreshold() != null) patchRequest.setFreeDeliveryThreshold(productDataDTO.getFreeDeliveryThreshold());
        }

        if (productImage != null && !productImage.isEmpty()) {
            patchRequest.setProductImage(productImage.getBytes());
            patchRequest.setProductImagePresent(true);
        } else if (existingProductImage != null && !existingProductImage.trim().isEmpty()) {
            patchRequest.setExistingProductImage(existingProductImage.trim());
        }

        if (productSubImages != null && productSubImages.length > 0) {
            List<byte[]> subImages = new ArrayList<>();
            for (MultipartFile file : productSubImages) {
                if (!file.isEmpty()) {
                    subImages.add(file.getBytes());
                }
            }
            patchRequest.setProductSubImages(subImages);
            patchRequest.setProductSubImagesPresent(true);
        } else if (existingProductSubImages != null && !existingProductSubImages.trim().isEmpty()) {
            List<String> subImageUrls = objectMapper.readValue(existingProductSubImages, new TypeReference<List<String>>(){});
            patchRequest.setExistingProductSubImages(subImageUrls);
        }

        return patchRequest;
    }

    private void setImageFields(ProductCreateRequestDTO requestDTO, MultipartFile productImage,
                                MultipartFile[] productSubImages) throws Exception {
        requestDTO.setProductImagePresent(productImage != null && !productImage.isEmpty());
        if (productImage != null && !productImage.isEmpty()) {
            requestDTO.setProductImage(productImage.getBytes());
        }

        requestDTO.setProductSubImagesPresent(productSubImages != null && productSubImages.length > 0);
        if (productSubImages != null && productSubImages.length > 0) {
            List<byte[]> subImages = new ArrayList<>();
            for (MultipartFile file : productSubImages) {
                if (!file.isEmpty()) {
                    subImages.add(file.getBytes());
                }
            }
            requestDTO.setProductSubImages(subImages);
        }
    }
}