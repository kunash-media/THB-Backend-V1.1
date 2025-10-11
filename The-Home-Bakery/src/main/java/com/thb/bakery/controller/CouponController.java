package com.thb.bakery.controller;

import com.thb.bakery.dto.request.CouponRequestDto;
import com.thb.bakery.dto.response.CouponResponseDto;
import com.thb.bakery.service.CouponService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/coupons")
public class CouponController {

    private static final Logger logger = LoggerFactory.getLogger(CouponController.class);

    @Autowired
    private CouponService couponService;

    @PostMapping("/create-Coupon")
    public ResponseEntity<CouponResponseDto> createCoupon(@RequestBody CouponRequestDto requestDto) {
        logger.info("Received request to create coupon");
        CouponResponseDto response = couponService.createCoupon(requestDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/get-coupon-by-Id/{id}")
    public ResponseEntity<CouponResponseDto> getCouponById(@PathVariable Long id) {
        logger.info("Received request to get coupon by ID: {}", id);
        CouponResponseDto response = couponService.getCouponById(id);
        if (response != null) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/get-all-coupons")
    public ResponseEntity<List<CouponResponseDto>> getAllCoupons() {
        logger.info("Received request to get all coupons");
        List<CouponResponseDto> response = couponService.getAllCoupons();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update-coupon/{id}")
    public ResponseEntity<CouponResponseDto> updateCoupon(@PathVariable Long id, @RequestBody CouponRequestDto requestDto) {
        logger.info("Received request to update coupon by ID: {}", id);
        CouponResponseDto response = couponService.updateCoupon(id, requestDto);
        if (response != null) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/delete-coupon/{id}")
    public ResponseEntity<Void> deleteCoupon(@PathVariable Long id) {
        logger.info("Received request to delete coupon by ID: {}", id);
        couponService.deleteCoupon(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/get-coupon-by-code/code/{couponCode}")
    public ResponseEntity<CouponResponseDto> getCouponByCode(@PathVariable String couponCode) {
        logger.info("Received request to get coupon by code: {}", couponCode);
        CouponResponseDto response = couponService.getCouponByCode(couponCode);
        if (response != null) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.notFound().build();
    }
}


