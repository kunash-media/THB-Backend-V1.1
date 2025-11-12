package com.thb.bakery.service;


import com.thb.bakery.dto.request.SnacksDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface SnacksService {

    SnacksDTO create(String productData, MultipartFile productMainImage);
    Page<SnacksDTO> getAll(Pageable pageable);
    SnacksDTO getById(Long id);
    Page<SnacksDTO> getByCategory(String category, Pageable pageable);
    Page<SnacksDTO> getBySubcategory(String subcategory, Pageable pageable);
    List<SnacksDTO> searchByName(String name);
    SnacksDTO update(Long id, String productData, MultipartFile productMainImage);
    SnacksDTO patch(Long id, String productData, MultipartFile productMainImage);
    void delete(Long id);

}
