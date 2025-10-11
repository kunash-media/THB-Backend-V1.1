package com.thb.bakery.service;

import com.thb.bakery.dto.request.InventoryRequestDto;
import com.thb.bakery.dto.response.InventoryResponseDto;

import java.util.List;

public interface InventoryService {

    InventoryResponseDto createItem(InventoryRequestDto requestDto);

    InventoryResponseDto getItemById(Long id);

    List<InventoryResponseDto> getAllItems();

    InventoryResponseDto updateItem(Long id, InventoryRequestDto requestDto);

    void deleteItem(Long id);

    List<InventoryResponseDto> bulkUpload(List<InventoryRequestDto> requestDtos);

    InventoryResponseDto getDashboard();  // Full payload with stats, alerts, chart
}