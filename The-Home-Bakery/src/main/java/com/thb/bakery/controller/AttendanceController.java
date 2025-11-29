package com.thb.bakery.controller;

import com.thb.bakery.dto.request.AttendanceRequest;
import com.thb.bakery.dto.response.AttendanceResponse;
import com.thb.bakery.service.AttendanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/attendance") // Fixed: "/api/attendance"
public class AttendanceController {

    private static final Logger logger = LoggerFactory.getLogger(AttendanceController.class);

    @Autowired
    private AttendanceService attendanceService;

    // Mark attendance (check-in/check-out)
    @PostMapping("/mark")
    public ResponseEntity<?> markAttendance(@RequestBody AttendanceRequest request) {
        logger.info("📥 Received attendance mark request for staff: {}", request.getStaffId());

        try {
            AttendanceResponse response = attendanceService.markAttendance(request);
            logger.info("✅ Attendance marked successfully for staff: {}", request.getStaffId());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("❌ Error marking attendance for staff: {}", request.getStaffId(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Update attendance
    @PutMapping("/update/{attendanceId}")
    public ResponseEntity<?> updateAttendance(
            @PathVariable Long attendanceId,
            @RequestBody AttendanceRequest request) {
        logger.info("📥 Received attendance update request for ID: {}", attendanceId);

        try {
            AttendanceResponse response = attendanceService.updateAttendance(attendanceId, request);
            logger.info("✅ Attendance updated successfully for ID: {}", attendanceId);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("❌ Error updating attendance ID: {}", attendanceId, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Get attendance by staff and date range
    @GetMapping("/staff/{staffId}")
    public ResponseEntity<?> getStaffAttendance(
            @PathVariable Long staffId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        logger.info("📥 Getting attendance for staff: {} from {} to {}", staffId, startDate, endDate);

        try {
            List<AttendanceResponse> response = attendanceService
                    .getAttendanceByStaffAndDateRange(staffId, startDate, endDate);
            logger.info("✅ Found {} attendance records for staff: {}", response.size(), staffId);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("❌ Error getting attendance for staff: {}", staffId, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Get attendance by date (all staff)
    @GetMapping("/date/{date}")
    public ResponseEntity<?> getAttendanceByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        logger.info("📥 Getting attendance for date: {}", date);

        try {
            List<AttendanceResponse> response = attendanceService.getAttendanceByDate(date);
            logger.info("✅ Found {} attendance records for date: {}", response.size(), date);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("❌ Error getting attendance for date: {}", date, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Get today's attendance for a staff
    @GetMapping("/today/{staffId}")
    public ResponseEntity<?> getTodayAttendance(@PathVariable Long staffId) {
        logger.info("📥 Getting today's attendance for staff: {}", staffId);

        try {
            AttendanceResponse response = attendanceService.getTodayAttendance(staffId);
            logger.info("✅ Found today's attendance for staff: {}", staffId);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.info("ℹ️ No attendance found for staff: {} today", staffId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "No attendance found for today"));
        }
    }

    // Check if staff has attendance for today
    @GetMapping("/check-today/{staffId}")
    public ResponseEntity<?> hasAttendanceForToday(@PathVariable Long staffId) {
        logger.info("📥 Checking if staff: {} has attendance for today", staffId);

        try {
            boolean hasAttendance = attendanceService.hasAttendanceForToday(staffId);
            logger.info("✅ Staff: {} has attendance for today: {}", staffId, hasAttendance);
            return ResponseEntity.ok(hasAttendance);

        } catch (Exception e) {
            logger.error("❌ Error checking today's attendance for staff: {}", staffId, e);
            return ResponseEntity.ok(false);
        }
    }

    // CHANGE 1: Monthly Attendance (Line ~95 in your file)
    // FIXED: Monthly Attendance endpoint
    @GetMapping("/monthly/{staffId}")
    public ResponseEntity<?> getMonthlyAttendance(
            @PathVariable Long staffId,
            @RequestParam("month") String monthStr) {

        logger.info("Getting monthly attendance for staff: {}, month: {}", staffId, monthStr);

        try {
            // Validate month format
            if (!monthStr.matches("\\d{4}-\\d{2}")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Invalid month format. Use YYYY-MM (e.g. 2025-11)"));
            }

            // Parse the month properly
            LocalDate month = LocalDate.parse(monthStr + "-01");
            List<AttendanceResponse> response = attendanceService.getMonthlyAttendance(staffId, month);
            logger.info("Found {} attendance records for staff: {} in month: {}", response.size(), staffId, monthStr);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error getting monthly attendance for staff: {}", staffId, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Invalid month format. Use YYYY-MM (e.g. 2025-11)"));
        }
    }
    // CHANGE 2: Attendance Summary (Line ~115 in your file)
    @GetMapping("/summary/{staffId}")
    public ResponseEntity<?> getAttendanceSummary(
            @PathVariable Long staffId,
            @RequestParam("month") String monthStr) {   // ← Change LocalDate → String + remove @DateTimeFormat

        logger.info("Getting attendance summary for staff: {}, month: {}", staffId, monthStr);

        try {
            LocalDate month = LocalDate.parse(monthStr + "-01");  // ← Add this line
            Map<String, Object> summary = attendanceService.getAttendanceSummary(staffId, month);
            logger.info("Attendance summary generated for staff: {} in month: {}", staffId, monthStr);
            return ResponseEntity.ok(summary);

        } catch (Exception e) {
            logger.error("Error getting attendance summary for staff: {}", staffId, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Invalid month format. Use YYYY-MM"));
        }
    }

    // Bulk mark attendance for multiple staff
    @PostMapping("/bulk-mark")
    public ResponseEntity<?> bulkMarkAttendance(@RequestBody List<AttendanceRequest> requests) {
        logger.info("📥 Received bulk attendance mark request for {} staff", requests.size());

        try {
            List<AttendanceResponse> responses = attendanceService.bulkMarkAttendance(requests);
            logger.info("✅ Bulk attendance marked successfully for {} staff", responses.size());
            return ResponseEntity.ok(responses);

        } catch (Exception e) {
            logger.error("❌ Error in bulk attendance marking", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Quick check-in endpoint
    @PostMapping("/check-in/{staffId}")
    public ResponseEntity<?> checkIn(@PathVariable Long staffId) {
        logger.info("🟢 Staff: {} is checking in", staffId);

        try {
            AttendanceRequest request = new AttendanceRequest();
            request.setStaffId(staffId);
            request.setAttendanceDate(LocalDate.now());
            request.setCheckInTime(java.time.LocalTime.now());
            request.setStatus("PRESENT");

            AttendanceResponse response = attendanceService.markAttendance(request);
            logger.info("✅ Check-in successful for staff: {}", staffId);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("❌ Error during check-in for staff: {}", staffId, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Quick check-out endpoint
    @PostMapping("/check-out/{staffId}")
    public ResponseEntity<?> checkOut(@PathVariable Long staffId) {
        logger.info("🔴 Staff: {} is checking out", staffId);

        try {
            // First get today's attendance
            AttendanceResponse todayAttendance = attendanceService.getTodayAttendance(staffId);

            if (todayAttendance == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "No check-in found for today"));
            }

            AttendanceRequest request = new AttendanceRequest();
            request.setStaffId(staffId);
            request.setCheckOutTime(java.time.LocalTime.now());

            AttendanceResponse response = attendanceService.updateAttendance(
                    todayAttendance.getAttendanceId(), request);

            logger.info("✅ Check-out successful for staff: {}", staffId);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("❌ Error during check-out for staff: {}", staffId, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
