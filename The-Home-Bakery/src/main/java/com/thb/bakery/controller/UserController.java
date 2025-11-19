package com.thb.bakery.controller;

import com.thb.bakery.entity.UserEntity;
import com.thb.bakery.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserEntity> createUser(@RequestBody UserEntity user) {
        UserEntity createdUser = userService.createUser(user);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @GetMapping("/login")
    public ResponseEntity<UserEntity> authenticateUser(
            @RequestParam("mobile") String mobile,
            @RequestParam("password") String password) {

        Optional<UserEntity> user = userService.authenticateUserByMobileAndPassword(mobile, password);

        return user.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/get-all-user")
    public ResponseEntity<List<UserEntity>> getAllUsers() {
        List<UserEntity> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }


    @GetMapping("/{userId}")
    public ResponseEntity<UserEntity> getUserById(@PathVariable Long userId) {
        UserEntity user = userService.getUserById(userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserEntity> updateUser(@PathVariable Long userId, @RequestBody UserEntity user) {
        UserEntity updatedUser = userService.updateUser(userId, user);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    // NEW: PATCH endpoint for partial updates
    @PatchMapping("/update/{userId}")
    public ResponseEntity<UserEntity> patchUser(@PathVariable Long userId, @RequestBody Map<String, Object> updates) {
        UserEntity patchedUser = userService.patchUser(userId, updates);
        return new ResponseEntity<>(patchedUser, HttpStatus.OK);
    }

    // password changing api
    @PatchMapping("/change-password/{userId}")
    public ResponseEntity<?> changePassword(
            @PathVariable Long userId,
            @RequestParam("oldPassword") String oldPassword,
            @RequestParam("newPassword") String newPassword) {

        try {
            boolean success = userService.changePassword(userId, oldPassword, newPassword);
            return ResponseEntity.ok().body("Password changed successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Get total count of all users
     *
     * @return ResponseEntity with user count
     */
    @GetMapping("/user-count")
    public ResponseEntity<Long> getUserCount() {
        try {
            Long userCount = userService.getTotalUserCount();
            return new ResponseEntity<>(userCount, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(0L, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get count of users by status
     *
     * @param status - User status (active/inactive)
     * @return ResponseEntity with user count by status
     */
    @GetMapping("/user-count/status")
    public ResponseEntity<Long> getUserCountByStatus(@RequestParam String status) {
        try {
            Long userCount = userService.getUserCountByStatus(status);
            return new ResponseEntity<>(userCount, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(0L, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete-user/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<UserEntity>> getUsersByStatus(@PathVariable String status) {
        List<UserEntity> users = userService.getUsersByStatus(status);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserEntity> getUserByEmail(@PathVariable String email) {
        UserEntity user = userService.getUserByEmail(email);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    // NEW: Password reset API (uses OTP verification - no old password required)
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            String otp = request.get("otp");
            String newPassword = request.get("newPassword");

            // Validate required fields
            if (email == null || email.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Email is required");
            }
            if (otp == null || otp.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("OTP is required");
            }
            if (newPassword == null || newPassword.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("New password is required");
            }

            // Call service method to reset password with OTP
            boolean success = userService.resetPasswordWithOtp(email, otp, newPassword);

            if (success) {
                return ResponseEntity.ok().body("Password reset successfully");
            } else {
                return ResponseEntity.badRequest().body("Invalid OTP or OTP expired");
            }

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error resetting password: " + e.getMessage());
        }
    }


}