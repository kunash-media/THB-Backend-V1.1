package com.thb.bakery.service;

import com.thb.bakery.dto.request.CouponRequestDto;
import com.thb.bakery.dto.response.CouponResponseDto;

import java.util.List;

public interface CouponService {
    CouponResponseDto createCoupon(CouponRequestDto requestDto);
    CouponResponseDto getCouponById(Long id);
    List<CouponResponseDto> getAllCoupons();
    CouponResponseDto updateCoupon(Long id, CouponRequestDto requestDto);
    void deleteCoupon(Long id);
    CouponResponseDto getCouponByCode(String couponCode);
}