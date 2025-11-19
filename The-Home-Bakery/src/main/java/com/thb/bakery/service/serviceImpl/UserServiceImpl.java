package com.thb.bakery.service.serviceImpl;

import com.thb.bakery.Bcrypt.BcryptEncoderConfig;
import com.thb.bakery.entity.UserEntity;
import com.thb.bakery.repository.UserRepository;
import com.thb.bakery.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BcryptEncoderConfig passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository usersRepository, BcryptEncoderConfig passwordEncoder) {
        this.userRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserEntity createUser(UserEntity user) {

        if(userRepository.existsByMobileOrEmail(user.getMobile(), user.getEmail())) {
            // Need additional checks to determine which field exists
            if(userRepository.findByMobile(user.getMobile()) != null) {
                throw new RuntimeException("Mobile Number Already Exists!!");
            } else {
                throw new RuntimeException("Email Already Exists!!");
            }
        }

        // Set default status if not provided
        if (user.getStatus() == null || user.getStatus().isEmpty()) {
            user.setStatus("active");
        }
        String encodedPassword = passwordEncoder.encode(user.getPassword());

        user.setPassword(encodedPassword);
        return userRepository.save(user);
    }

    @Override
    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Get total count of all users
     * @return Total user count
     */
    public Long getTotalUserCount() {
        return userRepository.count();
    }

    /**
     * Get count of users by status
     * @param status - User status
     * @return User count by status
     */
    public Long getUserCountByStatus(String status) {
        return userRepository.countByStatus(status);
    }

    @Override
    public Optional<UserEntity> authenticateUserByMobileAndPassword(String mobile, String password) {
        UserEntity user = userRepository.findByMobile(mobile);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return Optional.of(user);
        }
        return Optional.empty();
    }

    @Override
    public UserEntity getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
    }

    @Override
    public UserEntity updateUser(Long userId, UserEntity user) {
        UserEntity existingUser = getUserById(userId);

        // Update all fields
        existingUser.setCustomerName(user.getCustomerName());
        existingUser.setEmail(user.getEmail());
        existingUser.setMobile(user.getMobile());
        existingUser.setStatus(user.getStatus());

        return userRepository.save(existingUser);
    }

    @Override
    public UserEntity patchUser(Long userId, Map<String, Object> updates) {

        UserEntity existingUser = getUserById(userId);

        // Apply partial updates - only update provided fields
        updates.forEach((key, value) -> {
            // Skip null values to avoid NullPointerException
            if (value == null) {
                return;
            }

            switch (key) {
                case "customerName":
                    if (value instanceof String) {
                        existingUser.setCustomerName((String) value);
                    }
                    break;
                case "email":
                    if (value instanceof String) {
                        existingUser.setEmail((String) value);
                    }
                    break;
                case "mobile":
                    if (value instanceof String) {
                        existingUser.setMobile((String) value);
                    }
                    break;

                case "status":
                    if (value instanceof String) {
                        existingUser.setStatus((String) value);
                    }
                    break;
                default:
                    // Optionally log unknown fields
                    System.out.println("Unknown field in update: " + key);
                    break;
            }
        });

        return userRepository.save(existingUser);
    }

    @Override
    public boolean changePassword(Long userId, String oldPassword, String newPassword) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        // Verify old password matches
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("Old password is incorrect");
        }

        // Encode and set new password
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        return true;
    }

    @Override
    public void deleteUser(Long userId) {
        UserEntity user = getUserById(userId);
        userRepository.delete(user);
    }

    @Override
    public List<UserEntity> getUsersByStatus(String status) {
        return userRepository.findByStatus(status);
    }

    @Override
    public UserEntity getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Reset password by email without OTP (for emergency/admin use)
     */
    @Override
    public boolean resetPasswordByEmail(String email, String newPassword) {
        try {
            UserEntity user = userRepository.findByEmail(email);
            if (user == null) {
                throw new RuntimeException("User not found with email: " + email);
            }

            String encodedPassword = passwordEncoder.encode(newPassword);
            user.setPassword(encodedPassword);
            userRepository.save(user);

            System.out.println("Password reset successfully for: " + email);
            return true;

        } catch (Exception e) {
            System.err.println("Error resetting password for " + email + ": " + e.getMessage());
            throw new RuntimeException("Failed to reset password: " + e.getMessage());
        }
    }

    @Override
    public boolean resetPasswordWithOtp(String email, String otp, String newPassword) {
        try {
            // 1. Find user by email first
            UserEntity user = userRepository.findByEmail(email);
            if (user == null) {
                throw new RuntimeException("User not found with email: " + email);
            }

            // 2. Verify OTP - Since you don't have OTP service integrated yet,
            // we'll implement a simple verification logic
            boolean isOtpValid = verifyOtp(email, otp);

            if (!isOtpValid) {
                throw new RuntimeException("Invalid OTP or OTP expired");
            }

            // 3. Update password (hash it first)
            String encodedNewPassword = passwordEncoder.encode(newPassword);
            user.setPassword(encodedNewPassword);
            userRepository.save(user);

            // 4. Invalidate OTP after use
            invalidateOtp(email, otp);

            System.out.println("Password reset successfully for user: " + email);
            return true;

        } catch (Exception e) {
            System.err.println("Error resetting password for " + email + ": " + e.getMessage());
            throw new RuntimeException("Failed to reset password: " + e.getMessage());
        }
    }

    // Simple OTP verification - Replace this with your actual OTP service integration
    private boolean verifyOtp(String email, String otp) {
        try {
            // For now, we'll accept any 6-digit OTP for testing
            // In production, integrate with your actual OTP service

            if (otp == null || otp.length() != 6) {
                return false;
            }

            // Basic validation - check if it's a 6-digit number
            if (!otp.matches("\\d{6}")) {
                return false;
            }

            // TODO: Integrate with your actual OTP service
            // Example: return otpService.verifyOtp(email, otp);

            System.out.println("OTP verification for " + email + ": " + otp + " - ACCEPTED (Temporary)");
            return true;

        } catch (Exception e) {
            System.err.println("OTP verification error: " + e.getMessage());
            return false;
        }
    }

    private void invalidateOtp(String email, String otp) {
        // TODO: Integrate with your actual OTP service to invalidate used OTP
        // Example: otpService.invalidateOtp(email, otp);
        System.out.println("OTP invalidated for: " + email);
    }

}