package com.thb.bakery.service;

import com.thb.bakery.dto.request.CustomizeCakeRequestDTO;
import com.thb.bakery.dto.response.CustomizeCakeResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

public interface CustomizeCakeService {
    CustomizeCakeResponseDTO createProduct(CustomizeCakeRequestDTO requestDTO);
    CustomizeCakeResponseDTO updateProduct(Long id, CustomizeCakeRequestDTO requestDTO);
    CustomizeCakeResponseDTO getProductById(Long id);
    List<CustomizeCakeResponseDTO> getAllProducts();
    Page<CustomizeCakeResponseDTO> getAllProducts(Pageable pageable);
    List<CustomizeCakeResponseDTO> searchProducts(String query);
    List<CustomizeCakeResponseDTO> getProductsByCategory(String category);
    List<CustomizeCakeResponseDTO> getProductsBySubcategory(String subcategory);
    List<CustomizeCakeResponseDTO> getProductsByCategoryAndSubcategory(String category, String subcategory);
    List<CustomizeCakeResponseDTO> getProductsWithMinimumDiscount(BigDecimal minDiscount);
    void deleteProduct(Long id);
    void permanentDeleteProduct(Long id);
    CustomizeCakeResponseDTO toggleProductStatus(Long id);
    long getActiveProductsCount();
    String saveImage(MultipartFile imageFile);

    // Add to CustomizeCakeService.java

    List<CustomizeCakeResponseDTO> getAllDeletedProducts();
    List<CustomizeCakeResponseDTO> getAllProductsIncludingDeleted();

    CustomizeCakeResponseDTO restoreProduct(Long id);
}
