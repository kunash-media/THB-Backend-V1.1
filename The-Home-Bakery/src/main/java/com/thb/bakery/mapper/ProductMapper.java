package com.thb.bakery.mapper;

import com.thb.bakery.dto.request.ProductCreateRequestDTO;
import com.thb.bakery.dto.request.ProductDTO;
import com.thb.bakery.dto.request.ProductPatchRequestDTO;
import com.thb.bakery.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    // FIXED: Changed target from "isDeleted" to "deleted"
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "productId", ignore = true)
    ProductEntity toEntity(ProductCreateRequestDTO dto);

    @Mapping(target = "productImageUrl", expression = "java(generateImageUrl(entity.getProductId(), entity.getProductImage()))")
    @Mapping(target = "productSubImageUrls", expression = "java(generateSubImageUrls(entity.getProductId(), entity.getProductSubImages()))")
    ProductDTO toDTO(ProductEntity entity);

    List<ProductDTO> toDTOList(List<ProductEntity> entities);

    // FIXED: Changed target from "isDeleted" to "deleted"
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "productId", ignore = true)
    void updateEntityFromDTO(ProductCreateRequestDTO dto, @MappingTarget ProductEntity entity);

    // FIXED: Changed target from "isDeleted" to "deleted"
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "productId", ignore = true)
    void updateEntityFromPatchDTO(ProductPatchRequestDTO dto, @MappingTarget ProductEntity entity);

    // Image URL generation methods
    default String generateImageUrl(Long productId, byte[] productImage) {
        if (productId == null || productImage == null || productImage.length == 0) {
            return null;
        }
        return "/api/v1/products/" + productId + "/image";
    }

    default List<String> generateSubImageUrls(Long productId, List<byte[]> subImages) {
        if (productId == null || subImages == null || subImages.isEmpty()) {
            return new ArrayList<>();
        }

        List<String> urls = new ArrayList<>();
        for (int i = 0; i < subImages.size(); i++) {
            if (subImages.get(i) != null && subImages.get(i).length > 0) {
                urls.add("/api/v1/products/" + productId + "/subimage/" + i);
            }
        }
        return urls;
    }
}