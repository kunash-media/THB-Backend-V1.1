package com.bakery.The.Home.Bakery.service;

import com.bakery.The.Home.Bakery.dto.request.AdminLoginDTO;
import com.bakery.The.Home.Bakery.dto.request.AdminRequestDTO;
import com.bakery.The.Home.Bakery.dto.request.AdminUpdateDTO;
import com.bakery.The.Home.Bakery.dto.response.AdminResponseDTO;

import java.util.List;

public interface AdminService {

    // Existing methods
    AdminResponseDTO createAdmin(AdminRequestDTO adminRequestDTO);
    AdminResponseDTO getAdminById(Long id);
    AdminResponseDTO getAdminByEmail(String email);
    List<AdminResponseDTO> getAllAdmins();
    AdminResponseDTO updateAdmin(Long id, AdminUpdateDTO adminUpdateDTO);
    void deleteAdmin(Long id);
    boolean existsByEmail(String email);

    // New methods
    AdminResponseDTO authenticateAdmin(AdminLoginDTO adminLoginDTO);
    List<AdminResponseDTO> getAdminsByRole(String role);
}