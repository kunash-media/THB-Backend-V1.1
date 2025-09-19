package com.bakery.The.Home.Bakery.service.serviceImpl;

import com.bakery.The.Home.Bakery.dto.request.ProductCreateRequestDTO;
import com.bakery.The.Home.Bakery.dto.request.ProductDTO;
import com.bakery.The.Home.Bakery.dto.request.ProductPatchRequestDTO;
import com.bakery.The.Home.Bakery.entity.ProductEntity;
import com.bakery.The.Home.Bakery.mapper.ProductMapper;
import com.bakery.The.Home.Bakery.repository.ProductRepository;
import com.bakery.The.Home.Bakery.service.ProductService;
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

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    @Override
    public ProductDTO createProduct(ProductCreateRequestDTO requestDTO) {
        validateProductRequest(requestDTO);

        Optional<ProductEntity> existProduct = productRepository.findBySkuNumber(requestDTO.getSkuNumber());
        if (existProduct.isPresent()) {
            throw new IllegalArgumentException("Product with this SKU number already exists");
        }

        ProductEntity productEntity = productMapper.toEntity(requestDTO);
        ProductEntity savedEntity = productRepository.save(productEntity);
        return productMapper.toDTO(savedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDTO> getAllProducts() {
        List<ProductEntity> entities = productRepository.findAll();
        return productMapper.toDTOList(entities);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductDTO> getAllProducts(Pageable pageable) {
        Page<ProductEntity> entities = productRepository.findAll(pageable);
        return entities.map(productMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDTO getProductById(Long productId) {
        validateProductId(productId);
        ProductEntity entity = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + productId));
        return productMapper.toDTO(entity);
    }

    @Override
    public ProductDTO patchProduct(Long productId, ProductPatchRequestDTO patchRequest) {
        validateProductId(productId);

        ProductEntity existingEntity = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + productId));

        if (patchRequest.getWeights() != null && patchRequest.getWeightPrices() != null &&
                patchRequest.getWeights().size() != patchRequest.getWeightPrices().size()) {
            throw new IllegalArgumentException("Weights and weightPrices must have the same size");
        }

        // Update fields if present
        if (patchRequest.getProductName() != null) {
            existingEntity.setProductName(patchRequest.getProductName());
        }
        if (patchRequest.getProductCategory() != null) {
            existingEntity.setProductCategory(patchRequest.getProductCategory());
        }
        if (patchRequest.getProductFoodType() != null) {
            existingEntity.setProductFoodType(patchRequest.getProductFoodType());
        }
        if (patchRequest.getSkuNumber() != null) {
            existingEntity.setSkuNumber(patchRequest.getSkuNumber());
        }
        if (patchRequest.getNameOnCake() != null) {
            existingEntity.setNameOnCake(patchRequest.getNameOnCake());
        }
        if (patchRequest.getOrderCount() != null) {
            existingEntity.setOrderCount(patchRequest.getOrderCount());
        }
        if (patchRequest.getDescription() != null) {
            existingEntity.setDescription(patchRequest.getDescription());
        }
        if (patchRequest.getProductIngredients() != null) {
            existingEntity.setProductIngredients(patchRequest.getProductIngredients());
        }
        if (patchRequest.getAllergenInfo() != null) {
            existingEntity.setAllergenInfo(patchRequest.getAllergenInfo());
        }
        if (patchRequest.getCareInstructions() != null) {
            existingEntity.setCareInstructions(patchRequest.getCareInstructions());
        }
        if (patchRequest.getStorageInstructions() != null) {
            existingEntity.setStorageInstructions(patchRequest.getStorageInstructions());
        }
        if (patchRequest.getShelfLife() != null) {
            existingEntity.setShelfLife(patchRequest.getShelfLife());
        }
        if (patchRequest.getBestServed() != null) {
            existingEntity.setBestServed(patchRequest.getBestServed());
        }
        if (patchRequest.getPreparationTime() != null) {
            existingEntity.setPreparationTime(patchRequest.getPreparationTime());
        }
        if (patchRequest.getFlavor() != null) {
            existingEntity.setFlavor(patchRequest.getFlavor());
        }
        if (patchRequest.getShape() != null) {
            existingEntity.setShape(patchRequest.getShape());
        }
        if (patchRequest.getDefaultWeight() != null) {
            existingEntity.setDefaultWeight(patchRequest.getDefaultWeight());
        }
        if (patchRequest.getLayers() != null) {
            existingEntity.setLayers(patchRequest.getLayers());
        }
        if (patchRequest.getServes() != null) {
            existingEntity.setServes(patchRequest.getServes());
        }
        if (patchRequest.getNote() != null) {
            existingEntity.setNote(patchRequest.getNote());
        }
        if (patchRequest.getProductOldPrice() != null) {
            existingEntity.setProductOldPrice(patchRequest.getProductOldPrice());
        }
        if (patchRequest.getProductNewPrice() != null) {
            existingEntity.setProductNewPrice(patchRequest.getProductNewPrice());
        }
        if (patchRequest.getWeights() != null) {
            existingEntity.setWeights(patchRequest.getWeights());
        }
        if (patchRequest.getWeightPrices() != null) {
            existingEntity.setWeightPrices(patchRequest.getWeightPrices());
        }
        if (patchRequest.getFeatures() != null) {
            existingEntity.setFeatures(patchRequest.getFeatures());
        }
        if (patchRequest.getRatings() != null) {
            existingEntity.setRatings(patchRequest.getRatings());
        }
        if (patchRequest.getReviews() != null) {
            existingEntity.setReviews(patchRequest.getReviews());
        }
        if (patchRequest.getProductDiscount() != null) {
            existingEntity.setProductDiscount(patchRequest.getProductDiscount());
        }
        if (patchRequest.getDeliveryTime() != null) {
            existingEntity.setDeliveryTime(patchRequest.getDeliveryTime());
        }
        if (patchRequest.getFreeDeliveryThreshold() != null) {
            existingEntity.setFreeDeliveryThreshold(patchRequest.getFreeDeliveryThreshold());
        }

        // Handle image updates
        if (patchRequest.isProductImagePresent()) {
            existingEntity.setProductImage(patchRequest.getProductImage());
        }

        // Handle sub-image updates
        if (patchRequest.isProductSubImagesPresent()) {
            existingEntity.setProductSubImages(patchRequest.getProductSubImages());
        } else if (patchRequest.getExistingProductSubImages() != null) {
            List<String> subImageIndices = patchRequest.getExistingProductSubImages();
            List<byte[]> currentSubImages = existingEntity.getProductSubImages() != null ? existingEntity.getProductSubImages() : new ArrayList<>();
            List<byte[]> updatedSubImages = new ArrayList<>();

            for (String indexStr : subImageIndices) {
                try {
                    int index = Integer.parseInt(indexStr);
                    if (index >= 0 && index < currentSubImages.size()) {
                        updatedSubImages.add(currentSubImages.get(index));
                    } else {
                        throw new IllegalArgumentException("Invalid sub-image index: " + indexStr);
                    }
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid sub-image index format: " + indexStr);
                }
            }
            existingEntity.setProductSubImages(updatedSubImages);
        }

        ProductEntity updatedEntity = productRepository.save(existingEntity);
        return productMapper.toDTO(updatedEntity);
    }

    @Override
    public ProductDTO updateProduct(Long productId, ProductCreateRequestDTO requestDTO) {
        validateProductId(productId);
        validateProductRequest(requestDTO);

        ProductEntity existingEntity = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + productId));

        productMapper.updateEntityFromDTO(requestDTO, existingEntity);
        ProductEntity updatedEntity = productRepository.save(existingEntity);
        return productMapper.toDTO(updatedEntity);
    }

    @Override
    public void deleteProduct(Long productId) {
        validateProductId(productId);
        productRepository.deleteById(productId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDTO> getProductsByName(String name) {
        if (!StringUtils.hasText(name)) {
            throw new IllegalArgumentException("Product name cannot be null or empty");
        }
        List<ProductEntity> entities = productRepository.findByProductNameContainingIgnoreCase(name.trim());
        return productMapper.toDTOList(entities);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long productId) {
        validateProductId(productId);
        return productRepository.existsById(productId);
    }

    @Override
    @Transactional(readOnly = true)
    public long getProductCount() {
        return productRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDTO> getProductsByCategory(String category) {
        if (!StringUtils.hasText(category)) {
            throw new IllegalArgumentException("Category cannot be null or empty");
        }
        List<ProductEntity> entities = productRepository.findByProductCategoryAndNotDeleted(category.trim());
        return productMapper.toDTOList(entities);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDTO> getProductsByFoodType(String foodType) {
        if (!StringUtils.hasText(foodType)) {
            throw new IllegalArgumentException("Food type cannot be null or empty");
        }
        List<ProductEntity> entities = productRepository.findByProductFoodType(foodType.trim());
        return productMapper.toDTOList(entities);
    }

    private void validateProductId(Long productId) {
        if (productId == null || productId <= 0) {
            throw new IllegalArgumentException("Product ID must be a positive number");
        }
    }

    private void validateProductRequest(ProductCreateRequestDTO requestDTO) {
        if (requestDTO == null) {
            throw new IllegalArgumentException("Product request cannot be null");
        }

        if (!StringUtils.hasText(requestDTO.getProductName())) {
            throw new IllegalArgumentException("Product name is required");
        }

        if (!StringUtils.hasText(requestDTO.getSkuNumber())) {
            throw new IllegalArgumentException("SKU number is required");
        }

        if (!StringUtils.hasText(requestDTO.getProductCategory())) {
            throw new IllegalArgumentException("Product category is required");
        }

        if (!StringUtils.hasText(requestDTO.getProductFoodType())) {
            throw new IllegalArgumentException("Product food type is required");
        }

        if (requestDTO.getProductNewPrice() == null) {
            throw new IllegalArgumentException("Product new price is required");
        }

        if (requestDTO.getDefaultWeight() == null) {
            throw new IllegalArgumentException("Default weight is required");
        }

        if (requestDTO.getWeights() != null && requestDTO.getWeightPrices() != null &&
                requestDTO.getWeights().size() != requestDTO.getWeightPrices().size()) {
            throw new IllegalArgumentException("Weights and weightPrices must have the same size");
        }

        // Trim string fields
        requestDTO.setProductName(requestDTO.getProductName() != null ? requestDTO.getProductName().trim() : null);
        requestDTO.setSkuNumber(requestDTO.getSkuNumber() != null ? requestDTO.getSkuNumber().trim() : null);
        requestDTO.setProductCategory(requestDTO.getProductCategory() != null ? requestDTO.getProductCategory().trim() : null);
        requestDTO.setProductFoodType(requestDTO.getProductFoodType() != null ? requestDTO.getProductFoodType().trim() : null);
    }
}