package com.thb.bakery.service;

import com.thb.bakery.dto.request.CustomizationRequestDTO;
import com.thb.bakery.dto.response.CustomizationResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomizationService {
    CustomizationResponseDTO createCustomization(CustomizationRequestDTO requestDTO);
    CustomizationResponseDTO getCustomizationById(Long id);
    Page<CustomizationResponseDTO> getAllCustomizations(Pageable pageable);
    CustomizationResponseDTO updateCustomization(Long id, CustomizationRequestDTO requestDTO);
    CustomizationResponseDTO patchCustomization(Long id, CustomizationRequestDTO requestDTO);
    void deleteCustomization(Long id);
}