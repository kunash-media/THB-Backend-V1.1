package com.bakery.The.Home.Bakery.controller;

import com.bakery.The.Home.Bakery.dto.request.AdminLoginDTO;
import com.bakery.The.Home.Bakery.dto.request.AdminRequestDTO;
import com.bakery.The.Home.Bakery.dto.request.AdminUpdateDTO;
import com.bakery.The.Home.Bakery.dto.response.AdminResponseDTO;
import com.bakery.The.Home.Bakery.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    // Create new admin
    @PostMapping("/create-admin")
    public ResponseEntity<AdminResponseDTO> createAdmin(@RequestBody AdminRequestDTO adminRequestDTO) {
        AdminResponseDTO createdAdmin = adminService.createAdmin(adminRequestDTO);
        return new ResponseEntity<>(createdAdmin, HttpStatus.CREATED);
    }

    // Get admin by ID
    @GetMapping("get-admin/{id}")
    public ResponseEntity<AdminResponseDTO> getAdminById(@PathVariable Long id) {
        AdminResponseDTO admin = adminService.getAdminById(id);
        if (admin != null) {
            return new ResponseEntity<>(admin, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Get admin by email
    @GetMapping("/email/{email}")
    public ResponseEntity<AdminResponseDTO> getAdminByEmail(@PathVariable String email) {
        AdminResponseDTO admin = adminService.getAdminByEmail(email);
        if (admin != null) {
            return new ResponseEntity<>(admin, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Get all admins
    @GetMapping("/get-all-admins")
    public ResponseEntity<List<AdminResponseDTO>> getAllAdmins() {
        List<AdminResponseDTO> admins = adminService.getAllAdmins();
        return new ResponseEntity<>(admins, HttpStatus.OK);
    }

    // Update admin
    @PatchMapping("update-admin/{id}")
    public ResponseEntity<AdminResponseDTO> updateAdmin(@PathVariable Long id,
                                                        @RequestBody AdminUpdateDTO adminUpdateDTO) {
        AdminResponseDTO updatedAdmin = adminService.updateAdmin(id, adminUpdateDTO);
        if (updatedAdmin != null) {
            return new ResponseEntity<>(updatedAdmin, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Delete admin
    @DeleteMapping("delete-admin/{id}")
    public ResponseEntity<String> deleteAdmin(@PathVariable Long id) {
        AdminResponseDTO admin = adminService.getAdminById(id);
        if (admin != null) {
            adminService.deleteAdmin(id);
            return new ResponseEntity<>("Admin Deleletd Successfully!",HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Check if admin exists by email
    @GetMapping("/exists/{email}")
    public ResponseEntity<Boolean> existsByEmail(@PathVariable String email) {
        boolean exists = adminService.existsByEmail(email);
        return new ResponseEntity<>(exists, HttpStatus.OK);
    }

    // NEW: Authenticate admin with email and password
    @PostMapping("/login-admin")
    public ResponseEntity<AdminResponseDTO> authenticateAdmin(@RequestBody AdminLoginDTO adminLoginDTO) {
        AdminResponseDTO admin = adminService.authenticateAdmin(adminLoginDTO);
        if (admin != null) {
            return new ResponseEntity<>(admin, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    // NEW: Get admins by role
    @GetMapping("/role/{role}")
    public ResponseEntity<List<AdminResponseDTO>> getAdminsByRole(@PathVariable String role) {
        List<AdminResponseDTO> admins = adminService.getAdminsByRole(role);
        return new ResponseEntity<>(admins, HttpStatus.OK);
    }
}