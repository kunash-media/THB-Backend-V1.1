package com.bakery.The.Home.Bakery.service.serviceImpl;

import com.bakery.The.Home.Bakery.Bcrypt.BcryptEncoderConfig;
import com.bakery.The.Home.Bakery.dto.request.AdminLoginDTO;
import com.bakery.The.Home.Bakery.dto.request.AdminRequestDTO;
import com.bakery.The.Home.Bakery.dto.request.AdminUpdateDTO;
import com.bakery.The.Home.Bakery.dto.response.AdminResponseDTO;
import com.bakery.The.Home.Bakery.entity.AdminEntity;
import com.bakery.The.Home.Bakery.mapper.AdminMapper;
import com.bakery.The.Home.Bakery.repository.AdminRepository;
import com.bakery.The.Home.Bakery.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private AdminMapper adminMapper;

    private final BcryptEncoderConfig passwordEncoder;

    public AdminServiceImpl(BcryptEncoderConfig passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AdminResponseDTO createAdmin(AdminRequestDTO adminRequestDTO) {
        AdminEntity adminEntity = adminMapper.toEntity(adminRequestDTO);

        // Hash the password before saving
        String hashedPassword = passwordEncoder.encode(adminRequestDTO.getPassword());
        adminEntity.setPassword(hashedPassword);

        AdminEntity savedEntity = adminRepository.save(adminEntity);
        return adminMapper.toResponseDTO(savedEntity);
    }

    @Override
    public AdminResponseDTO getAdminById(Long id) {
        Optional<AdminEntity> adminEntity = adminRepository.findById(id);
        return adminEntity.map(adminMapper::toResponseDTO).orElse(null);
    }

    @Override
    public AdminResponseDTO getAdminByEmail(String email) {
        Optional<AdminEntity> adminEntity = adminRepository.findByEmail(email);
        return adminEntity.map(adminMapper::toResponseDTO).orElse(null);
    }

    @Override
    public List<AdminResponseDTO> getAllAdmins() {
        List<AdminEntity> adminEntities = adminRepository.findAll();
        return adminMapper.toResponseDTOList(adminEntities);
    }

    @Override
    public AdminResponseDTO updateAdmin(Long id, AdminUpdateDTO adminUpdateDTO) {
        Optional<AdminEntity> existingAdmin = adminRepository.findById(id);
        if (existingAdmin.isPresent()) {
            AdminEntity adminToUpdate = existingAdmin.get();

            // If password is being updated, hash it
            if (adminUpdateDTO.getPassword() != null) {
                String hashedPassword = passwordEncoder.encode(adminUpdateDTO.getPassword());
                adminUpdateDTO.setPassword(hashedPassword);
            }

            adminMapper.updateEntityFromDTO(adminUpdateDTO, adminToUpdate);
            AdminEntity updatedEntity = adminRepository.save(adminToUpdate);
            return adminMapper.toResponseDTO(updatedEntity);
        }
        return null;
    }

    @Override
    public void deleteAdmin(Long id) {
        adminRepository.deleteById(id);
    }

    @Override
    public boolean existsByEmail(String email) {
        return adminRepository.existsByEmail(email);
    }

    @Override
    public AdminResponseDTO authenticateAdmin(AdminLoginDTO adminLoginDTO) {
        Optional<AdminEntity> adminEntity = adminRepository.findByEmail(adminLoginDTO.getEmail());

        if (adminEntity.isPresent()) {
            AdminEntity admin = adminEntity.get();

            // Verify password using BCrypt
            if (passwordEncoder.matches(adminLoginDTO.getPassword(), admin.getPassword())) {
                return adminMapper.toResponseDTO(admin);
            }
        }
        return null;
    }

    @Override
    public List<AdminResponseDTO> getAdminsByRole(String role) {
        List<AdminEntity> adminEntities = adminRepository.findByRole(role);
        return adminMapper.toResponseDTOList(adminEntities);
    }
}