package com.bakery.The.Home.Bakery.controller;


import com.bakery.The.Home.Bakery.dto.request.EmailRequest;
import com.bakery.The.Home.Bakery.dto.request.OtpRequestDto;
import com.bakery.The.Home.Bakery.dto.request.OtpVerificationDto;
import com.bakery.The.Home.Bakery.service.OtpService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/otp")
public class OtpController {

    private final OtpService otpService;

    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOtp(@RequestBody OtpRequestDto otpRequest) {
        otpService.sendOtp(otpRequest.getMobile());
        return ResponseEntity.ok().body("OTP sent successfully");
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody OtpVerificationDto otpVerification, String mobile) {
        boolean isVerified = otpService.verifyOtp(otpVerification, mobile);
        if (isVerified) {
            return ResponseEntity.ok().body("Password reset successfully");
        }
        return ResponseEntity.badRequest().body("Invalid OTP or expired");
    }
    public OtpController(OtpService otpService) {
        this.otpService = otpService;
    }

    @PostMapping("/send-email-body")
    public ResponseEntity<String> sendEmailOtp(@RequestBody EmailRequest emailRequest) {
        try {
            otpService.sendOtpEmail(emailRequest.getEmail());
            return ResponseEntity.ok("OTP sent successfully to email: " + emailRequest.getEmail());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to send OTP: " + e.getMessage());
        }
    }

    @PostMapping("/verify-email")
    public ResponseEntity<String> verifyEmailOtp(@RequestBody OtpVerificationDto otpVerificationDto) {
        try {
            boolean isValid = otpService.verifyEmailOtp(otpVerificationDto);
            if (isValid) {
                return ResponseEntity.ok("OTP verified successfully");
            } else {
                return ResponseEntity.badRequest().body("Invalid or expired OTP");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Verification failed: " + e.getMessage());
        }
    }


}