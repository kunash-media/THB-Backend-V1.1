package com.thb.bakery.controller;

import com.thb.bakery.dto.request.EmailRequest;
import com.thb.bakery.dto.request.OtpRequestDto;
import com.thb.bakery.dto.request.OtpVerificationDto;
import com.thb.bakery.service.OtpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/otp")
public class OtpController {

    private static final Logger logger = LoggerFactory.getLogger(OtpController.class);
    private final OtpService otpService;

    public OtpController(OtpService otpService) {
        this.otpService = otpService;
    }

    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOtp(@RequestBody OtpRequestDto otpRequest) {
        logger.info("Received request to send OTP to mobile: {}", otpRequest.getMobile());

        try {
            if (otpRequest.getMobile() == null || otpRequest.getMobile().trim().isEmpty()) {
                logger.warn("Invalid mobile number provided: {}", otpRequest.getMobile());
                return ResponseEntity.badRequest().body("Mobile number is required");
            }

            String mobile = otpRequest.getMobile().trim();
            logger.debug("Processing OTP request for mobile: {}", mobile);

            otpService.sendOtp(mobile);
            logger.info("OTP sent successfully to mobile: {}", mobile);

            return ResponseEntity.ok().body("OTP sent successfully");

        } catch (Exception e) {
            logger.error("Error sending OTP to mobile {}: {}", otpRequest.getMobile(), e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to send OTP: " + e.getMessage());
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody OtpVerificationDto otpVerification) {
        logger.info("Received request to verify OTP for mobile");
        logger.debug("OTP verification details - OTP: {}, Mobile: {}",
                otpVerification.getOtp(), otpVerification.getMobile());

        try {
            if (otpVerification.getOtp() == null || otpVerification.getOtp().trim().isEmpty()) {
                logger.warn("Invalid OTP provided");
                return ResponseEntity.badRequest().body("OTP is required");
            }

            if (otpVerification.getMobile() == null || otpVerification.getMobile().trim().isEmpty()) {
                logger.warn("Invalid mobile number provided for OTP verification");
                return ResponseEntity.badRequest().body("Mobile number is required");
            }

            String mobile = otpVerification.getMobile().trim();
            String otp = otpVerification.getOtp().trim();

            logger.debug("Verifying OTP: {} for mobile: {}", otp, mobile);
            boolean isVerified = otpService.verifyOtp(otpVerification, mobile);

            if (isVerified) {
                logger.info("OTP verified successfully for mobile: {}", mobile);
                return ResponseEntity.ok().body("Password reset successfully");
            } else {
                logger.warn("OTP verification failed for mobile: {}", mobile);
                return ResponseEntity.badRequest().body("Invalid OTP or expired");
            }

        } catch (Exception e) {
            logger.error("Error verifying OTP for mobile {}: {}",
                    otpVerification.getMobile(), e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("OTP verification failed: " + e.getMessage());
        }
    }

    @PostMapping("/send-email-body")
    public ResponseEntity<String> sendEmailOtp(@RequestBody EmailRequest emailRequest) {
        logger.info("Received request to send OTP to email: {}", emailRequest.getEmail());

        try {
            if (emailRequest.getEmail() == null || emailRequest.getEmail().trim().isEmpty()) {
                logger.warn("Invalid email address provided");
                return ResponseEntity.badRequest().body("Email address is required");
            }

            String email = emailRequest.getEmail().trim();
            logger.debug("Processing email OTP request for: {}", email);

            otpService.sendOtpEmail(email);
            logger.info("OTP sent successfully to email: {}", email);

            return ResponseEntity.ok("OTP sent successfully to email: " + email);

        } catch (Exception e) {
            logger.error("Error sending OTP to email {}: {}", emailRequest.getEmail(), e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to send OTP: " + e.getMessage());
        }
    }

    @PostMapping("/verify-email")
    public ResponseEntity<String> verifyEmailOtp(@RequestBody OtpVerificationDto otpVerificationDto) {
        logger.info("Received request to verify email OTP");
        logger.debug("Email OTP verification details - OTP: {}, Email: {}",
                otpVerificationDto.getOtp(), otpVerificationDto.getEmail());

        try {
            if (otpVerificationDto.getOtp() == null || otpVerificationDto.getOtp().trim().isEmpty()) {
                logger.warn("Invalid OTP provided for email verification");
                return ResponseEntity.badRequest().body("OTP is required");
            }

            if (otpVerificationDto.getEmail() == null || otpVerificationDto.getEmail().trim().isEmpty()) {
                logger.warn("Invalid email provided for OTP verification");
                return ResponseEntity.badRequest().body("Email address is required");
            }

            String otp = otpVerificationDto.getOtp().trim();
            String email = otpVerificationDto.getEmail().trim();

            logger.debug("Verifying email OTP: {} for email: {}", otp, email);
            boolean isValid = otpService.verifyEmailOtp(otpVerificationDto);

            if (isValid) {
                logger.info("Email OTP verified successfully for: {}", email);
                return ResponseEntity.ok("OTP verified successfully");
            } else {
                logger.warn("Email OTP verification failed for: {}", email);
                return ResponseEntity.badRequest().body("Invalid or expired OTP");
            }

        } catch (Exception e) {
            logger.error("Error verifying email OTP for {}: {}",
                    otpVerificationDto.getEmail(), e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Verification failed: " + e.getMessage());
        }
    }

    // Global exception handlers for consistent error responses
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        logger.warn("Invalid request parameter: {}", e.getMessage());
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception e) {
        logger.error("Unexpected error in OTP controller: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An unexpected error occurred: " + e.getMessage());
    }
}