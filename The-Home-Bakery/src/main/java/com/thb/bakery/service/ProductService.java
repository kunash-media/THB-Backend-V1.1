package com.thb.bakery.service;

import com.thb.bakery.dto.request.ProductCreateRequestDTO;
import com.thb.bakery.dto.request.ProductDTO;
import com.thb.bakery.dto.request.ProductPatchRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {

    ProductDTO createProduct(ProductCreateRequestDTO requestDTO);

    List<ProductDTO> getAllProducts();

    Page<ProductDTO> getAllProducts(Pageable pageable);

    ProductDTO getProductById(Long productId);

    ProductDTO patchProduct(Long productId, ProductPatchRequestDTO patchRequest);

    ProductDTO updateProduct(Long productId, ProductCreateRequestDTO requestDTO);

    void deleteProduct(Long productId);

    List<ProductDTO> getProductsByName(String name);

    boolean existsById(Long productId);

    long getProductCount();

    List<ProductDTO> getProductsByCategory(String category);

    Page<ProductDTO> getProductsByCategory(String category, Pageable pageable);

    List<ProductDTO> getProductsByFoodType(String foodType);
}

