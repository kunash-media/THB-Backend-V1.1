package com.thb.bakery.service;

import com.thb.bakery.dto.response.SnackResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface SnacksService {

    SnackResponseDTO create(String productData, MultipartFile productMainImage);

    Page<SnackResponseDTO> getAll(Pageable pageable);

    SnackResponseDTO getById(Long id);

    Page<SnackResponseDTO> getByCategory(String category, Pageable pageable);

    Page<SnackResponseDTO> getBySubcategory(String subcategory, Pageable pageable);

    List<SnackResponseDTO> searchByName(String name);

    SnackResponseDTO update(Long id, String productData, MultipartFile productMainImage);

    SnackResponseDTO patch(Long id, String productData, MultipartFile productMainImage);

    void delete(Long id);

}
