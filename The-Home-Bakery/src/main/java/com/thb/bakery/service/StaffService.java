package com.thb.bakery.service;

import com.thb.bakery.dto.request.StaffCreateRequest;
import com.thb.bakery.dto.response.StaffResponse;

import java.util.List;

public interface StaffService {
    boolean staffExists(Long staffId);
    StaffResponse createStaff(StaffCreateRequest request);
    StaffResponse updateStaff(Long id, StaffCreateRequest request);
    void deleteStaff(Long staffId);
    void deleteStaffWithDependencies(Long staffId);
    List<StaffResponse> getAllStaff(String search, String role, String status);
    StaffResponse getStaffById(Long staffId);
}