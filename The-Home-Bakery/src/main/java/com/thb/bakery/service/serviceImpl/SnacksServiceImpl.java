package com.thb.bakery.service.serviceImpl;

import com.thb.bakery.dto.request.SnacksDTO;
import com.thb.bakery.dto.response.SnackResponseDTO;
import com.thb.bakery.entity.SnacksEntity;
import com.thb.bakery.repository.SnacksRepository;
import com.thb.bakery.service.SnacksService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SnacksServiceImpl implements SnacksService {

    private static final Logger logger = LoggerFactory.getLogger(SnacksServiceImpl.class);

    @Autowired
    private SnacksRepository repo;

    @Autowired
    private ObjectMapper mapper;

    // ===================================================================
    // CREATE
    // ===================================================================
    @Override
    @Transactional
    public SnackResponseDTO create(String productData, MultipartFile productMainImage) {
        logger.info("Creating new snack");

        SnacksDTO dto = parse(productData);
        byte[] image = getImage(productMainImage);

        // Use setters instead of constructor (avoids field order issues)
        SnacksEntity entity = new SnacksEntity();
        entity.setProductName(dto.getProductName());
        entity.setProductCategory(dto.getProductCategory());
        entity.setProductSubcategory(dto.getProductSubcategory());
        entity.setSkuNumber(dto.getSkuNumber());
        entity.setProductOldPrice(dto.getProductOldPrice() != null ? dto.getProductOldPrice() : 0.0);
        entity.setProductNewPrice(dto.getProductNewPrice() != null ? dto.getProductNewPrice() : 0.0);
        entity.setRatings(dto.getRatings() != null ? dto.getRatings() : 0.0);
        entity.setProductDiscount(dto.getProductDiscount());
        entity.setProductMainImage(image);

        SnacksEntity saved = repo.save(entity);
        return toResponseDTO(saved);
    }

    // ===================================================================
    // READ
    // ===================================================================
    @Override
    public Page<SnackResponseDTO> getAll(Pageable p) {
        return repo.findAll(p).map(this::toResponseDTO);
    }

    @Override
    public SnackResponseDTO getById(Long id) {
        return repo.findById(id).map(this::toResponseDTO).orElseThrow(this::notFound);
    }

    @Override
    public Page<SnackResponseDTO> getByCategory(String category, Pageable p) {
        return repo.findByProductCategory(category, p).map(this::toResponseDTO);
    }

    @Override
    public Page<SnackResponseDTO> getBySubcategory(String subcategory, Pageable p) {
        return repo.findByProductSubcategory(subcategory, p).map(this::toResponseDTO);
    }

    @Override
    public List<SnackResponseDTO> searchByName(String name) {
        return repo.findByProductNameContainingIgnoreCase(name, Pageable.unpaged())
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    // ===================================================================
    // UPDATE (Full)
    // ===================================================================
    @Override
    @Transactional
    public SnackResponseDTO update(Long id, String productData, MultipartFile productMainImage) {
        SnacksEntity entity = getEntity(id);
        SnacksDTO dto = parse(productData);

        entity.setProductName(dto.getProductName());
        entity.setProductCategory(dto.getProductCategory());
        entity.setProductSubcategory(dto.getProductSubcategory());
        entity.setSkuNumber(dto.getSkuNumber());
        entity.setProductOldPrice(dto.getProductOldPrice() != null ? dto.getProductOldPrice() : entity.getProductOldPrice());
        entity.setProductNewPrice(dto.getProductNewPrice() != null ? dto.getProductNewPrice() : entity.getProductNewPrice());
        entity.setRatings(dto.getRatings() != null ? dto.getRatings() : entity.getRatings());
        entity.setProductDiscount(dto.getProductDiscount());

        if (productMainImage != null && !productMainImage.isEmpty()) {
            entity.setProductMainImage(getImage(productMainImage));
        }

        return toResponseDTO(repo.save(entity));
    }

    // ===================================================================
    // PATCH (Partial Update)
    // ===================================================================
    @Override
    @Transactional
    public SnackResponseDTO patch(Long id, String productData, MultipartFile productMainImage) {
        SnacksEntity entity = getEntity(id);

        if (productData != null && !productData.trim().isEmpty()) {
            SnacksDTO dto = parse(productData);

            Optional.ofNullable(dto.getProductName()).ifPresent(entity::setProductName);
            Optional.ofNullable(dto.getProductCategory()).ifPresent(entity::setProductCategory);
            Optional.ofNullable(dto.getProductSubcategory()).ifPresent(entity::setProductSubcategory);
            Optional.ofNullable(dto.getSkuNumber()).ifPresent(entity::setSkuNumber);
            Optional.ofNullable(dto.getProductOldPrice()).ifPresent(entity::setProductOldPrice);
            Optional.ofNullable(dto.getProductNewPrice()).ifPresent(entity::setProductNewPrice);
            Optional.ofNullable(dto.getRatings()).ifPresent(entity::setRatings);
            Optional.ofNullable(dto.getProductDiscount()).ifPresent(entity::setProductDiscount);
        }

        if (productMainImage != null && !productMainImage.isEmpty()) {
            entity.setProductMainImage(getImage(productMainImage));
        }

        return toResponseDTO(repo.save(entity));
    }

    // ===================================================================
    // DELETE
    // ===================================================================
    @Override
    @Transactional
    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw notFound();
        }
        repo.deleteById(id);
    }

    // ===================================================================
    // HELPER METHODS
    // ===================================================================
    private SnacksEntity getEntity(Long id) {
        return repo.findById(id).orElseThrow(this::notFound);
    }

    private ResponseStatusException notFound() {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "Snack not found");
    }

    private SnacksDTO parse(String json) {
        try {
            return mapper.readValue(json, SnacksDTO.class);
        } catch (Exception ex) {
            logger.error("Failed to parse snack JSON: {}", json, ex);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid product data format");
        }
    }

    private byte[] getImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }
        try {
            return file.getBytes();
        } catch (IOException ex) {
            logger.error("Failed to read image file", ex);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid image file");
        }
    }

    // ===================================================================
    // DTO MAPPING
    // ===================================================================
    private SnackResponseDTO toResponseDTO(SnacksEntity e) {
        return new SnackResponseDTO(
                e.getSnackId(),
                e.getProductName(),
                e.getProductCategory(),
                e.getProductSubcategory(),
                e.getSkuNumber(),
                e.getProductOldPrice() != 0.0 ? e.getProductOldPrice() : null,
                e.getProductNewPrice() != 0.0 ? e.getProductNewPrice() : null,
                e.getRatings() != 0.0 ? e.getRatings() : null,
                e.getProductDiscount(),
                "/api/v1/snacks/" + e.getSnackId() + "/image"
        );
    }
}