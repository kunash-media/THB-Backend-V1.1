package com.thb.bakery.service.serviceImpl;

import com.thb.bakery.dto.request.CouponRequestDto;
import com.thb.bakery.dto.response.CouponResponseDto;
import com.thb.bakery.entity.CouponEntity;
import com.thb.bakery.repository.CouponRepository;
import com.thb.bakery.service.CouponService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CouponServiceImpl implements CouponService {

    private static final Logger logger = LoggerFactory.getLogger(CouponServiceImpl.class);

    @Autowired
    private CouponRepository couponRepository;

    @Override
    public CouponResponseDto createCoupon(CouponRequestDto requestDto) {
        logger.info("Creating new coupon with code: {}", requestDto.getCouponCode());
        CouponEntity entity = mapToEntity(requestDto);
        CouponEntity saved = couponRepository.save(entity);
        logger.info("Coupon created successfully with ID: {}", saved.getCouponId());
        return mapToResponseDto(saved);
    }

    @Override
    public CouponResponseDto getCouponById(Long id) {
        logger.info("Fetching coupon by ID: {}", id);
        Optional<CouponEntity> optional = couponRepository.findById(id);
        if (optional.isPresent()) {
            logger.info("Coupon found for ID: {}", id);
            return mapToResponseDto(optional.get());
        }
        logger.warn("Coupon not found for ID: {}", id);
        return null;
    }

    @Override
    public List<CouponResponseDto> getAllCoupons() {
        logger.info("Fetching all coupons");
        List<CouponEntity> entities = couponRepository.findAll();
        logger.info("Retrieved {} coupons", entities.size());
        return entities.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public CouponResponseDto updateCoupon(Long id, CouponRequestDto requestDto) {
        logger.info("Updating coupon with ID: {}", id);
        Optional<CouponEntity> optional = couponRepository.findById(id);
        if (optional.isPresent()) {
            CouponEntity entity = optional.get();
            updateEntityFromDto(entity, requestDto);
            CouponEntity updated = couponRepository.save(entity);
            logger.info("Coupon updated successfully for ID: {}", id);
            return mapToResponseDto(updated);
        }
        logger.warn("Coupon not found for update with ID: {}", id);
        return null;
    }

    @Override
    public void deleteCoupon(Long id) {
        logger.info("Deleting coupon with ID: {}", id);
        if (couponRepository.existsById(id)) {
            couponRepository.deleteById(id);
            logger.info("Coupon deleted successfully for ID: {}", id);
        } else {
            logger.warn("Coupon not found for deletion with ID: {}", id);
        }
    }

    @Override
    public CouponResponseDto getCouponByCode(String couponCode) {
        logger.info("Fetching coupon by code: {}", couponCode);
        Optional<CouponEntity> optional = couponRepository.findByCouponCode(couponCode);
        if (optional.isPresent()) {
            logger.info("Coupon found for code: {}", couponCode);
            return mapToResponseDto(optional.get());
        }
        logger.warn("Coupon not found for code: {}", couponCode);
        return null;
    }

    private CouponEntity mapToEntity(CouponRequestDto dto) {
        CouponEntity entity = new CouponEntity();
        entity.setEventType(dto.getEventType());
        entity.setCouponDescription(dto.getCouponDescription());
        entity.setCouponDiscount(dto.getCouponDiscount());
        entity.setValidFromDate(dto.getValidFromDate());
        entity.setValidUntilDate(dto.getValidUntilDate());
        entity.setStatus(dto.getStatus());
        entity.setCouponCode(dto.getCouponCode());
        entity.setIsUsed(dto.getIsUsed() != null ? dto.getIsUsed() : false);
        entity.setUserId(dto.getUserId());
        entity.setCategory(dto.getCategory());
        return entity;
    }

    private void updateEntityFromDto(CouponEntity entity, CouponRequestDto dto) {
        entity.setEventType(dto.getEventType());
        entity.setCouponDescription(dto.getCouponDescription());
        entity.setCouponDiscount(dto.getCouponDiscount());
        entity.setValidFromDate(dto.getValidFromDate());
        entity.setValidUntilDate(dto.getValidUntilDate());
        entity.setStatus(dto.getStatus());
        entity.setCouponCode(dto.getCouponCode());
        entity.setIsUsed(dto.getIsUsed() != null ? dto.getIsUsed() : entity.getIsUsed());
        entity.setUserId(dto.getUserId());
        entity.setCategory(dto.getCategory());
    }

    private CouponResponseDto mapToResponseDto(CouponEntity entity) {
        CouponResponseDto dto = new CouponResponseDto();
        dto.setCouponId(entity.getCouponId());
        dto.setEventType(entity.getEventType());
        dto.setCouponDescription(entity.getCouponDescription());
        dto.setCouponDiscount(entity.getCouponDiscount());
        dto.setValidFromDate(entity.getValidFromDate());
        dto.setValidUntilDate(entity.getValidUntilDate());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setStatus(entity.getStatus());
        dto.setCouponCode(entity.getCouponCode());
        dto.setIsUsed(entity.getIsUsed());
        dto.setUserId(entity.getUserId());
        dto.setCategory(entity.getCategory());
        return dto;
    }
}