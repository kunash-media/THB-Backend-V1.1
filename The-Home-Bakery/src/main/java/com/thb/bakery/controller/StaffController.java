package com.thb.bakery.controller;

import com.thb.bakery.dto.request.AdvanceRequest;
import com.thb.bakery.dto.request.BonusRequest;
import com.thb.bakery.dto.request.IncentiveRequest;
import com.thb.bakery.dto.request.StaffCreateRequest;
import com.thb.bakery.dto.response.SalarySlipResponse;
import com.thb.bakery.dto.response.StaffResponse;
import com.thb.bakery.service.AdvanceService;
import com.thb.bakery.service.BonusService;
import com.thb.bakery.service.IncentiveService;
import com.thb.bakery.service.SalarySlipService;
import com.thb.bakery.service.StaffService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/staff")
//@CrossOrigin(origins = "*")
public class StaffController {

    private static final Logger logger = LoggerFactory.getLogger(StaffController.class);

    @Autowired
    private StaffService staffService;

    @Autowired
    private AdvanceService advanceService;

    @Autowired
    private BonusService bonusService;

    @Autowired
    private IncentiveService incentiveService; // NEW

    @Autowired
    private SalarySlipService salarySlipService;

    // Get all staff with optional filters
    @GetMapping
    public ResponseEntity<List<StaffResponse>> getAllStaff(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String status) {

        logger.info("🟢 GET /api/staff - Retrieving all staff with filters: search={}, role={}, status={}",
                search, role, status);

        try {
            List<StaffResponse> staffList = staffService.getAllStaff(search, role, status);
            logger.info("✅ Successfully retrieved {} staff members", staffList.size());
            return ResponseEntity.ok(staffList);

        } catch (Exception e) {
            logger.error("❌ Failed to retrieve staff list", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get staff by ID
    @GetMapping("/{staffId}")
    public ResponseEntity<StaffResponse> getStaffById(@PathVariable Long staffId) {
        logger.info("🟢 GET /api/staff/{} - Retrieving staff by ID", staffId);

        try {
            StaffResponse staff = staffService.getStaffById(staffId);
            logger.info("✅ Successfully retrieved staff: {} (ID: {})", staff.getName(), staffId);
            return ResponseEntity.ok(staff);

        } catch (RuntimeException e) {
            logger.error("❌ Staff not found with ID: {}", staffId);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("❌ Failed to retrieve staff with ID: {}", staffId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Create new staff
    @PostMapping
    public ResponseEntity<StaffResponse> createStaff(@RequestBody StaffCreateRequest request) {
        logger.info("🟢 POST /api/staff - Creating new staff: {}", request.getName());

        try {
            StaffResponse createdStaff = staffService.createStaff(request);
            logger.info("✅ Successfully created staff: {} (ID: {})", createdStaff.getName(), createdStaff.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdStaff);

        } catch (Exception e) {
            logger.error("❌ Failed to create staff: {}", request.getName(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Update staff
    @PutMapping("/{staffId}")
    public ResponseEntity<StaffResponse> updateStaff(@PathVariable Long staffId, @RequestBody StaffCreateRequest request) {
        logger.info("🟢 PUT /api/staff/{} - Updating staff", staffId);

        try {
            StaffResponse updatedStaff = staffService.updateStaff(staffId, request);
            logger.info("✅ Successfully updated staff: {} (ID: {})", updatedStaff.getName(), staffId);
            return ResponseEntity.ok(updatedStaff);

        } catch (RuntimeException e) {
            logger.error("❌ Staff not found for update: {}", staffId);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("❌ Failed to update staff with ID: {}", staffId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Delete staff
    @DeleteMapping("/{staffId}")
    public ResponseEntity<Void> deleteStaff(@PathVariable Long staffId) {
        logger.info("🟢 DELETE /api/staff/{} - Deleting staff", staffId);

        try {
            staffService.deleteStaff(staffId);
            logger.info("✅ Successfully deleted staff with ID: {}", staffId);
            return ResponseEntity.noContent().build();

        } catch (RuntimeException e) {
            logger.error("❌ Staff not found for deletion: {}", staffId);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("❌ Failed to delete staff with ID: {}", staffId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Delete staff with all dependencies
    @DeleteMapping("/{staffId}/with-dependencies")
    public ResponseEntity<Void> deleteStaffWithDependencies(@PathVariable Long staffId) {
        logger.info("🟢 DELETE /api/staff/{}/with-dependencies - Deleting staff with all dependencies", staffId);

        try {
            staffService.deleteStaffWithDependencies(staffId);
            logger.info("✅ Successfully deleted staff and all dependencies with ID: {}", staffId);
            return ResponseEntity.noContent().build();

        } catch (RuntimeException e) {
            logger.error("❌ Staff not found for deletion with dependencies: {}", staffId);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("❌ Failed to delete staff with dependencies with ID: {}", staffId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Add advance for staff
    @PostMapping("/{staffId}/advances")
    public ResponseEntity<Void> addAdvance(@PathVariable Long staffId, @RequestBody AdvanceRequest request) {
        logger.info("🟢 POST /api/staff/{}/advances - Adding advance for staff", staffId);

        try {
            advanceService.addAdvance(staffId, request);
            logger.info("✅ Successfully added advance for staff ID: {}", staffId);
            return ResponseEntity.ok().build();

        } catch (RuntimeException e) {
            logger.error("❌ Failed to add advance for staff ID: {}", staffId, e);
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            logger.error("❌ Failed to add advance for staff ID: {}", staffId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Add bonus for staff
    @PostMapping("/{staffId}/bonuses")
    public ResponseEntity<Void> addBonus(@PathVariable Long staffId, @RequestBody BonusRequest request) {
        logger.info("🟢 POST /api/staff/{}/bonuses - Adding bonus for staff", staffId);

        try {
            bonusService.addBonus(staffId, request);
            logger.info("✅ Successfully added bonus for staff ID: {}", staffId);
            return ResponseEntity.ok().build();

        } catch (RuntimeException e) {
            logger.error("❌ Failed to add bonus for staff ID: {}", staffId, e);
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            logger.error("❌ Failed to add bonus for staff ID: {}", staffId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Add incentive for staff
    @PostMapping("/{staffId}/incentives")
    public ResponseEntity<Void> addIncentive(@PathVariable Long staffId, @RequestBody IncentiveRequest request) {
        logger.info("🟢 POST /api/staff/{}/incentives - Adding incentive for staff", staffId);

        try {
            incentiveService.addIncentive(staffId, request);
            logger.info("✅ Successfully added incentive for staff ID: {}", staffId);
            return ResponseEntity.ok().build();

        } catch (RuntimeException e) {
            logger.error("❌ Failed to add incentive for staff ID: {}", staffId, e);
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            logger.error("❌ Failed to add incentive for staff ID: {}", staffId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Generate salary slip
    @GetMapping("/{staffId}/salary-slip")
    public ResponseEntity<SalarySlipResponse> generateSalarySlip(
            @PathVariable Long staffId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM") LocalDate month) {

        logger.info("🟢 GET /api/staff/{}/salary-slip - Generating salary slip for month: {}", staffId, month);

        try {
            SalarySlipResponse salarySlip = salarySlipService.generateSalarySlip(staffId, month);
            logger.info("✅ Successfully generated salary slip for staff ID: {}, month: {}", staffId, month);
            return ResponseEntity.ok(salarySlip);

        } catch (RuntimeException e) {
            logger.error("❌ Failed to generate salary slip for staff ID: {}", staffId, e);
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            logger.error("❌ Failed to generate salary slip for staff ID: {}", staffId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

