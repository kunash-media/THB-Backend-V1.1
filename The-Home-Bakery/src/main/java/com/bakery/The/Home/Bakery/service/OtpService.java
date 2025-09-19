package com.bakery.The.Home.Bakery.service;


import com.bakery.The.Home.Bakery.dto.request.OtpVerificationDto;

public interface OtpService {
    void sendOtp(String mobile);
    boolean verifyOtp(OtpVerificationDto otpVerificationDto, String mobile);

    void sendOtpEmail(String email);      // Add email method

    boolean verifyEmailOtp(OtpVerificationDto otpVerificationDto);    // Add email verification

}
