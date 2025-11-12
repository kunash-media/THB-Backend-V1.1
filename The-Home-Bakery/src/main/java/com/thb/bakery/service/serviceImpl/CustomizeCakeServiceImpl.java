package com.thb.bakery.service.serviceImpl;

import com.thb.bakery.dto.request.CustomizeCakeRequestDTO;
import com.thb.bakery.dto.response.CustomizeCakeResponseDTO;
import com.thb.bakery.entity.CustomizeCakeEntity;
import com.thb.bakery.repository.CustomizeCakeRepository;
import com.thb.bakery.service.CustomizeCakeService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CustomizeCakeServiceImpl implements CustomizeCakeService {

    private static final Logger logger = LoggerFactory.getLogger(CustomizeCakeServiceImpl.class);

    @Autowired
    private CustomizeCakeRepository customizeCakeRepository;

    @Value("${file.upload-dir:uploads}")
    private String uploadDir;

    @Override
    public CustomizeCakeResponseDTO createProduct(CustomizeCakeRequestDTO requestDTO) {
        logger.info("Creating new customize cake product with title: {}", requestDTO.getTitle());

        try {
            validateRequest(requestDTO);

            if (customizeCakeRepository.existsByTitleAndIsActiveTrue(requestDTO.getTitle())) {
                throw new IllegalArgumentException("Product with title '" + requestDTO.getTitle() + "' already exists");
            }

            CustomizeCakeEntity entity = new CustomizeCakeEntity();
            mapRequestToEntity(requestDTO, entity);

            if (requestDTO.getProductImage() != null && !requestDTO.getProductImage().isEmpty()) {
                String imageUrl = saveImage(requestDTO.getProductImage());
//                entity.setImageUrl(imageUrl);
                entity.setProductImage(requestDTO.getProductImage().getBytes());
            }

            CustomizeCakeEntity savedEntity = customizeCakeRepository.save(entity);
            logger.info("Successfully created product with ID: {}", savedEntity.getId());

            return mapEntityToResponse(savedEntity);
        } catch (Exception e) {
            logger.error("Error creating product: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create product: " + e.getMessage());
        }
    }

    @Override
    public CustomizeCakeResponseDTO updateProduct(Long id, CustomizeCakeRequestDTO requestDTO) {
        logger.info("Updating product with ID: {}", id);

        try {
            validateRequest(requestDTO);

            CustomizeCakeEntity entity = customizeCakeRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + id));

            mapRequestToEntity(requestDTO, entity);

            if (requestDTO.getProductImage() != null && !requestDTO.getProductImage().isEmpty()) {
                String imageUrl = saveImage(requestDTO.getProductImage());
//                entity.setImageUrl(imageUrl);
                entity.setProductImage(requestDTO.getProductImage().getBytes());
            }

            CustomizeCakeEntity updatedEntity = customizeCakeRepository.save(entity);
            logger.info("Successfully updated product with ID: {}", id);

            return mapEntityToResponse(updatedEntity);
        } catch (Exception e) {
            logger.error("Error updating product with ID {}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Failed to update product: " + e.getMessage());
        }
    }

    @Override
    public CustomizeCakeResponseDTO getProductById(Long id) {
        logger.debug("Fetching product with ID: {}", id);

        CustomizeCakeEntity entity = customizeCakeRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Active product not found with ID: " + id));

        return mapEntityToResponse(entity);
    }

    @Override
    public List<CustomizeCakeResponseDTO> getAllProducts() {
        logger.debug("Fetching all active products");

        List<CustomizeCakeEntity> entities = customizeCakeRepository.findByIsActiveTrue();
        return entities.stream()
                .map(this::mapEntityToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Page<CustomizeCakeResponseDTO> getAllProducts(Pageable pageable) {
        logger.debug("Fetching products with pagination");

        Page<CustomizeCakeEntity> entityPage = customizeCakeRepository.findByIsActiveTrue(pageable);
        return entityPage.map(this::mapEntityToResponse);
    }

    @Override
    public List<CustomizeCakeResponseDTO> searchProducts(String query) {
        logger.debug("Searching products with query: '{}'", query);

        List<CustomizeCakeEntity> entities = customizeCakeRepository.searchActiveProducts(query);
        return entities.stream()
                .map(this::mapEntityToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<CustomizeCakeResponseDTO> getProductsByCategory(String category) {
        logger.debug("Fetching products by category: '{}'", category);

        List<CustomizeCakeEntity> entities = customizeCakeRepository.findByCategoryAndIsActiveTrue(category);
        return entities.stream()
                .map(this::mapEntityToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<CustomizeCakeResponseDTO> getProductsBySubcategory(String subcategory) {
        logger.debug("Fetching products by subcategory: '{}'", subcategory);

        List<CustomizeCakeEntity> entities = customizeCakeRepository.findBySubcategoryAndIsActiveTrue(subcategory);
        return entities.stream()
                .map(this::mapEntityToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<CustomizeCakeResponseDTO> getProductsByCategoryAndSubcategory(String category, String subcategory) {
        logger.debug("Fetching products by category: '{}' and subcategory: '{}'", category, subcategory);

        List<CustomizeCakeEntity> entities = customizeCakeRepository.findByCategoryAndSubcategoryAndIsActiveTrue(category, subcategory);
        return entities.stream()
                .map(this::mapEntityToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<CustomizeCakeResponseDTO> getProductsWithMinimumDiscount(BigDecimal minDiscount) {
        logger.debug("Fetching products with minimum discount: {}", minDiscount);

        List<CustomizeCakeEntity> entities = customizeCakeRepository.findByDiscountGreaterThanEqual(minDiscount);
        return entities.stream()
                .map(this::mapEntityToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteProduct(Long id) {
        logger.info("Soft deleting product with ID: {}", id);

        CustomizeCakeEntity entity = customizeCakeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + id));

        entity.setIsActive(false);
        customizeCakeRepository.save(entity);
        logger.info("Successfully soft deleted product with ID: {}", id);
    }

    @Override
    public void permanentDeleteProduct(Long id) {
        logger.info("Permanently deleting product with ID: {}", id);

        if (!customizeCakeRepository.existsById(id)) {
            throw new EntityNotFoundException("Product not found with ID: " + id);
        }

        customizeCakeRepository.deleteById(id);
        logger.info("Successfully permanently deleted product with ID: {}", id);
    }

    @Override
    public CustomizeCakeResponseDTO toggleProductStatus(Long id) {
        logger.info("Toggling status for product with ID: {}", id);

        CustomizeCakeEntity entity = customizeCakeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + id));

        entity.setIsActive(!entity.getIsActive());
        CustomizeCakeEntity updatedEntity = customizeCakeRepository.save(entity);

        return mapEntityToResponse(updatedEntity);
    }

    @Override
    public long getActiveProductsCount() {
        return customizeCakeRepository.countByIsActiveTrue();
    }

    @Override
    public String saveImage(MultipartFile imageFile) {
        logger.info("Saving image file: {}", imageFile.getOriginalFilename());

        try {
            if (imageFile.isEmpty()) {
                throw new IllegalArgumentException("Image file is empty");
            }

            if (imageFile.getSize() > 5 * 1024 * 1024) {
                throw new IllegalArgumentException("Image size must be less than 5MB");
            }

            String contentType = imageFile.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                throw new IllegalArgumentException("Only image files are allowed");
            }

            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String originalFilename = imageFile.getOriginalFilename();
            String fileExtension = originalFilename != null ?
                    originalFilename.substring(originalFilename.lastIndexOf(".")) : ".jpg";
            String filename = UUID.randomUUID().toString() + fileExtension;

            Path filePath = uploadPath.resolve(filename);
            Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            String imageUrl = "/uploads/" + filename;
            logger.info("Successfully saved image: {}", imageUrl);

            return imageUrl;
        } catch (IOException e) {
            logger.error("Error saving image file: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to save image: " + e.getMessage());
        }
    }

    private void validateRequest(CustomizeCakeRequestDTO requestDTO) {
        if (requestDTO.getTitle() == null || requestDTO.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Product title is required");
        }

        if (requestDTO.getTitle().length() < 3 || requestDTO.getTitle().length() > 200) {
            throw new IllegalArgumentException("Title must be between 3 and 200 characters");
        }

        if (requestDTO.getWeights() == null || requestDTO.getWeights().isEmpty()) {
            throw new IllegalArgumentException("At least one weight option is required");
        }

        if (requestDTO.getOldPrices() == null || requestDTO.getOldPrices().size() != requestDTO.getWeights().size()) {
            throw new IllegalArgumentException("Number of old prices must match number of weights");
        }

        if (requestDTO.getNewPrices() == null || requestDTO.getNewPrices().size() != requestDTO.getWeights().size()) {
            throw new IllegalArgumentException("Number of new prices must match number of weights");
        }

        for (int i = 0; i < requestDTO.getWeights().size(); i++) {
            BigDecimal oldPrice = requestDTO.getOldPrices().get(i);
            BigDecimal newPrice = requestDTO.getNewPrices().get(i);

            if (oldPrice == null || oldPrice.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Old price must be greater than 0 for weight: " + requestDTO.getWeights().get(i));
            }

            if (newPrice == null || newPrice.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("New price must be greater than 0 for weight: " + requestDTO.getWeights().get(i));
            }

            if (newPrice.compareTo(oldPrice) > 0) {
                throw new IllegalArgumentException("New price cannot be greater than old price for weight: " + requestDTO.getWeights().get(i));
            }
        }

        if (requestDTO.getDiscount() != null) {
            if (requestDTO.getDiscount().compareTo(BigDecimal.ZERO) < 0 ||
                    requestDTO.getDiscount().compareTo(new BigDecimal("100")) > 0) {
                throw new IllegalArgumentException("Discount must be between 0 and 100");
            }
        }
    }

    private void mapRequestToEntity(CustomizeCakeRequestDTO requestDTO, CustomizeCakeEntity entity) {
        entity.setTitle(requestDTO.getTitle());
        entity.setCategory(requestDTO.getCategory());
        entity.setSubcategory(requestDTO.getSubcategory());
        entity.setDiscount(requestDTO.getDiscount());
        entity.setWeights(requestDTO.getWeights());
        entity.setOldPrices(requestDTO.getOldPrices());
        entity.setNewPrices(requestDTO.getNewPrices());

        if (requestDTO.getIsActive() != null) {
            entity.setIsActive(requestDTO.getIsActive());
        } else {
            entity.setIsActive(true);
        }
    }

    private CustomizeCakeResponseDTO mapEntityToResponse(CustomizeCakeEntity entity) {
        CustomizeCakeResponseDTO responseDTO = new CustomizeCakeResponseDTO();
        responseDTO.setId(entity.getId());
        responseDTO.setProductImage(entity.getProductImage());
        responseDTO.setTitle(entity.getTitle());
        responseDTO.setCategory(entity.getCategory());
        responseDTO.setSubcategory(entity.getSubcategory());
        responseDTO.setDiscount(entity.getDiscount());
        responseDTO.setWeights(entity.getWeights());
        responseDTO.setOldPrices(entity.getOldPrices());
        responseDTO.setNewPrices(entity.getNewPrices());
        responseDTO.setIsActive(entity.getIsActive());
        responseDTO.setCreatedAt(entity.getCreatedAt());
        responseDTO.setUpdatedAt(entity.getUpdatedAt());

        return responseDTO;
    }

    // Add these methods to your CustomizeCakeServiceImpl.java

    @Override
    public List<CustomizeCakeResponseDTO> getAllDeletedProducts() {
        logger.debug("Fetching all deleted (inactive) products");

        List<CustomizeCakeEntity> entities = customizeCakeRepository.findByIsActiveFalse();
        return entities.stream()
                .map(this::mapEntityToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<CustomizeCakeResponseDTO> getAllProductsIncludingDeleted() {
        logger.debug("Fetching all products including deleted");

        List<CustomizeCakeEntity> entities = customizeCakeRepository.findAllIncludingInactive();
        return entities.stream()
                .map(this::mapEntityToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CustomizeCakeResponseDTO restoreProduct(Long id) {
        logger.info("Restoring deleted product with ID: {}", id);

        CustomizeCakeEntity entity = customizeCakeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + id));

        if (entity.getIsActive()) {
            throw new IllegalStateException("Product is already active");
        }

        entity.setIsActive(true);
        CustomizeCakeEntity restoredEntity = customizeCakeRepository.save(entity);

        logger.info("Successfully restored product with ID: {}", id);
        return mapEntityToResponse(restoredEntity);
    }
}
