package com.thb.bakery.repository;

import com.thb.bakery.entity.BannerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BannerRepository extends JpaRepository<BannerEntity, Long> {

    Optional<BannerEntity> findByPageName(String pageName);
}
