package com.bakery.The.Home.Bakery.service.serviceImpl;

import com.bakery.The.Home.Bakery.Bcrypt.BcryptEncoderConfig;
import com.bakery.The.Home.Bakery.dto.request.OtpVerificationDto;
import com.bakery.The.Home.Bakery.entity.AdminEntity;
import com.bakery.The.Home.Bakery.entity.OtpEntity;
import com.bakery.The.Home.Bakery.entity.UserEntity;
import com.bakery.The.Home.Bakery.repository.AdminRepository;
import com.bakery.The.Home.Bakery.repository.OtpRepository;
import com.bakery.The.Home.Bakery.repository.UserRepository;
import com.bakery.The.Home.Bakery.service.EmailService;
import com.bakery.The.Home.Bakery.service.OtpService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import java.util.Random;

@Service
public class OtpServiceImpl implements OtpService {

    private final UserRepository userRepository;
    private final AdminRepository adminRepository;
    private final OtpRepository otpRepository;
    private final BcryptEncoderConfig passwordEncoder;
    private final EmailService emailService;

    public OtpServiceImpl(UserRepository userRepository, AdminRepository adminRepository,
                          OtpRepository otpRepository, BcryptEncoderConfig passwordEncoder,
                          EmailService emailService) {
        this.userRepository = userRepository;
        this.adminRepository = adminRepository;
        this.otpRepository = otpRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @Override
    @Transactional
    public void sendOtp(String mobile) {
        // Clean expired OTPs
        otpRepository.deleteExpiredOtps(LocalDateTime.now());

        // Find user or admin
        UserEntity user = userRepository.findByMobile(mobile);
        AdminEntity admin = null;

        if (user == null) {
            admin = adminRepository.findByMobile(mobile)
                    .orElseThrow(() -> new RuntimeException("User/Admin not found with mobile: " + mobile));
        }

        // Delete previous OTPs for this mobile
        otpRepository.deleteByMobile(mobile);

        // Generate 6-digit OTP
        String otp = String.format("%06d", new Random().nextInt(999999));
        String hashedOtp = passwordEncoder.encode(otp);

        // Create and save OTP
        OtpEntity otpEntity = new OtpEntity(
                user, admin, hashedOtp, mobile,
                LocalDateTime.now().plusMinutes(5), null, "PASSWORD_RESET"
        );

        otpRepository.save(otpEntity);

        // Send OTP via SMS (implement your SMS service)
        // smsService.sendSms(mobileNumber, "Your OTP is: " + otp + ". Valid for 5 minutes.");
        System.out.println("SMS OTP for " + mobile + ": " + otp);
    }

    @Override
    @Transactional
    public boolean verifyOtp(OtpVerificationDto otpVerificationDto, String mobile) {
        // Clean expired OTPs
        otpRepository.deleteExpiredOtps(LocalDateTime.now());

        String mobileNumber = otpVerificationDto.getMobile();
        List<OtpEntity> validOtps = otpRepository.findValidMobileOtps(mobile, LocalDateTime.now());

        if (validOtps.isEmpty()) {
            return false;
        }

        // Check if any OTP matches
        boolean otpMatched = validOtps.stream()
                .anyMatch(otp -> passwordEncoder.matches(otpVerificationDto.getOtp(), otp.getOtpCode()));

        if (otpMatched) {
            // Find user or admin
            UserEntity user = userRepository.findByMobile(mobile);
            AdminEntity admin = null;

            if (user == null) {
                admin = adminRepository.findByMobile(mobile)
                        .orElseThrow(() -> new RuntimeException("User/Admin not found"));
            }

            // Update password if provided
            if (otpVerificationDto.getNewPassword() != null && !otpVerificationDto.getNewPassword().isEmpty()) {
                String encodedPassword = passwordEncoder.encode(otpVerificationDto.getNewPassword());
                if (user != null) {
                    user.setPassword(encodedPassword);
                    userRepository.save(user);
                } else if (admin != null) {
                    admin.setPassword(encodedPassword);
                    adminRepository.save(admin);
                }
            }

            // Mark OTPs as used
            validOtps.forEach(otp -> {
                otp.setUsed(true);
                otpRepository.save(otp);
            });

            return true;
        }

        return false;
    }

    @Override
    @Transactional
    public void sendOtpEmail(String email) {
        // Clean expired OTPs
        otpRepository.deleteExpiredOtps(LocalDateTime.now());

        // Find user or admin
        UserEntity user = userRepository.findByEmail(email);
        AdminEntity admin = null;

        if (user == null) {
            admin = adminRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User/Admin not found with email: " + email));
        }

        // Delete previous OTPs for this email
        otpRepository.deleteByEmail(email);

        // Generate 6-digit OTP
        String otp = String.format("%06d", new Random().nextInt(999999));
        String hashedOtp = passwordEncoder.encode(otp);

        // Create and save OTP
        OtpEntity otpEntity = new OtpEntity(
                user, admin, hashedOtp, null,
                LocalDateTime.now().plusMinutes(5), email, "PASSWORD_RESET"
        );

        otpRepository.save(otpEntity);

        // Send OTP via Email
        String subject = "Your OTP for Password Reset";
        String message = "Your OTP is: " + otp + ". Valid for 5 minutes.";
        emailService.sendOtpEmail(email, subject, message);
    }

    @Override
    @Transactional
    public boolean verifyEmailOtp(OtpVerificationDto otpVerificationDto) {
        // Clean expired OTPs
        otpRepository.deleteExpiredOtps(LocalDateTime.now());

        String email = otpVerificationDto.getEmail();
        List<OtpEntity> validOtps = otpRepository.findValidEmailOtps(email, LocalDateTime.now());

        if (validOtps.isEmpty()) {
            return false;
        }

        // Check if any OTP matches
        boolean otpMatched = validOtps.stream()
                .anyMatch(otp -> passwordEncoder.matches(otpVerificationDto.getOtp(), otp.getOtpCode()));

        if (otpMatched) {
            // Find user or admin
            UserEntity user = userRepository.findByEmail(email);
            AdminEntity admin = null;

            if (user == null) {
                admin = adminRepository.findByEmail(email)
                        .orElseThrow(() -> new RuntimeException("User/Admin not found"));
            }

            // Update password if provided
            if (otpVerificationDto.getNewPassword() != null && !otpVerificationDto.getNewPassword().isEmpty()) {
                String encodedPassword = passwordEncoder.encode(otpVerificationDto.getNewPassword());
                if (user != null) {
                    user.setPassword(encodedPassword);
                    userRepository.save(user);
                } else if (admin != null) {
                    admin.setPassword(encodedPassword);
                    adminRepository.save(admin);
                }
            }

            // Mark OTPs as used
            validOtps.forEach(otp -> {
                otp.setUsed(true);
                otpRepository.save(otp);
            });

            return true;
        }

        return false;
    }
}