package com.thb.bakery.service.serviceImpl;

import com.thb.bakery.dto.request.AttendanceRequest;
import com.thb.bakery.dto.response.AttendanceResponse;
import com.thb.bakery.entity.AttendanceEntity;
import com.thb.bakery.entity.StaffEntity;
import com.thb.bakery.repository.AttendanceRepository;
import com.thb.bakery.repository.StaffRepository;
import com.thb.bakery.service.AttendanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    private static final Logger logger = LoggerFactory.getLogger(AttendanceServiceImpl.class);

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private StaffRepository staffRepository;

    @Override
    public AttendanceResponse markAttendance(AttendanceRequest request) {
        logger.info("Marking attendance for staff: {} on date: {}", request.getStaffId(), request.getAttendanceDate());

        try {
            // Validate staff exists
            StaffEntity staff = staffRepository.findById(request.getStaffId())
                    .orElseThrow(() -> new RuntimeException("Staff not found with ID: " + request.getStaffId()));

            // Check if attendance already exists for this date
            Optional<AttendanceEntity> existingAttendance = attendanceRepository
                    .findByStaffStaffidAndAttendanceDate(request.getStaffId(), request.getAttendanceDate());

            AttendanceEntity attendance;

            if (existingAttendance.isPresent()) {
                // Update existing attendance
                attendance = existingAttendance.get();
                logger.info("Updating existing attendance for staff: {} on date: {}",
                        request.getStaffId(), request.getAttendanceDate());
            } else {
                // Create new attendance
                attendance = new AttendanceEntity();
                attendance.setStaff(staff);
                attendance.setAttendanceDate(request.getAttendanceDate());
                logger.info("Creating new attendance for staff: {} on date: {}",
                        request.getStaffId(), request.getAttendanceDate());
            }

            // Update fields
            if (request.getCheckInTime() != null) {
                attendance.setCheckInTime(request.getCheckInTime());
            }
            if (request.getCheckOutTime() != null) {
                attendance.setCheckOutTime(request.getCheckOutTime());
            }
            if (request.getStatus() != null) {
                attendance.setStatus(request.getStatus());
            }
            if (request.getNotes() != null) {
                attendance.setNotes(request.getNotes());
            }

            // Calculate total hours if both check-in and check-out are provided
            if (attendance.getCheckInTime() != null && attendance.getCheckOutTime() != null) {
                calculateTotalHours(attendance);
            }

            // Save attendance
            AttendanceEntity savedAttendance = attendanceRepository.save(attendance);
            logger.info("Attendance saved successfully for staff: {} on date: {}",
                    request.getStaffId(), request.getAttendanceDate());

            return convertToResponse(savedAttendance);

        } catch (Exception e) {
            logger.error("Error marking attendance for staff: {}", request.getStaffId(), e);
            throw new RuntimeException("Failed to mark attendance: " + e.getMessage());
        }
    }

    @Override
    public AttendanceResponse updateAttendance(Long attendanceId, AttendanceRequest request) {
        logger.info("Updating attendance with ID: {}", attendanceId);

        try {
            AttendanceEntity attendance = attendanceRepository.findById(attendanceId)
                    .orElseThrow(() -> new RuntimeException("Attendance not found with ID: " + attendanceId));

            // Update fields
            if (request.getCheckInTime() != null) {
                attendance.setCheckInTime(request.getCheckInTime());
            }
            if (request.getCheckOutTime() != null) {
                attendance.setCheckOutTime(request.getCheckOutTime());
            }
            if (request.getStatus() != null) {
                attendance.setStatus(request.getStatus());
            }
            if (request.getNotes() != null) {
                attendance.setNotes(request.getNotes());
            }

            // Calculate total hours if both check-in and check-out are provided
            if (attendance.getCheckInTime() != null && attendance.getCheckOutTime() != null) {
                calculateTotalHours(attendance);
            }

            AttendanceEntity updatedAttendance = attendanceRepository.save(attendance);
            logger.info("Attendance updated successfully with ID: {}", attendanceId);

            return convertToResponse(updatedAttendance);

        } catch (Exception e) {
            logger.error("Error updating attendance with ID: {}", attendanceId, e);
            throw new RuntimeException("Failed to update attendance: " + e.getMessage());
        }
    }

    @Override
    public List<AttendanceResponse> getAttendanceByStaffAndDateRange(Long staffId, LocalDate startDate, LocalDate endDate) {
        logger.info("Getting attendance for staff: {} from {} to {}", staffId, startDate, endDate);

        try {
            List<AttendanceEntity> attendanceList = attendanceRepository
                    .findByStaffStaffidAndAttendanceDateBetween(staffId, startDate, endDate);

            return attendanceList.stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            logger.error("Error getting attendance for staff: {}", staffId, e);
            throw new RuntimeException("Failed to get attendance: " + e.getMessage());
        }
    }

    @Override
    public List<AttendanceResponse> getAttendanceByDate(LocalDate date) {
        logger.info("Getting attendance for date: {}", date);

        try {
            List<AttendanceEntity> attendanceList = attendanceRepository.findByAttendanceDate(date);

            return attendanceList.stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            logger.error("Error getting attendance for date: {}", date, e);
            throw new RuntimeException("Failed to get attendance: " + e.getMessage());
        }
    }

    @Override
    public AttendanceResponse getTodayAttendance(Long staffId) {
        logger.info("Getting today's attendance for staff: {}", staffId);

        try {
            LocalDate today = LocalDate.now();
            Optional<AttendanceEntity> attendance = attendanceRepository
                    .findByStaffStaffidAndAttendanceDate(staffId, today);

            if (attendance.isPresent()) {
                return convertToResponse(attendance.get());
            } else {
                throw new RuntimeException("No attendance found for today");
            }

        } catch (Exception e) {
            logger.error("Error getting today's attendance for staff: {}", staffId, e);
            throw new RuntimeException("Failed to get today's attendance: " + e.getMessage());
        }
    }

    @Override
    public boolean hasAttendanceForToday(Long staffId) {
        logger.info("Checking if staff: {} has attendance for today", staffId);

        try {
            LocalDate today = LocalDate.now();
            return attendanceRepository.existsByStaffStaffidAndAttendanceDate(staffId, today);

        } catch (Exception e) {
            logger.error("Error checking today's attendance for staff: {}", staffId, e);
            return false;
        }
    }

    @Override
    public List<AttendanceResponse> getMonthlyAttendance(Long staffId, LocalDate month) {
        logger.info("🟢 Getting monthly attendance for staff: {}, month: {}", staffId, month);

        try {
            // The month parameter is a LocalDate representing the first day of the month (e.g., "2025-11-01")
            // Calculate start and end dates for the month
            LocalDate startDate = month.withDayOfMonth(1);
            LocalDate endDate = month.withDayOfMonth(month.lengthOfMonth());

            logger.info("📅 Querying database for date range: {} to {}", startDate, endDate);

            // Use the existing method that we know works
            List<AttendanceResponse> attendanceList = getAttendanceByStaffAndDateRange(staffId, startDate, endDate);

            logger.info("✅ Found {} attendance records for staff: {} in month: {}",
                    attendanceList.size(), staffId, month);

            // Log sample records for debugging
            if (!attendanceList.isEmpty()) {
                attendanceList.stream()
                        .limit(3)
                        .forEach(record ->
                                logger.info("📊 Sample record - Date: {}, Status: {}",
                                        record.getAttendanceDate(), record.getStatus())
                        );
            }

            return attendanceList;

        } catch (Exception e) {
            logger.error("❌ Error getting monthly attendance for staff: {}, month: {}", staffId, month, e);
            throw new RuntimeException("Failed to get monthly attendance for month: " + month + ". Error: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> getAttendanceSummary(Long staffId, LocalDate month) {
        logger.info("Getting attendance summary for staff: {}, month: {}", staffId, month);

        try {
            List<AttendanceResponse> monthlyAttendance = getMonthlyAttendance(staffId, month);

            // Calculate total days in the month
            int totalDaysInMonth = month.lengthOfMonth();

            // Count different status types
            long presentCount = monthlyAttendance.stream()
                    .filter(a -> "PRESENT".equalsIgnoreCase(a.getStatus()))
                    .count();

            long absentCount = monthlyAttendance.stream()
                    .filter(a -> "ABSENT".equalsIgnoreCase(a.getStatus()))
                    .count();

            long lateCount = monthlyAttendance.stream()
                    .filter(a -> "LATE".equalsIgnoreCase(a.getStatus()))
                    .count();

            long leaveCount = monthlyAttendance.stream()
                    .filter(a -> "LEAVE".equalsIgnoreCase(a.getStatus()))
                    .count();

            // Calculate not marked days
            long notMarkedCount = totalDaysInMonth - monthlyAttendance.size();

            Map<String, Object> summary = new HashMap<>();
            summary.put("presentDays", presentCount);
            summary.put("absentDays", absentCount);
            summary.put("lateDays", lateCount);
            summary.put("leaveDays", leaveCount);
            summary.put("notMarkedDays", notMarkedCount);
            summary.put("totalWorkingDays", totalDaysInMonth);
            summary.put("month", month.toString().substring(0, 7)); // Return only YYYY-MM
            summary.put("staffId", staffId);

            logger.info("Attendance summary generated for staff: {} - Present: {}, Absent: {}, Late: {}, Leave: {}, Not Marked: {}",
                    staffId, presentCount, absentCount, lateCount, leaveCount, notMarkedCount);

            return summary;

        } catch (Exception e) {
            logger.error("Error generating attendance summary for staff: {}", staffId, e);
            throw new RuntimeException("Failed to generate attendance summary: " + e.getMessage());
        }
    }

    @Override
    public List<AttendanceResponse> bulkMarkAttendance(List<AttendanceRequest> requests) {
        logger.info("Processing bulk attendance marking for {} requests", requests.size());

        try {
            List<AttendanceResponse> responses = new ArrayList<>();

            for (AttendanceRequest request : requests) {
                try {
                    AttendanceResponse response = markAttendance(request);
                    responses.add(response);
                } catch (Exception e) {
                    logger.error("Error marking attendance for staff: {}", request.getStaffId(), e);
                    // Continue with other requests even if one fails
                }
            }

            logger.info("Bulk attendance marking completed. Successfully processed: {} out of {}",
                    responses.size(), requests.size());

            return responses;

        } catch (Exception e) {
            logger.error("Error in bulk attendance marking", e);
            throw new RuntimeException("Failed to process bulk attendance: " + e.getMessage());
        }
    }

    // Temporary debug method - can be removed after testing
    public Map<String, Object> debugMonthlyAttendance(Long staffId, LocalDate month) {
        Map<String, Object> debugInfo = new HashMap<>();

        debugInfo.put("staffId", staffId);
        debugInfo.put("inputMonth", month.toString());
        debugInfo.put("monthClass", month.getClass().getSimpleName());
        debugInfo.put("year", month.getYear());
        debugInfo.put("monthValue", month.getMonthValue());

        try {
            LocalDate startDate = month.withDayOfMonth(1);
            LocalDate endDate = month.withDayOfMonth(month.lengthOfMonth());

            debugInfo.put("startDate", startDate.toString());
            debugInfo.put("endDate", endDate.toString());

            List<AttendanceEntity> records = attendanceRepository
                    .findByStaffStaffidAndAttendanceDateBetween(staffId, startDate, endDate);

            debugInfo.put("recordsFound", records.size());
            debugInfo.put("sampleRecords", records.stream()
                    .limit(3)
                    .map(r -> Map.of(
                            "date", r.getAttendanceDate().toString(),
                            "status", r.getStatus()
                    ))
                    .collect(Collectors.toList()));

        } catch (Exception e) {
            debugInfo.put("error", e.getMessage());
            debugInfo.put("errorType", e.getClass().getSimpleName());
        }

        return debugInfo;
    }

    private void calculateTotalHours(AttendanceEntity attendance) {
        if (attendance.getCheckInTime() != null && attendance.getCheckOutTime() != null) {
            int checkInMinutes = attendance.getCheckInTime().getHour() * 60 + attendance.getCheckInTime().getMinute();
            int checkOutMinutes = attendance.getCheckOutTime().getHour() * 60 + attendance.getCheckOutTime().getMinute();

            double totalMinutes = checkOutMinutes - checkInMinutes;
            attendance.setTotalHours(totalMinutes / 60.0);

            // Auto-update status based on hours if not already set
            if (attendance.getStatus() == null) {
                if (attendance.getTotalHours() >= 6) {
                    attendance.setStatus("PRESENT");
                } else if (attendance.getTotalHours() >= 3) {
                    attendance.setStatus("HALF_DAY");
                } else {
                    attendance.setStatus("ABSENT");
                }
            }
        }
    }

    private AttendanceResponse convertToResponse(AttendanceEntity attendance) {
        AttendanceResponse response = new AttendanceResponse();
        response.setAttendanceId(attendance.getAttendanceId());
        response.setStaffId(attendance.getStaff().getStaffid());
        response.setStaffName(attendance.getStaff().getName());
        response.setAttendanceDate(attendance.getAttendanceDate());
        response.setCheckInTime(attendance.getCheckInTime());
        response.setCheckOutTime(attendance.getCheckOutTime());
        response.setTotalHours(attendance.getTotalHours());
        response.setStatus(attendance.getStatus());
        response.setNotes(attendance.getNotes());
        return response;
    }
}
