package com.bakery.The.Home.Bakery.repository;

import com.bakery.The.Home.Bakery.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByMobile(String mobile);

    // Find users by status (active/inactive)
    List<UserEntity> findByStatus(String status);




    // Find users by customer name containing
    //List<UserEntity> findByCustomerNameContainingIgnoreCase(String customerName);

    boolean existsByMobileOrEmail(String mobile, String email);

    @Query("SELECT u FROM UserEntity u WHERE u.mobile = :mobile")
    Optional<UserEntity> findUserByMobile(@Param("mobile") String mobile);


    Optional<UserEntity> findByUserId(Long userId);

    Optional<UserEntity> findUserByEmail(String email);

    UserEntity findByEmail(String email);

    Long countByStatus(String status);



}