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
import java.util.List;

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
        logger.info("🟢 Marking attendance for staff: {}, Date: {}",
                request.getStaffId(), request.getAttendanceDate());

        try {
            // Validate staff exists
            StaffEntity staff = staffRepository.findById(request.getStaffId())
                    .orElseThrow(() -> {
                        logger.error("🔴 Staff not found with ID: {}", request.getStaffId());
                        return new RuntimeException("Staff not found with ID: " + request.getStaffId());
                    });

            LocalDate today = request.getAttendanceDate() != null ?
                    request.getAttendanceDate() : LocalDate.now();

            // FIXED: Use orElseGet instead of orElse with constructor
            AttendanceEntity attendance = attendanceRepository
                    .findByStaffStaffidAndAttendanceDate(request.getStaffId(), today)
                    .orElseGet(() -> {
                        // Create new attendance with proper initialization
                        AttendanceEntity newAttendance = new AttendanceEntity();
                        newAttendance.setStaff(staff);
                        newAttendance.setAttendanceDate(today);
                        newAttendance.setStatus("ABSENT");
                        newAttendance.setTotalHours(0.0);
                        logger.debug("🆕 Created new attendance record for staff: {}", staff.getName());
                        return newAttendance;
                    });

            // Update attendance based on request
            updateAttendanceEntity(attendance, request);

            // Save attendance
            AttendanceEntity savedAttendance = attendanceRepository.save(attendance);
            logger.info("✅ Attendance marked successfully. Staff: {}, Date: {}, Status: {}",
                    staff.getName(), today, savedAttendance.getStatus());

            return mapToResponse(savedAttendance);

        } catch (RuntimeException e) {
            logger.error("❌ Failed to mark attendance. Staff ID: {}, Error: {}",
                    request.getStaffId(), e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            logger.error("💥 Unexpected error while marking attendance. Staff ID: {}",
                    request.getStaffId(), e);
            throw new RuntimeException("Failed to mark attendance", e);
        }
    }

    @Override
    public AttendanceResponse updateAttendance(Long attendanceId, AttendanceRequest request) {
        logger.info("🟢 Updating attendance. Attendance ID: {}", attendanceId);

        try {
            AttendanceEntity attendance = attendanceRepository.findById(attendanceId)
                    .orElseThrow(() -> {
                        logger.error("🔴 Attendance not found with ID: {}", attendanceId);
                        return new RuntimeException("Attendance not found");
                    });

            updateAttendanceEntity(attendance, request);

            AttendanceEntity updatedAttendance = attendanceRepository.save(attendance);
            logger.info("✅ Attendance updated successfully. ID: {}", attendanceId);

            return mapToResponse(updatedAttendance);

        } catch (Exception e) {
            logger.error("❌ Failed to update attendance. ID: {}, Error: {}",
                    attendanceId, e.getMessage(), e);
            throw new RuntimeException("Failed to update attendance", e);
        }
    }

    @Override
    public List<AttendanceResponse> getAttendanceByStaffAndDateRange(Long staffId, LocalDate startDate, LocalDate endDate) {
        logger.info("🟢 Getting attendance for staff: {}, From: {} To: {}",
                staffId, startDate, endDate);

        try {
            // Validate staff exists
            if (!staffRepository.existsById(staffId)) {
                throw new RuntimeException("Staff not found with ID: " + staffId);
            }

            List<AttendanceEntity> attendanceList = attendanceRepository
                    .findByStaffStaffidAndAttendanceDateBetween(staffId, startDate, endDate);

            logger.info("✅ Found {} attendance records for staff: {}",
                    attendanceList.size(), staffId);

            return attendanceList.stream()
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            logger.error("❌ Failed to get attendance for staff: {}, Error: {}",
                    staffId, e.getMessage(), e);
            throw new RuntimeException("Failed to get attendance records", e);
        }
    }

    @Override
    public List<AttendanceResponse> getAttendanceByDate(LocalDate date) {
        logger.info("🟢 Getting attendance for date: {}", date);

        try {
            List<AttendanceEntity> attendanceList = attendanceRepository.findByAttendanceDate(date);

            logger.info("✅ Found {} attendance records for date: {}",
                    attendanceList.size(), date);

            return attendanceList.stream()
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            logger.error("❌ Failed to get attendance for date: {}, Error: {}",
                    date, e.getMessage(), e);
            throw new RuntimeException("Failed to get attendance records", e);
        }
    }

    @Override
    public AttendanceResponse getTodayAttendance(Long staffId) {
        logger.info("🟢 Getting today's attendance for staff: {}", staffId);

        try {
            LocalDate today = LocalDate.now();

            AttendanceEntity attendance = attendanceRepository
                    .findByStaffStaffidAndAttendanceDate(staffId, today)
                    .orElseThrow(() -> {
                        logger.info("ℹ️ No attendance found for staff: {} today", staffId);
                        return new RuntimeException("No attendance record found for today");
                    });

            return mapToResponse(attendance);

        } catch (Exception e) {
            logger.error("❌ Failed to get today's attendance for staff: {}, Error: {}",
                    staffId, e.getMessage(), e);
            throw new RuntimeException("Failed to get today's attendance", e);
        }
    }

    @Override
    public boolean hasAttendanceForToday(Long staffId) {
        try {
            LocalDate today = LocalDate.now();
            boolean hasAttendance = attendanceRepository.existsByStaffStaffidAndAttendanceDate(staffId, today);
            logger.debug("📅 Staff {} has attendance for today: {}", staffId, hasAttendance);
            return hasAttendance;
        } catch (Exception e) {
            logger.error("❌ Error checking today's attendance for staff: {}", staffId, e);
            return false;
        }
    }

    // Helper method to update attendance entity
    private void updateAttendanceEntity(AttendanceEntity attendance, AttendanceRequest request) {
        if (request.getCheckInTime() != null) {
            attendance.setCheckInTime(request.getCheckInTime());
            logger.debug("📝 Updated check-in time: {}", request.getCheckInTime());
        }

        if (request.getCheckOutTime() != null) {
            attendance.setCheckOutTime(request.getCheckOutTime());
            logger.debug("📝 Updated check-out time: {}", request.getCheckOutTime());
        }

        if (request.getStatus() != null) {
            attendance.setStatus(request.getStatus());
            logger.debug("📝 Updated status: {}", request.getStatus());
        }

        if (request.getNotes() != null) {
            attendance.setNotes(request.getNotes());
            logger.debug("📝 Updated notes");
        }

        // Auto-calculate status if both times are provided
        if (attendance.getCheckInTime() != null && attendance.getCheckOutTime() != null) {
            autoCalculateStatus(attendance);
        }
    }

    // BETTER: Manual status updates always override auto-calculation
    private void autoCalculateStatus(AttendanceEntity attendance) {
        if (attendance.getCheckInTime() != null && attendance.getCheckOutTime() != null) {
            // Calculate hours first
            int checkInMinutes = attendance.getCheckInTime().getHour() * 60 + attendance.getCheckInTime().getMinute();
            int checkOutMinutes = attendance.getCheckOutTime().getHour() * 60 + attendance.getCheckOutTime().getMinute();

            double totalMinutes = checkOutMinutes - checkInMinutes;
            double hours = totalMinutes / 60.0;

            attendance.setTotalHours(hours); // Set the calculated hours

            // Check if status was manually set (not the default ABSENT)
            boolean isManuallySet = !"ABSENT".equals(attendance.getStatus());

            if (!isManuallySet) {
                // Only auto-calculate if status wasn't manually set
                if (hours >= 6.0) {
                    attendance.setStatus("PRESENT");
                    logger.debug("✅ Auto-calculated: PRESENT - {} hours worked", hours);
                } else if (hours >= 3.0) {
                    attendance.setStatus("HALF_DAY");
                    logger.debug("🟡 Auto-calculated: HALF_DAY - {} hours worked", hours);
                } else {
                    attendance.setStatus("ABSENT");
                    logger.debug("🔴 Auto-calculated: ABSENT - {} hours worked", hours);
                }
            } else {
                logger.debug("📝 Manual status preserved: {} ({} hours worked)",
                        attendance.getStatus(), hours);
            }
        }
    }
    // Helper method to map entity to response
    private AttendanceResponse mapToResponse(AttendanceEntity attendance) {
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

        logger.debug("📄 Mapped attendance entity to response for: {}", attendance.getStaff().getName());

        return response;
    }
}
