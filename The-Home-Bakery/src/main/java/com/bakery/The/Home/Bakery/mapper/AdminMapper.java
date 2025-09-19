package com.bakery.The.Home.Bakery.mapper;

import com.bakery.The.Home.Bakery.dto.request.AdminRequestDTO;
import com.bakery.The.Home.Bakery.dto.request.AdminUpdateDTO;
import com.bakery.The.Home.Bakery.dto.response.AdminResponseDTO;
import com.bakery.The.Home.Bakery.entity.AdminEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AdminMapper {

    // Convert AdminRequestDTO to AdminEntity
    public AdminEntity toEntity(AdminRequestDTO adminRequestDTO) {
        AdminEntity adminEntity = new AdminEntity();
        adminEntity.setName(adminRequestDTO.getName());
        adminEntity.setMobile(adminRequestDTO.getMobile());
        adminEntity.setEmail(adminRequestDTO.getEmail());
        adminEntity.setPassword(adminRequestDTO.getPassword());
        adminEntity.setRole(adminRequestDTO.getRole());
        adminEntity.setCreatedAt(LocalDateTime.now());
        adminEntity.setUpdatedAt(LocalDateTime.now());
        return adminEntity;
    }

    // Convert AdminEntity to AdminResponseDTO
    public AdminResponseDTO toResponseDTO(AdminEntity adminEntity) {
        AdminResponseDTO responseDTO = new AdminResponseDTO();
        responseDTO.setId(adminEntity.getId());
        responseDTO.setName(adminEntity.getName());
        responseDTO.setMobile(adminEntity.getMobile());
        responseDTO.setEmail(adminEntity.getEmail());
        responseDTO.setRole(adminEntity.getRole());
        responseDTO.setCreatedAt(adminEntity.getCreatedAt());
        responseDTO.setUpdatedAt(adminEntity.getUpdatedAt());
        return responseDTO;
    }

    // Convert List<AdminEntity> to List<AdminResponseDTO>
    public List<AdminResponseDTO> toResponseDTOList(List<AdminEntity> adminEntities) {
        return adminEntities.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    // Update AdminEntity from AdminUpdateDTO
    public void updateEntityFromDTO(AdminUpdateDTO adminUpdateDTO, AdminEntity adminEntity) {
        if (adminUpdateDTO.getName() != null) {
            adminEntity.setName(adminUpdateDTO.getName());
        }
        if (adminUpdateDTO.getMobileNumber()!= null){
            adminEntity.setMobile(adminUpdateDTO.getMobileNumber());
        }
        if (adminUpdateDTO.getEmail() != null) {
            adminEntity.setEmail(adminUpdateDTO.getEmail());
        }
        if (adminUpdateDTO.getPassword() != null) {
            adminEntity.setPassword(adminUpdateDTO.getPassword());
        }
        if (adminUpdateDTO.getRole() != null) {
            adminEntity.setRole(adminUpdateDTO.getRole());
        }
        adminEntity.setUpdatedAt(LocalDateTime.now());
    }
}