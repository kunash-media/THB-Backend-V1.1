package com.thb.bakery.service;

import com.thb.bakery.dto.request.AttendanceRequest;
import com.thb.bakery.dto.response.AttendanceResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface AttendanceService {

    // Mark attendance (check-in/check-out)
    AttendanceResponse markAttendance(AttendanceRequest request);

    // Update existing attendance
    AttendanceResponse updateAttendance(Long attendanceId, AttendanceRequest request);

    // Get attendance by staff and date range
    List<AttendanceResponse> getAttendanceByStaffAndDateRange(Long staffId, LocalDate startDate, LocalDate endDate);

    // Get all attendance for a specific date
    List<AttendanceResponse> getAttendanceByDate(LocalDate date);

    // Get today's attendance for a staff
    AttendanceResponse getTodayAttendance(Long staffId);

    // Check if staff has attendance for today
    boolean hasAttendanceForToday(Long staffId);

    // NEW: Get monthly attendance for staff
    List<AttendanceResponse> getMonthlyAttendance(Long staffId, LocalDate month);

    // NEW: Get attendance summary for staff and month
    Map<String, Object> getAttendanceSummary(Long staffId, LocalDate month);

    // NEW: Bulk mark attendance for multiple staff
    List<AttendanceResponse> bulkMarkAttendance(List<AttendanceRequest> requests);
}
