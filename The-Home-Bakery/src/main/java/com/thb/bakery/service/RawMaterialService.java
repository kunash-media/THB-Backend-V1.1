package com.thb.bakery.service;

import com.thb.bakery.dto.request.RawMaterialRequestDto;
import com.thb.bakery.dto.response.RawMaterialResponseDto;
import com.thb.bakery.entity.MaterialHistoryEntity;

import java.util.List;

public interface RawMaterialService {

    RawMaterialResponseDto createRawMaterial(RawMaterialRequestDto requestDto);

    RawMaterialResponseDto updateRawMaterial(Long id, RawMaterialRequestDto requestDto);

    RawMaterialResponseDto getRawMaterialById(Long id);

    List<RawMaterialResponseDto> getAllRawMaterials();

    void deleteRawMaterial(Long id);

    List<RawMaterialResponseDto> getRawMaterialsByCategory(String category);

    List<RawMaterialResponseDto> getRawMaterialsByStatus(String status);

    List<RawMaterialResponseDto> getRawMaterialsByVendor(String vendorName);

    List<MaterialHistoryEntity> getMaterialHistory(Long materialId);

}