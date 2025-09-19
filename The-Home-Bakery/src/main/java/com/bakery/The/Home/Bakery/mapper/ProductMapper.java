package com.bakery.The.Home.Bakery.mapper;

import com.bakery.The.Home.Bakery.dto.request.ProductCreateRequestDTO;
import com.bakery.The.Home.Bakery.dto.request.ProductDTO;
import com.bakery.The.Home.Bakery.dto.request.ProductPatchRequestDTO;
import com.bakery.The.Home.Bakery.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    // FIXED: Changed target from "isDeleted" to "deleted"
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "productId", ignore = true)
    ProductEntity toEntity(ProductCreateRequestDTO dto);

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

}