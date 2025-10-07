package com.thb.bakery.service;


import com.thb.bakery.dto.request.OtpVerificationDto;

public interface OtpService {
    void sendOtp(String mobile);
    boolean verifyOtp(OtpVerificationDto otpVerificationDto, String mobile);

    void sendOtpEmail(String email);      // Add email method

    boolean verifyEmailOtp(OtpVerificationDto otpVerificationDto);    // Add email verification

}
