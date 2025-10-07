package com.thb.bakery.service;

import com.thb.bakery.dto.request.AdminLoginDTO;
import com.thb.bakery.dto.request.AdminRequestDTO;
import com.thb.bakery.dto.request.AdminUpdateDTO;
import com.thb.bakery.dto.response.AdminResponseDTO;

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