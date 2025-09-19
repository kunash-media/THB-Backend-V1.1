package com.bakery.The.Home.Bakery.service.serviceImpl;

import com.bakery.The.Home.Bakery.dto.request.ProductCreateRequestDTO;
import com.bakery.The.Home.Bakery.dto.request.ProductDTO;
import com.bakery.The.Home.Bakery.dto.request.ProductPatchRequestDTO;
import com.bakery.The.Home.Bakery.entity.ProductEntity;
import com.bakery.The.Home.Bakery.mapper.ProductMapper;
import com.bakery.The.Home.Bakery.repository.ProductRepository;
import com.bakery.The.Home.Bakery.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    @Override
    public ProductDTO createProduct(ProductCreateRequestDTO requestDTO) {
        logger.info("Creating new product with SKU: {}", requestDTO.getSkuNumber());

        try {
            validateProductRequest(requestDTO);
            logger.debug("Product validation passed for: {}", requestDTO.getProductName());

            // Check if product with same SKU exists
            Optional<ProductEntity> existingProduct = productRepository.findBySkuNumber(requestDTO.getSkuNumber());
            if (existingProduct.isPresent()) {
                logger.warn("Product creation failed - SKU already exists: {}", requestDTO.getSkuNumber());
                throw new IllegalArgumentException("Product with this SKU number already exists");
            }

            ProductEntity productEntity = productMapper.toEntity(requestDTO);
            logger.debug("Mapped DTO to entity for product: {}", productEntity.getProductName());

            ProductEntity savedEntity = productRepository.save(productEntity);
            logger.info("Product saved successfully with ID: {} and name: {}",
                    savedEntity.getProductId(), savedEntity.getProductName());

            ProductDTO resultDTO = productMapper.toDTO(savedEntity);
            logger.debug("Mapped entity to DTO for product ID: {}", savedEntity.getProductId());

            return resultDTO;

        } catch (IllegalArgumentException e) {
            logger.error("Validation failed for product creation: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error during product creation for SKU {}: {}",
                    requestDTO.getSkuNumber(), e.getMessage(), e);
            throw new RuntimeException("Failed to create product: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDTO> getAllProducts() {
        logger.info("Fetching all products without pagination");

        try {
            List<ProductEntity> entities = productRepository.findAll();
            logger.info("Retrieved {} products from database", entities.size());

            List<ProductDTO> resultList = productMapper.toDTOList(entities);
            logger.debug("Mapped {} entities to DTOs", resultList.size());

            return resultList;

        } catch (Exception e) {
            logger.error("Error retrieving all products: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to retrieve products: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductDTO> getAllProducts(Pageable pageable) {
        logger.info("Fetching products with pagination - page: {}, size: {}",
                pageable.getPageNumber(), pageable.getPageSize());

        try {
            Page<ProductEntity> entities = productRepository.findAll(pageable);
            logger.info("Retrieved page {} of {} with {} products out of {} total",
                    entities.getNumber() + 1, entities.getTotalPages(),
                    entities.getNumberOfElements(), entities.getTotalElements());

            Page<ProductDTO> resultPage = entities.map(productMapper::toDTO);
            logger.debug("Mapped paginated entities to DTOs");

            return resultPage;

        } catch (Exception e) {
            logger.error("Error retrieving paginated products: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to retrieve products: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDTO getProductById(Long productId) {
        logger.info("Fetching product by ID: {}", productId);

        try {
            validateProductId(productId);

            ProductEntity entity = productRepository.findById(productId)
                    .orElseThrow(() -> {
                        logger.warn("Product not found with ID: {}", productId);
                        return new IllegalArgumentException("Product not found with id: " + productId);
                    });

            logger.info("Product found: {} (ID: {})", entity.getProductName(), productId);

            ProductDTO resultDTO = productMapper.toDTO(entity);
            logger.debug("Mapped entity to DTO for product: {}", entity.getProductName());

            return resultDTO;

        } catch (IllegalArgumentException e) {
            logger.error("Invalid product ID or product not found: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error retrieving product with ID {}: {}", productId, e.getMessage(), e);
            throw new RuntimeException("Failed to retrieve product: " + e.getMessage(), e);
        }
    }

    @Override
    public ProductDTO patchProduct(Long productId, ProductPatchRequestDTO patchRequest) {
        logger.info("Partially updating product with ID: {}", productId);

        try {
            validateProductId(productId);

            ProductEntity existingEntity = productRepository.findById(productId)
                    .orElseThrow(() -> {
                        logger.warn("Product not found for patch update with ID: {}", productId);
                        return new IllegalArgumentException("Product not found with id: " + productId);
                    });

            String originalName = existingEntity.getProductName();
            logger.debug("Found existing product for patch: {}", originalName);

            // Validate weights and prices if both are provided
            if (patchRequest.getWeights() != null && patchRequest.getWeightPrices() != null &&
                    patchRequest.getWeights().size() != patchRequest.getWeightPrices().size()) {
                logger.warn("Weights and prices size mismatch in patch request for product ID: {}", productId);
                throw new IllegalArgumentException("Weights and weightPrices must have the same size");
            }

            // Update fields selectively
            updateEntityFromPatchRequest(existingEntity, patchRequest);

            ProductEntity updatedEntity = productRepository.save(existingEntity);
            logger.info("Product partially updated successfully: {} (ID: {})",
                    updatedEntity.getProductName(), productId);

            ProductDTO resultDTO = productMapper.toDTO(updatedEntity);
            logger.debug("Mapped updated entity to DTO");

            return resultDTO;

        } catch (IllegalArgumentException e) {
            logger.error("Validation failed for product patch: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error during product patch for ID {}: {}", productId, e.getMessage(), e);
            throw new RuntimeException("Failed to update product: " + e.getMessage(), e);
        }
    }

    @Override
    public ProductDTO updateProduct(Long productId, ProductCreateRequestDTO requestDTO) {
        logger.info("Fully updating product with ID: {}", productId);

        try {
            validateProductId(productId);
            validateProductRequest(requestDTO);

            ProductEntity existingEntity = productRepository.findById(productId)
                    .orElseThrow(() -> {
                        logger.warn("Product not found for full update with ID: {}", productId);
                        return new IllegalArgumentException("Product not found with id: " + productId);
                    });

            String originalName = existingEntity.getProductName();
            logger.debug("Found existing product for full update: {}", originalName);

            productMapper.updateEntityFromDTO(requestDTO, existingEntity);
            ProductEntity updatedEntity = productRepository.save(existingEntity);

            logger.info("Product fully updated successfully: {} (ID: {})",
                    updatedEntity.getProductName(), productId);

            ProductDTO resultDTO = productMapper.toDTO(updatedEntity);
            logger.debug("Mapped fully updated entity to DTO");

            return resultDTO;

        } catch (IllegalArgumentException e) {
            logger.error("Validation failed for product full update: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error during product full update for ID {}: {}", productId, e.getMessage(), e);
            throw new RuntimeException("Failed to update product: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteProduct(Long productId) {
        logger.info("Deleting product with ID: {}", productId);

        try {
            validateProductId(productId);

            // Check if product exists before deletion
            Optional<ProductEntity> existingProduct = productRepository.findById(productId);
            if (existingProduct.isEmpty()) {
                logger.warn("Cannot delete - product not found with ID: {}", productId);
                throw new IllegalArgumentException("Product not found with id: " + productId);
            }

            String productName = existingProduct.get().getProductName();
            productRepository.deleteById(productId);

            logger.info("Product deleted successfully: {} (ID: {})", productName, productId);

        } catch (IllegalArgumentException e) {
            logger.error("Validation failed for product deletion: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error during product deletion for ID {}: {}", productId, e.getMessage(), e);
            throw new RuntimeException("Failed to delete product: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDTO> getProductsByName(String name) {
        logger.info("Searching products by name: '{}'", name);

        try {
            if (!StringUtils.hasText(name)) {
                logger.warn("Empty or null product name provided for search");
                throw new IllegalArgumentException("Product name cannot be null or empty");
            }

            String searchName = name.trim();
            List<ProductEntity> entities = productRepository.findByProductNameContainingIgnoreCase(searchName);

            logger.info("Found {} products matching name: '{}'", entities.size(), searchName);

            List<ProductDTO> resultList = productMapper.toDTOList(entities);
            logger.debug("Mapped {} search results to DTOs", resultList.size());

            return resultList;

        } catch (IllegalArgumentException e) {
            logger.error("Invalid search parameter: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error searching products by name '{}': {}", name, e.getMessage(), e);
            throw new RuntimeException("Failed to search products: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long productId) {
        logger.info("Checking existence of product with ID: {}", productId);

        try {
            validateProductId(productId);

            boolean exists = productRepository.existsById(productId);
            logger.debug("Product existence check for ID {}: {}", productId, exists);

            return exists;

        } catch (IllegalArgumentException e) {
            logger.error("Invalid product ID for existence check: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error checking product existence for ID {}: {}", productId, e.getMessage(), e);
            throw new RuntimeException("Failed to check product existence: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public long getProductCount() {
        logger.info("Getting total product count");

        try {
            long count = productRepository.count();
            logger.info("Total products in database: {}", count);

            return count;

        } catch (Exception e) {
            logger.error("Unexpected error getting product count: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to get product count: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDTO> getProductsByCategory(String category) {
        logger.info("Fetching products by category: '{}'", category);

        try {
            if (!StringUtils.hasText(category)) {
                logger.warn("Empty or null category provided");
                throw new IllegalArgumentException("Category cannot be null or empty");
            }

            String searchCategory = category.trim();
            List<ProductEntity> entities = productRepository.findByProductCategoryAndNotDeleted(searchCategory);

            logger.info("Found {} products in category: '{}'", entities.size(), searchCategory);

            List<ProductDTO> resultList = productMapper.toDTOList(entities);
            logger.debug("Mapped {} category results to DTOs", resultList.size());

            return resultList;

        } catch (IllegalArgumentException e) {
            logger.error("Invalid category parameter: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error fetching products by category '{}': {}", category, e.getMessage(), e);
            throw new RuntimeException("Failed to fetch products by category: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDTO> getProductsByFoodType(String foodType) {
        logger.info("Fetching products by food type: '{}'", foodType);

        try {
            if (!StringUtils.hasText(foodType)) {
                logger.warn("Empty or null food type provided");
                throw new IllegalArgumentException("Food type cannot be null or empty");
            }

            String searchFoodType = foodType.trim();
            List<ProductEntity> entities = productRepository.findByProductFoodType(searchFoodType);

            logger.info("Found {} products with food type: '{}'", entities.size(), searchFoodType);

            List<ProductDTO> resultList = productMapper.toDTOList(entities);
            logger.debug("Mapped {} food type results to DTOs", resultList.size());

            return resultList;

        } catch (IllegalArgumentException e) {
            logger.error("Invalid food type parameter: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error fetching products by food type '{}': {}", foodType, e.getMessage(), e);
            throw new RuntimeException("Failed to fetch products by food type: " + e.getMessage(), e);
        }
    }

    // Private helper methods
    private void validateProductId(Long productId) {
        if (productId == null || productId <= 0) {
            logger.warn("Invalid product ID provided: {}", productId);
            throw new IllegalArgumentException("Product ID must be a positive number");
        }
    }

    private void validateProductRequest(ProductCreateRequestDTO requestDTO) {
        logger.debug("Validating product request for: {}",
                requestDTO != null ? requestDTO.getProductName() : "null");

        if (requestDTO == null) {
            throw new IllegalArgumentException("Product request cannot be null");
        }

        List<String> validationErrors = new ArrayList<>();

        if (!StringUtils.hasText(requestDTO.getProductName())) {
            validationErrors.add("Product name is required");
        }

        if (!StringUtils.hasText(requestDTO.getSkuNumber())) {
            validationErrors.add("SKU number is required");
        }

        if (!StringUtils.hasText(requestDTO.getProductCategory())) {
            validationErrors.add("Product category is required");
        }

        if (!StringUtils.hasText(requestDTO.getProductFoodType())) {
            validationErrors.add("Product food type is required");
        }

        if (requestDTO.getProductNewPrice() == null) {
            validationErrors.add("Product new price is required");
        }

        if (!StringUtils.hasText(requestDTO.getDefaultWeight())) {
            validationErrors.add("Default weight is required");
        }

        if (requestDTO.getWeights() != null && requestDTO.getWeightPrices() != null &&
                requestDTO.getWeights().size() != requestDTO.getWeightPrices().size()) {
            validationErrors.add("Weights and weightPrices must have the same size");
        }

        if (!validationErrors.isEmpty()) {
            String errorMessage = String.join(", ", validationErrors);
            logger.warn("Product validation failed: {}", errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }

        // Trim string fields
        trimStringFields(requestDTO);
        logger.debug("Product validation passed and fields trimmed");
    }

    private void trimStringFields(ProductCreateRequestDTO requestDTO) {
        if (requestDTO.getProductName() != null) {
            requestDTO.setProductName(requestDTO.getProductName().trim());
        }
        if (requestDTO.getSkuNumber() != null) {
            requestDTO.setSkuNumber(requestDTO.getSkuNumber().trim());
        }
        if (requestDTO.getProductCategory() != null) {
            requestDTO.setProductCategory(requestDTO.getProductCategory().trim());
        }
        if (requestDTO.getProductFoodType() != null) {
            requestDTO.setProductFoodType(requestDTO.getProductFoodType().trim());
        }
    }

    private void updateEntityFromPatchRequest(ProductEntity entity, ProductPatchRequestDTO patchRequest) {
        logger.debug("Applying patch updates to product: {}", entity.getProductName());

        int updateCount = 0;

        if (patchRequest.getProductName() != null) {
            entity.setProductName(patchRequest.getProductName().trim());
            updateCount++;
        }
        if (patchRequest.getProductCategory() != null) {
            entity.setProductCategory(patchRequest.getProductCategory().trim());
            updateCount++;
        }
        if (patchRequest.getProductFoodType() != null) {
            entity.setProductFoodType(patchRequest.getProductFoodType().trim());
            updateCount++;
        }
        if (patchRequest.getSkuNumber() != null) {
            entity.setSkuNumber(patchRequest.getSkuNumber().trim());
            updateCount++;
        }
        if (patchRequest.getNameOnCake() != null) {
            entity.setNameOnCake(patchRequest.getNameOnCake());
            updateCount++;
        }
        if (patchRequest.getOrderCount() != null) {
            entity.setOrderCount(patchRequest.getOrderCount());
            updateCount++;
        }
        if (patchRequest.getDescription() != null) {
            entity.setDescription(patchRequest.getDescription());
            updateCount++;
        }
        if (patchRequest.getProductIngredients() != null) {
            entity.setProductIngredients(patchRequest.getProductIngredients());
            updateCount++;
        }
        if (patchRequest.getAllergenInfo() != null) {
            entity.setAllergenInfo(patchRequest.getAllergenInfo());
            updateCount++;
        }
        if (patchRequest.getCareInstructions() != null) {
            entity.setCareInstructions(patchRequest.getCareInstructions());
            updateCount++;
        }
        if (patchRequest.getStorageInstructions() != null) {
            entity.setStorageInstructions(patchRequest.getStorageInstructions());
            updateCount++;
        }
        if (patchRequest.getShelfLife() != null) {
            entity.setShelfLife(patchRequest.getShelfLife());
            updateCount++;
        }
        if (patchRequest.getBestServed() != null) {
            entity.setBestServed(patchRequest.getBestServed());
            updateCount++;
        }
        if (patchRequest.getPreparationTime() != null) {
            entity.setPreparationTime(patchRequest.getPreparationTime());
            updateCount++;
        }
        if (patchRequest.getFlavor() != null) {
            entity.setFlavor(patchRequest.getFlavor());
            updateCount++;
        }
        if (patchRequest.getShape() != null) {
            entity.setShape(patchRequest.getShape());
            updateCount++;
        }
        if (patchRequest.getDefaultWeight() != null) {
            entity.setDefaultWeight(patchRequest.getDefaultWeight());
            updateCount++;
        }
        if (patchRequest.getLayers() != null) {
            entity.setLayers(patchRequest.getLayers());
            updateCount++;
        }
        if (patchRequest.getServes() != null) {
            entity.setServes(patchRequest.getServes());
            updateCount++;
        }
        if (patchRequest.getNote() != null) {
            entity.setNote(patchRequest.getNote());
            updateCount++;
        }
        if (patchRequest.getProductOldPrice() != null) {
            entity.setProductOldPrice(patchRequest.getProductOldPrice());
            updateCount++;
        }
        if (patchRequest.getProductNewPrice() != null) {
            entity.setProductNewPrice(patchRequest.getProductNewPrice());
            updateCount++;
        }
        if (patchRequest.getWeights() != null) {
            entity.setWeights(patchRequest.getWeights());
            updateCount++;
        }
        if (patchRequest.getWeightPrices() != null) {
            entity.setWeightPrices(patchRequest.getWeightPrices());
            updateCount++;
        }
        if (patchRequest.getFeatures() != null) {
            entity.setFeatures(patchRequest.getFeatures());
            updateCount++;
        }
        if (patchRequest.getRatings() != null) {
            entity.setRatings(patchRequest.getRatings());
            updateCount++;
        }
        if (patchRequest.getReviews() != null) {
            entity.setReviews(patchRequest.getReviews());
            updateCount++;
        }
        if (patchRequest.getProductDiscount() != null) {
            entity.setProductDiscount(patchRequest.getProductDiscount());
            updateCount++;
        }
        if (patchRequest.getDeliveryTime() != null) {
            entity.setDeliveryTime(patchRequest.getDeliveryTime());
            updateCount++;
        }
        if (patchRequest.getFreeDeliveryThreshold() != null) {
            entity.setFreeDeliveryThreshold(patchRequest.getFreeDeliveryThreshold());
            updateCount++;
        }

        // Handle image updates
        if (patchRequest.isProductImagePresent()) {
            entity.setProductImage(patchRequest.getProductImage());
            updateCount++;
        }

        // Handle sub-image updates
        if (patchRequest.isProductSubImagesPresent()) {
            entity.setProductSubImages(patchRequest.getProductSubImages());
            updateCount++;
        } else if (patchRequest.getExistingProductSubImages() != null) {
            updateSubImagesFromIndices(entity, patchRequest.getExistingProductSubImages());
            updateCount++;
        }

        logger.debug("Applied {} field updates to product", updateCount);
    }

    private void updateSubImagesFromIndices(ProductEntity entity, List<String> subImageIndices) {
        List<byte[]> currentSubImages = entity.getProductSubImages() != null ?
                entity.getProductSubImages() : new ArrayList<>();
        List<byte[]> updatedSubImages = new ArrayList<>();

        for (String indexStr : subImageIndices) {
            try {
                int index = Integer.parseInt(indexStr);
                if (index >= 0 && index < currentSubImages.size()) {
                    updatedSubImages.add(currentSubImages.get(index));
                } else {
                    logger.warn("Invalid sub-image index: {} for product: {}", indexStr, entity.getProductName());
                    throw new IllegalArgumentException("Invalid sub-image index: " + indexStr);
                }
            } catch (NumberFormatException e) {
                logger.error("Invalid sub-image index format: {} for product: {}", indexStr, entity.getProductName());
                throw new IllegalArgumentException("Invalid sub-image index format: " + indexStr);
            }
        }
        entity.setProductSubImages(updatedSubImages);
        logger.debug("Updated sub-images using {} valid indices", updatedSubImages.size());
    }
}