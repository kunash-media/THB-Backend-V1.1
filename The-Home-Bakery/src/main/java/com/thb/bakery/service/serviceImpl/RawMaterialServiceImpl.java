package com.thb.bakery.service.serviceImpl;

import com.thb.bakery.dto.request.RawMaterialRequestDto;
import com.thb.bakery.dto.response.RawMaterialResponseDto;
import com.thb.bakery.entity.MaterialHistoryEntity;
import com.thb.bakery.entity.RawMaterialEntity;
import com.thb.bakery.repository.MaterialHistoryRepository;
import com.thb.bakery.repository.RawMaterialRepository;
import com.thb.bakery.service.RawMaterialService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RawMaterialServiceImpl implements RawMaterialService {

    private final RawMaterialRepository rawMaterialRepository;
    private final MaterialHistoryRepository materialHistoryRepository;

    private static final Logger logger = LoggerFactory.getLogger(RawMaterialServiceImpl.class);

    public RawMaterialServiceImpl(RawMaterialRepository rawMaterialRepository, MaterialHistoryRepository materialHistoryRepository) {
        this.rawMaterialRepository = rawMaterialRepository;
        this.materialHistoryRepository = materialHistoryRepository;
    }

    @Override
    @Transactional
    public RawMaterialResponseDto createRawMaterial(RawMaterialRequestDto requestDto) {
        logger.info("Creating new raw material: {}", requestDto.getName());

        RawMaterialEntity entity = new RawMaterialEntity();
        mapRequestToEntity(requestDto, entity);
        entity.setLastUpdated(LocalDate.now());

        RawMaterialEntity savedEntity = rawMaterialRepository.save(entity);
        logger.info("Raw material created successfully with ID: {}", savedEntity.getId());

        createHistory(savedEntity, "Purchase Order Received", requestDto.getCurrentStock(), 0.0);

        return mapEntityToResponse(savedEntity);
    }

    @Override
    @Transactional
    public RawMaterialResponseDto updateRawMaterial(Long id, RawMaterialRequestDto requestDto) {
        logger.info("Updating raw material with ID: {}", id);

        RawMaterialEntity entity = rawMaterialRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Raw material not found with ID: {}", id);
                    return new RuntimeException("Raw material not found with ID: " + id);
                });

        Double previousStock = entity.getCurrentStock();
        mapRequestToEntity(requestDto, entity);
        entity.setId(id);
        entity.setLastUpdated(LocalDate.now());

        RawMaterialEntity updatedEntity = rawMaterialRepository.save(entity);
        logger.info("Raw material updated successfully with ID: {}", id);

        if (!previousStock.equals(requestDto.getCurrentStock())) {
            Double quantity = requestDto.getCurrentStock() - previousStock;
            String action = quantity > 0 ? "Stock Added" : "Stock Reduced";
            createHistory(updatedEntity, action, Math.abs(quantity), previousStock);
        }

        return mapEntityToResponse(updatedEntity);
    }

    @Override
    public RawMaterialResponseDto getRawMaterialById(Long id) {
        logger.info("Fetching raw material with ID: {}", id);

        RawMaterialEntity entity = rawMaterialRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Raw material not found with ID: {}", id);
                    return new RuntimeException("Raw material not found with ID: " + id);
                });

        logger.info("Raw material fetched successfully: {}", entity.getName());
        return mapEntityToResponse(entity);
    }

    @Override
    public List<RawMaterialResponseDto> getAllRawMaterials() {
        logger.info("Fetching all raw materials");

        List<RawMaterialEntity> entities = rawMaterialRepository.findAll();
        logger.info("Total raw materials fetched: {}", entities.size());

        return entities.stream()
                .map(this::mapEntityToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteRawMaterial(Long id) {
        logger.info("Deleting raw material with ID: {}", id);

        if (!rawMaterialRepository.existsById(id)) {
            logger.error("Raw material not found with ID: {}", id);
            throw new RuntimeException("Raw material not found with ID: " + id);
        }

        rawMaterialRepository.deleteById(id);
        logger.info("Raw material deleted successfully with ID: {}", id);
    }

    @Override
    public List<RawMaterialResponseDto> getRawMaterialsByCategory(String category) {
        logger.info("Fetching raw materials by category: {}", category);

        List<RawMaterialEntity> entities = rawMaterialRepository.findByCategory(category);
        logger.info("Total raw materials found for category {}: {}", category, entities.size());

        return entities.stream()
                .map(this::mapEntityToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<RawMaterialResponseDto> getRawMaterialsByStatus(String status) {
        logger.info("Fetching raw materials by status: {}", status);

        List<RawMaterialEntity> entities = rawMaterialRepository.findByStatus(status);
        logger.info("Total raw materials found for status {}: {}", status, entities.size());

        return entities.stream()
                .map(this::mapEntityToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<RawMaterialResponseDto> getRawMaterialsByVendor(String vendorName) {
        logger.info("Fetching raw materials by vendor: {}", vendorName);

        List<RawMaterialEntity> entities = rawMaterialRepository.findByVendorName(vendorName);
        logger.info("Total raw materials found for vendor {}: {}", vendorName, entities.size());

        return entities.stream()
                .map(this::mapEntityToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialHistoryEntity> getMaterialHistory(Long materialId) {
        logger.info("Fetching history for material ID: {}", materialId);

        List<MaterialHistoryEntity> history = materialHistoryRepository.findByMaterialIdOrderByDateDesc(materialId);
        logger.info("Total history records found: {}", history.size());

        return history;
    }

    private void mapRequestToEntity(RawMaterialRequestDto dto, RawMaterialEntity entity) {
        entity.setName(dto.getName());
        entity.setCategory(dto.getCategory());
        entity.setVendorName(dto.getVendorName());
        entity.setVendorContact(dto.getVendorContact());
        entity.setDeliverTo(dto.getDeliverTo());
        entity.setAssignedTo(dto.getAssignedTo());
        entity.setCurrentStock(dto.getCurrentStock() != null ? dto.getCurrentStock() : 0.0);
        entity.setUnit(dto.getUnit());
        entity.setMinLevel(dto.getMinLevel());
        entity.setMaxLevel(dto.getMaxLevel());
        entity.setUnitPrice(dto.getUnitPrice());
        entity.setTotalPrice(dto.getTotalPrice());
        entity.setTotalAmount(dto.getTotalAmount());
        entity.setSupplier(dto.getSupplier());
        entity.setNotes(dto.getNotes());
        entity.setPosCreatedDate(dto.getPosCreatedDate());
        entity.setDateReceived(dto.getDateReceived());
        entity.setStatus(dto.getStatus() != null ? dto.getStatus() : "pending");

        if (dto.getPayments() != null) {
            List<RawMaterialEntity.Payment> payments = dto.getPayments().stream()
                    .map(p -> new RawMaterialEntity.Payment(p.getDate(), p.getAmount(), p.getMethod()))
                    .collect(Collectors.toList());
            entity.setPayments(payments);
        }
    }

    private RawMaterialResponseDto mapEntityToResponse(RawMaterialEntity entity) {
        RawMaterialResponseDto dto = new RawMaterialResponseDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setCategory(entity.getCategory());
        dto.setVendorName(entity.getVendorName());
        dto.setVendorContact(entity.getVendorContact());
        dto.setDeliverTo(entity.getDeliverTo());
        dto.setAssignedTo(entity.getAssignedTo());
        dto.setCurrentStock(entity.getCurrentStock());
        dto.setUnit(entity.getUnit());
        dto.setMinLevel(entity.getMinLevel());
        dto.setMaxLevel(entity.getMaxLevel());
        dto.setUnitPrice(entity.getUnitPrice());
        dto.setTotalPrice(entity.getTotalPrice());
        dto.setTotalAmount(entity.getTotalAmount());
        dto.setSupplier(entity.getSupplier());
        dto.setNotes(entity.getNotes());
        dto.setPosCreatedDate(entity.getPosCreatedDate());
        dto.setDateReceived(entity.getDateReceived());
        dto.setStatus(entity.getStatus());
        dto.setLastUpdated(entity.getLastUpdated());

        if (entity.getPayments() != null) {
            List<RawMaterialResponseDto.PaymentDto> payments = entity.getPayments().stream()
                    .map(p -> new RawMaterialResponseDto.PaymentDto(p.getDate(), p.getAmount(), p.getMethod()))
                    .collect(Collectors.toList());
            dto.setPayments(payments);
        }

        return dto;
    }

    private void createHistory(RawMaterialEntity entity, String action, Double quantity, Double previousStock) {
        logger.info("Creating history entry for material ID: {} with action: {}", entity.getId(), action);

        MaterialHistoryEntity history = new MaterialHistoryEntity();
        history.setDate(LocalDate.now());
        history.setMaterialId(entity.getId());
        history.setMaterialName(entity.getName());
        history.setAction(action);
        history.setQuantity(quantity);
        history.setPreviousStock(previousStock);
        history.setNewStock(entity.getCurrentStock());
        history.setVendor(entity.getVendorName());
        history.setVendorContact(entity.getVendorContact());
        history.setUnitPrice(entity.getUnitPrice());
        history.setNotes(entity.getNotes());

        materialHistoryRepository.save(history);
        logger.info("History entry created successfully");
    }
}