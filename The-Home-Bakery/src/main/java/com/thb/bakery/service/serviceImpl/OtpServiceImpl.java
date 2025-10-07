package com.thb.bakery.service.serviceImpl;

import com.thb.bakery.Bcrypt.BcryptEncoderConfig;
import com.thb.bakery.dto.request.OtpVerificationDto;
import com.thb.bakery.entity.AdminEntity;
import com.thb.bakery.entity.OtpEntity;
import com.thb.bakery.entity.UserEntity;
import com.thb.bakery.repository.AdminRepository;
import com.thb.bakery.repository.OtpRepository;
import com.thb.bakery.repository.UserRepository;
import com.thb.bakery.service.EmailService;
import com.thb.bakery.service.OtpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
public class OtpServiceImpl implements OtpService {

    private static final Logger logger = LoggerFactory.getLogger(OtpServiceImpl.class);

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
        logger.info("Initiating OTP send for mobile: {}", mobile);
        try {
            // Clean expired OTPs
            otpRepository.deleteExpiredOtps(LocalDateTime.now());
            logger.debug("Expired OTPs cleaned for mobile: {}", mobile);

            // Find user or admin
            UserEntity user = userRepository.findByMobile(mobile);
            AdminEntity admin = null;

            if (user == null) {
                admin = adminRepository.findByMobile(mobile)
                        .orElseThrow(() -> {
                            logger.error("User/Admin not found with mobile: {}", mobile);
                            return new RuntimeException("User/Admin not found with mobile: " + mobile);
                        });
                logger.info("Found admin with mobile: {}", mobile);
            } else {
                logger.info("Found user with mobile: {}", mobile);
            }

            // Delete previous OTPs for this mobile
            otpRepository.deleteByMobile(mobile);
            logger.debug("Previous OTPs deleted for mobile: {}", mobile);

            // Generate 6-digit OTP
            String otp = String.format("%06d", new Random().nextInt(999999));
            String hashedOtp = passwordEncoder.encode(otp);
            logger.debug("Generated OTP for mobile: {}", mobile);

            // Create and save OTP
            OtpEntity otpEntity = new OtpEntity(
                    user, admin, hashedOtp, mobile,
                    LocalDateTime.now().plusMinutes(5), null, "PASSWORD_RESET"
            );

            otpRepository.save(otpEntity);
            logger.info("OTP saved for mobile: {}", mobile);

            // Send OTP via SMS (implement your SMS service)
            // smsService.sendSms(mobileNumber, "Your OTP is: " + otp + ". Valid for 5 minutes.");
            logger.info("SMS OTP for {}: {}", mobile, otp);
            System.out.println("SMS OTP for " + mobile + ": " + otp);
        } catch (Exception e) {
            logger.error("Failed to send OTP for mobile: {}", mobile, e);
            throw e;
        }
    }

    @Override
    @Transactional
    public boolean verifyOtp(OtpVerificationDto otpVerificationDto, String mobile) {
        logger.info("Verifying OTP for mobile: {}", mobile);
        try {
            // Clean expired OTPs
            otpRepository.deleteExpiredOtps(LocalDateTime.now());
            logger.debug("Expired OTPs cleaned for mobile: {}", mobile);

            String mobileNumber = otpVerificationDto.getMobile();
            List<OtpEntity> validOtps = otpRepository.findValidMobileOtps(mobile, LocalDateTime.now());

            if (validOtps.isEmpty()) {
                logger.warn("No valid OTPs found for mobile: {}", mobile);
                return false;
            }

            // Check if any OTP matches
            boolean otpMatched = validOtps.stream()
                    .anyMatch(otp -> passwordEncoder.matches(otpVerificationDto.getOtp(), otp.getOtpCode()));
            logger.debug("OTP match result for mobile {}: {}", mobile, otpMatched);

            if (otpMatched) {
                // Find user or admin
                UserEntity user = userRepository.findByMobile(mobile);
                AdminEntity admin = null;

                if (user == null) {
                    admin = adminRepository.findByMobile(mobile)
                            .orElseThrow(() -> {
                                logger.error("User/Admin not found with mobile: {}", mobile);
                                return new RuntimeException("User/Admin not found");
                            });
                    logger.info("Found admin with mobile: {}", mobile);
                } else {
                    logger.info("Found user with mobile: {}", mobile);
                }

                // Update password if provided
                if (otpVerificationDto.getNewPassword() != null && !otpVerificationDto.getNewPassword().isEmpty()) {
                    String encodedPassword = passwordEncoder.encode(otpVerificationDto.getNewPassword());
                    if (user != null) {
                        user.setPassword(encodedPassword);
                        userRepository.save(user);
                        logger.info("Password updated for user with mobile: {}", mobile);
                    } else if (admin != null) {
                        admin.setPassword(encodedPassword);
                        adminRepository.save(admin);
                        logger.info("Password updated for admin with mobile: {}", mobile);
                    }
                }

                // Mark OTPs as used
                validOtps.forEach(otp -> {
                    otp.setUsed(true);
                    otpRepository.save(otp);
                });
                logger.debug("Valid OTPs marked as used for mobile: {}", mobile);

                return true;
            }

            logger.warn("OTP verification failed for mobile: {}", mobile);
            return false;
        } catch (Exception e) {
            logger.error("Failed to verify OTP for mobile: {}", mobile, e);
            throw e;
        }
    }

    @Override
    @Transactional
    public void sendOtpEmail(String email) {
        logger.info("Initiating OTP send for email: {}", email);
        try {
            // Clean expired OTPs
            otpRepository.deleteExpiredOtps(LocalDateTime.now());
            logger.debug("Expired OTPs cleaned for email: {}", email);

            // Find user or admin
            UserEntity user = userRepository.findByEmail(email);
            AdminEntity admin = null;

            if (user == null) {
                admin = adminRepository.findByEmail(email)
                        .orElseThrow(() -> {
                            logger.error("User/Admin not found with email: {}", email);
                            return new RuntimeException("User/Admin not found with email: " + email);
                        });
                logger.info("Found admin with email: {}", email);
            } else {
                logger.info("Found user with email: {}", email);
            }

            // Delete previous OTPs for this email
            otpRepository.deleteByEmail(email);
            logger.debug("Previous OTPs deleted for email: {}", email);

            // Generate 6-digit OTP
            String otp = String.format("%06d", new Random().nextInt(999999));
            String hashedOtp = passwordEncoder.encode(otp);
            logger.debug("Generated OTP for email: {}", email);

            // Create and save OTP
            OtpEntity otpEntity = new OtpEntity(
                    user, admin, hashedOtp, null,
                    LocalDateTime.now().plusMinutes(5), email, "PASSWORD_RESET"
            );

            otpRepository.save(otpEntity);
            logger.info("OTP saved for email: {}", email);

            // Send OTP via Email
            String subject = "Your OTP for Password Reset";
            String message = "Your OTP is: " + otp + ". Valid for 5 minutes.";
            emailService.sendOtpEmail(email, subject, message);
            logger.info("OTP email sent to: {}", email);
        } catch (Exception e) {
            logger.error("Failed to send OTP email to: {}", email, e);
            throw e;
        }
    }

    @Override
    @Transactional
    public boolean verifyEmailOtp(OtpVerificationDto otpVerificationDto) {
        logger.info("Verifying OTP for email: {}", otpVerificationDto.getEmail());
        try {
            // Clean expired OTPs
            otpRepository.deleteExpiredOtps(LocalDateTime.now());
            logger.debug("Expired OTPs cleaned for email: {}", otpVerificationDto.getEmail());

            String email = otpVerificationDto.getEmail();
            List<OtpEntity> validOtps = otpRepository.findValidEmailOtps(email, LocalDateTime.now());

            if (validOtps.isEmpty()) {
                logger.warn("No valid OTPs found for email: {}", email);
                return false;
            }

            // Check if any OTP matches
            boolean otpMatched = validOtps.stream()
                    .anyMatch(otp -> passwordEncoder.matches(otpVerificationDto.getOtp(), otp.getOtpCode()));
            logger.debug("OTP match result for email {}: {}", email, otpMatched);

            if (otpMatched) {
                // Find user or admin
                UserEntity user = userRepository.findByEmail(email);
                AdminEntity admin = null;

                if (user == null) {
                    admin = adminRepository.findByEmail(email)
                            .orElseThrow(() -> {
                                logger.error("User/Admin not found with email: {}", email);
                                return new RuntimeException("User/Admin not found");
                            });
                    logger.info("Found admin with email: {}", email);
                } else {
                    logger.info("Found user with email: {}", email);
                }

                // Update password if provided
                if (otpVerificationDto.getNewPassword() != null && !otpVerificationDto.getNewPassword().isEmpty()) {
                    String encodedPassword = passwordEncoder.encode(otpVerificationDto.getNewPassword());
                    if (user != null) {
                        user.setPassword(encodedPassword);
                        userRepository.save(user);
                        logger.info("Password updated for user with email: {}", email);
                    } else if (admin != null) {
                        admin.setPassword(encodedPassword);
                        adminRepository.save(admin);
                        logger.info("Password updated for admin with email: {}", email);
                    }
                }

                // Mark OTPs as used
                validOtps.forEach(otp -> {
                    otp.setUsed(true);
                    otpRepository.save(otp);
                });
                logger.debug("Valid OTPs marked as used for email: {}", email);

                return true;
            }

            logger.warn("OTP verification failed for email: {}", email);
            return false;
        } catch (Exception e) {
            logger.error("Failed to verify OTP for email: {}", otpVerificationDto.getEmail(), e);
            throw e;
        }
    }
}

