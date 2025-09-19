package com.bakery.The.Home.Bakery.controller;

import com.bakery.The.Home.Bakery.dto.request.ProductCreateRequestDTO;
import com.bakery.The.Home.Bakery.dto.request.ProductDTO;
import com.bakery.The.Home.Bakery.dto.request.ProductDataDTO;
import com.bakery.The.Home.Bakery.dto.request.ProductPatchRequestDTO;
import com.bakery.The.Home.Bakery.entity.ProductEntity;
import com.bakery.The.Home.Bakery.repository.ProductRepository;
import com.bakery.The.Home.Bakery.service.ProductService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
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

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ObjectMapper objectMapper;



    @Data
    public static class ErrorResponse {
        private String message;
        private String details;
        private int status;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getDetails() {
            return details;
        }

        public void setDetails(String details) {
            this.details = details;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }

    @PostMapping(value = "/create-product", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createProduct(
            @RequestPart("productData") String productData,
            @RequestPart(value = "productImage", required = false) MultipartFile productImage,
            @RequestPart(value = "productSubImages", required = false) MultipartFile[] productSubImages) {

        try {
            ProductDataDTO productDataDTO = objectMapper.readValue(productData, ProductDataDTO.class);

            if (productDataDTO.getWeights() != null && productDataDTO.getWeightPrices() != null &&
                    productDataDTO.getWeights().size() != productDataDTO.getWeightPrices().size()) {
                ErrorResponse error = new ErrorResponse();
                error.setMessage("Invalid input");
                error.setDetails("Weights and weightPrices must have the same size");
                error.setStatus(HttpStatus.BAD_REQUEST.value());
                return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
            }

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
            requestDTO.setProductImagePresent(productImage != null && !productImage.isEmpty());
            requestDTO.setProductSubImagesPresent(productSubImages != null && productSubImages.length > 0);

            if (productImage != null && !productImage.isEmpty()) {
                requestDTO.setProductImage(productImage.getBytes());
            }

            List<byte[]> subImages = new ArrayList<>();
            if (productSubImages != null) {
                for (MultipartFile file : productSubImages) {
                    if (!file.isEmpty()) {
                        subImages.add(file.getBytes());
                    }
                }
            }
            requestDTO.setProductSubImages(subImages);

            ProductDTO createdProduct = productService.createProduct(requestDTO);
            return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);

        } catch (IllegalArgumentException e) {
            ErrorResponse error = new ErrorResponse();
            error.setMessage("Invalid input");
            error.setDetails(e.getMessage());
            error.setStatus(HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse();
            error.setMessage("Internal server error");
            error.setDetails(e.getMessage());
            error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{productId}/image")
    public ResponseEntity<byte[]> getProductImage(@PathVariable Long productId) {
        try {
            Optional<ProductEntity> product = productRepository.findById(productId);
            if (product.isPresent() && product.get().getProductImage() != null) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(product.get().getProductImage());
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{productId}/subimage/{index}")
    public ResponseEntity<byte[]> getProductSubImage(@PathVariable Long productId, @PathVariable int index) {
        try {
            Optional<ProductEntity> product = productRepository.findById(productId);
            if (product.isPresent() && index >= 0 && index < product.get().getProductSubImages().size()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(product.get().getProductSubImages().get(index));
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/get-all-product")
    public ResponseEntity<?> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<ProductDTO> productPage = productService.getAllProducts(pageable);
            return new ResponseEntity<>(productPage, HttpStatus.OK);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse();
            error.setMessage("Failed to retrieve products");
            error.setDetails(e.getMessage());
            error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{productId}")
    public ResponseEntity<?> getProductById(@PathVariable Long productId) {
        try {
            ProductDTO product = productService.getProductById(productId);
            return new ResponseEntity<>(product, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            ErrorResponse error = new ErrorResponse();
            error.setMessage("Invalid input");
            error.setDetails(e.getMessage());
            error.setStatus(HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse();
            error.setMessage("Internal server error");
            error.setDetails(e.getMessage());
            error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping(value = "/update-product/{productId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> patchProduct(
            @PathVariable Long productId,
            @RequestPart(value = "productData", required = false) String productData,
            @RequestPart(value = "productImage", required = false) MultipartFile productImage,
            @RequestPart(value = "productSubImages", required = false) MultipartFile[] productSubImages,
            @RequestPart(value = "existingProductImage", required = false) String existingProductImage,
            @RequestPart(value = "existingProductSubImages", required = false) String existingProductSubImages) {

        try {
            ProductPatchRequestDTO patchRequest = new ProductPatchRequestDTO();

            if (productData != null) {
                ProductDataDTO productDataDTO = objectMapper.readValue(productData, ProductDataDTO.class);

                if (productDataDTO.getWeights() != null && productDataDTO.getWeightPrices() != null &&
                        productDataDTO.getWeights().size() != productDataDTO.getWeightPrices().size()) {
                    ErrorResponse error = new ErrorResponse();
                    error.setMessage("Invalid input");
                    error.setDetails("Weights and weightPrices must have the same size");
                    error.setStatus(HttpStatus.BAD_REQUEST.value());
                    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
                }

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
            } else if (existingProductImage != null) {
                patchRequest.setExistingProductImage(existingProductImage);
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
            } else if (existingProductSubImages != null) {
                List<String> subImageUrls = objectMapper.readValue(existingProductSubImages, new TypeReference<List<String>>(){});
                patchRequest.setExistingProductSubImages(subImageUrls);
            }

            ProductDTO updatedProduct = productService.patchProduct(productId, patchRequest);
            return new ResponseEntity<>(updatedProduct, HttpStatus.OK);

        } catch (IllegalArgumentException e) {
            ErrorResponse error = new ErrorResponse();
            error.setMessage("Invalid input");
            error.setDetails(e.getMessage());
            error.setStatus(HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse();
            error.setMessage("Internal server error");
            error.setDetails(e.getMessage());
            error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/update-product/{productId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateProduct(
            @PathVariable Long productId,
            @RequestPart("productData") String productData,
            @RequestPart(value = "productImage", required = false) MultipartFile productImage,
            @RequestPart(value = "productSubImages", required = false) MultipartFile[] productSubImages) {

        try {
            ProductDataDTO productDataDTO = objectMapper.readValue(productData, ProductDataDTO.class);

            if (productDataDTO.getWeights() != null && productDataDTO.getWeightPrices() != null &&
                    productDataDTO.getWeights().size() != productDataDTO.getWeightPrices().size()) {
                ErrorResponse error = new ErrorResponse();
                error.setMessage("Invalid input");
                error.setDetails("Weights and weightPrices must have the same size");
                error.setStatus(HttpStatus.BAD_REQUEST.value());
                return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
            }

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
            requestDTO.setProductImagePresent(productImage != null && !productImage.isEmpty());
            requestDTO.setProductSubImagesPresent(productSubImages != null && productSubImages.length > 0);

            if (productImage != null && !productImage.isEmpty()) {
                requestDTO.setProductImage(productImage.getBytes());
            }

            if (productSubImages != null && productSubImages.length > 0) {
                List<byte[]> subImages = new ArrayList<>();
                for (MultipartFile file : productSubImages) {
                    if (!file.isEmpty()) {
                        subImages.add(file.getBytes());
                    }
                }
                requestDTO.setProductSubImages(subImages);
            }

            ProductDTO updatedProduct = productService.updateProduct(productId, requestDTO);
            return new ResponseEntity<>(updatedProduct, HttpStatus.OK);

        } catch (IllegalArgumentException e) {
            ErrorResponse error = new ErrorResponse();
            error.setMessage("Invalid input");
            error.setDetails(e.getMessage());
            error.setStatus(HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse();
            error.setMessage("Internal server error");
            error.setDetails(e.getMessage());
            error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete-product/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long productId) {
        try {
            productService.deleteProduct(productId);
            return new ResponseEntity<>("Product deleted successfully", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            ErrorResponse error = new ErrorResponse();
            error.setMessage("Invalid input");
            error.setDetails(e.getMessage());
            error.setStatus(HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse();
            error.setMessage("Internal server error");
            error.setDetails(e.getMessage());
            error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/search/{name}")
    public ResponseEntity<?> getProductsByName(@PathVariable String name) {
        try {
            List<ProductDTO> products = productService.getProductsByName(name);
            return new ResponseEntity<>(products, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            ErrorResponse error = new ErrorResponse();
            error.setMessage("Invalid input");
            error.setDetails(e.getMessage());
            error.setStatus(HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse();
            error.setMessage("Internal server error");
            error.setDetails(e.getMessage());
            error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/exists/{productId}")
    public ResponseEntity<?> checkProductExists(@PathVariable Long productId) {
        try {
            boolean exists = productService.existsById(productId);
            return new ResponseEntity<>(exists, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            ErrorResponse error = new ErrorResponse();
            error.setMessage("Invalid input");
            error.setDetails(e.getMessage());
            error.setStatus(HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse();
            error.setMessage("Internal server error");
            error.setDetails(e.getMessage());
            error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/count")
    public ResponseEntity<?> getProductCount() {
        try {
            long count = productService.getProductCount();
            return new ResponseEntity<>(count, HttpStatus.OK);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse();
            error.setMessage("Internal server error");
            error.setDetails(e.getMessage());
            error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<?> getProductsByCategory(@PathVariable String category) {
        try {
            List<ProductDTO> products = productService.getProductsByCategory(category);
            return new ResponseEntity<>(products, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            ErrorResponse error = new ErrorResponse();
            error.setMessage("Invalid input");
            error.setDetails(e.getMessage());
            error.setStatus(HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse();
            error.setMessage("Internal server error");
            error.setDetails(e.getMessage());
            error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/food-type/{foodType}")
    public ResponseEntity<?> getProductsByFoodType(@PathVariable String foodType) {
        try {
            List<ProductDTO> products = productService.getProductsByFoodType(foodType);
            return new ResponseEntity<>(products, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            ErrorResponse error = new ErrorResponse();
            error.setMessage("Invalid input");
            error.setDetails(e.getMessage());
            error.setStatus(HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse();
            error.setMessage("Internal server error");
            error.setDetails(e.getMessage());
            error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}