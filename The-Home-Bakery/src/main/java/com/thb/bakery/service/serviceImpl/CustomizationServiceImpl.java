package com.thb.bakery.service.serviceImpl;

import com.thb.bakery.dto.request.CustomizationRequestDTO;
import com.thb.bakery.dto.response.CustomizationResponseDTO;
import com.thb.bakery.entity.CustomizationEntity;
import com.thb.bakery.entity.UserEntity;
import com.thb.bakery.repository.CustomizationRepository;
import com.thb.bakery.repository.UserRepository;
import com.thb.bakery.service.CustomizationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CustomizationServiceImpl implements CustomizationService {

    private static final Logger logger = LoggerFactory.getLogger(CustomizationServiceImpl.class);

    @Autowired
    private CustomizationRepository customizationRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public CustomizationResponseDTO createCustomization(CustomizationRequestDTO requestDTO) {
        logger.info("Creating new customization with flavour: {}", requestDTO.getCakeFlavour());

        CustomizationEntity entity = new CustomizationEntity();
        entity.setCakeFlavour(requestDTO.getCakeFlavour());
        entity.setCakeSize(requestDTO.getCakeSize());
        entity.setCakeAddOnes(requestDTO.getCakeAddOnes());
        entity.setIsGuest(requestDTO.isGuest());
        if (requestDTO.getUserId() != null) {
            UserEntity user = userRepository.findById(requestDTO.getUserId())
                    .orElseThrow(() -> {
                        logger.error("User not found with ID: {}", requestDTO.getUserId());
                        return new RuntimeException("User not found");
                    });
            entity.setUser(user);
        }

        CustomizationEntity savedEntity = customizationRepository.save(entity);
        logger.info("Customization created with ID: {}", savedEntity.getCustomizationId());

        return convertToResponseDTO(savedEntity);
    }

    @Override
    public CustomizationResponseDTO getCustomizationById(Long id) {
        logger.info("Fetching customization with ID: {}", id);
        CustomizationEntity entity = customizationRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Customization not found with ID: {}", id);
                    return new RuntimeException("Customization not found");
                });
        return convertToResponseDTO(entity);
    }

    @Override
    public Page<CustomizationResponseDTO> getAllCustomizations(Pageable pageable) {
        logger.info("Fetching all customizations with pagination - page: {}, size: {}",
                pageable.getPageNumber(), pageable.getPageSize());
        return customizationRepository.findAll(pageable)
                .map(this::convertToResponseDTO);
    }

    @Override
    public CustomizationResponseDTO updateCustomization(Long id, CustomizationRequestDTO requestDTO) {
        logger.info("Updating customization with ID: {}", id);
        CustomizationEntity entity = customizationRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Customization not found with ID: {}", id);
                    return new RuntimeException("Customization not found");
                });

        entity.setCakeFlavour(requestDTO.getCakeFlavour());
        entity.setCakeSize(requestDTO.getCakeSize());
        entity.setCakeAddOnes(requestDTO.getCakeAddOnes());
        entity.setIsGuest(requestDTO.isGuest());
        if (requestDTO.getUserId() != null) {
            UserEntity user = userRepository.findById(requestDTO.getUserId())
                    .orElseThrow(() -> {
                        logger.error("User not found with ID: {}", requestDTO.getUserId());
                        return new RuntimeException("User not found");
                    });
            entity.setUser(user);
        } else {
            entity.setUser(null);
        }

        CustomizationEntity updatedEntity = customizationRepository.save(entity);
        logger.info("Customization updated with ID: {}", id);
        return convertToResponseDTO(updatedEntity);
    }

    @Override
    public CustomizationResponseDTO patchCustomization(Long id, CustomizationRequestDTO requestDTO) {
        logger.info("Patching customization with ID: {}", id);
        CustomizationEntity entity = customizationRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Customization not found with ID: {}", id);
                    return new RuntimeException("Customization not found");
                });

        if (requestDTO.getCakeFlavour() != null) {
            entity.setCakeFlavour(requestDTO.getCakeFlavour());
        }
        if (requestDTO.getCakeSize() != null) {
            entity.setCakeSize(requestDTO.getCakeSize());
        }
        if (requestDTO.getCakeAddOnes() != null) {
            entity.setCakeAddOnes(requestDTO.getCakeAddOnes());
        }
        if (requestDTO.getUserId() != null) {
            UserEntity user = userRepository.findById(requestDTO.getUserId())
                    .orElseThrow(() -> {
                        logger.error("User not found with ID: {}", requestDTO.getUserId());
                        return new RuntimeException("User not found");
                    });
            entity.setUser(user);
        }
        entity.setIsGuest(requestDTO.isGuest());

        CustomizationEntity updatedEntity = customizationRepository.save(entity);
        logger.info("Customization patched with ID: {}", id);
        return convertToResponseDTO(updatedEntity);
    }

    @Override
    public void deleteCustomization(Long id) {
        logger.info("Deleting customization with ID: {}", id);
        if (!customizationRepository.existsById(id)) {
            logger.error("Customization not found with ID: {}", id);
            throw new RuntimeException("Customization not found");
        }
        customizationRepository.deleteById(id);
        logger.info("Customization deleted with ID: {}", id);
    }

    private CustomizationResponseDTO convertToResponseDTO(CustomizationEntity entity) {
        CustomizationResponseDTO responseDTO = new CustomizationResponseDTO();
        responseDTO.setCustomizationId(entity.getCustomizationId());
        responseDTO.setCakeFlavour(entity.getCakeFlavour());
        responseDTO.setCakeSize(entity.getCakeSize());
        responseDTO.setCakeAddOnes(entity.getCakeAddOnes());
        responseDTO.setUserId(entity.getUser() != null ? entity.getUser().getUserId() : null);
        responseDTO.setIsGuest(entity.isGuest());
        return responseDTO;
    }
}