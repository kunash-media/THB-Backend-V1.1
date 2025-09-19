package com.bakery.The.Home.Bakery.repository;

import com.bakery.The.Home.Bakery.entity.AdminEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<AdminEntity, Long> {

    // Find admin by email
    Optional<AdminEntity> findByEmail(String email);

    // Check if admin exists by email
    boolean existsByEmail(String email);

    // Find admin by name
    Optional<AdminEntity> findByName(String name);
    Optional<AdminEntity> findByMobile(String mobile);

    // Check if admin exists by name
    boolean existsByName(String name);

    Optional<AdminEntity> findAdminByEmail(String email);

    @Query("SELECT u FROM AdminEntity u WHERE u.mobile = :mobile")
    Optional<AdminEntity> findAdminByMobile(@Param("mobile") String mobile);

    // New methods
    Optional<AdminEntity> findByEmailAndPassword(String email, String password);
    List<AdminEntity> findByRole(String role);

}