package com.thb.bakery.controller;

import com.thb.bakery.dto.request.AdvanceRequest;
import com.thb.bakery.dto.request.BonusRequest;
import com.thb.bakery.dto.request.SalarySlipRequest;
import com.thb.bakery.dto.request.StaffCreateRequest;
import com.thb.bakery.dto.response.SalarySlipResponse;
import com.thb.bakery.dto.response.StaffResponse;

import com.thb.bakery.service.AdvanceService;
import com.thb.bakery.service.BonusService;
import com.thb.bakery.service.SalarySlipService;
import com.thb.bakery.service.StaffService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/staff")
public class StaffController {

    private static final Logger logger = LoggerFactory.getLogger(StaffController.class);

    @Autowired
    private StaffService staffService;

    @Autowired
    private AdvanceService advanceService;

    @Autowired
    private BonusService bonusService;

    @Autowired
    private SalarySlipService salarySlipService;

    //Create NEW Staff
    @PostMapping("/create-Staff")
    public ResponseEntity<StaffResponse> createStaff(@RequestBody StaffCreateRequest request){
        logger.info("Received request to create new staff. Name: {}, Role: {}",
                request.getName(), request.getRole());

        try {
            StaffResponse response = staffService.createStaff(request);
            logger.info("Successfully created staff with ID: {}", response.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            logger.error("Error occurred while creating staff. Request: {}", request, e);
            throw e;
        }
    }

    //Update staff by id
    @PutMapping("/update-Staff/{id}")
    public ResponseEntity<StaffResponse> updateStaff(@PathVariable Long id, @RequestBody StaffCreateRequest request){
        logger.info("Received request to update staff. StaffId: {}", id);

        try {
            StaffResponse response = staffService.updateStaff(id, request);
            logger.info("Successfully updated staff with ID: {}", id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error occurred while updating staff with ID: {}", id, e);
            throw e;
        }
    }

    //Delete Staff
    @DeleteMapping("/delete-Staff/{id}")
    public ResponseEntity<StaffResponse> deleteStaff(@PathVariable Long id){
        logger.info("Received request to delete staff. StaffId: {}", id);

        try {
            staffService.deleteStaff(id);
            logger.info("Successfully deleted staff with ID: {}", id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Error occurred while deleting staff with ID: {}", id, e);
            throw e;
        }
    }

    //Get ALL Staff with filters
    @GetMapping("/get-all-Staff")
    public ResponseEntity<List<StaffResponse>> getAllStaff(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String status){
        logger.info("Received request to get all staff. Filters - Search: {}, Role: {}, Status: {}",
                search, role, status);

        try {
            List<StaffResponse> response = staffService.getAllStaff(search, role, status);
            logger.info("Successfully retrieved {} staff record(s)", response.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error occurred while retrieving all staff with filters", e);
            throw e;
        }
    }

    //Get Staff By ID
    @GetMapping("/get-Staff-By-Id/{id}")
    public ResponseEntity<StaffResponse> getStaffById(@PathVariable Long id){
        logger.info("Received request to get staff by ID. StaffId: {}", id);

        try {
            StaffResponse response = staffService.getStaffById(id);
            logger.info("Successfully retrieved staff with ID: {}", id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error occurred while retrieving staff with ID: {}", id, e);
            throw e;
        }
    }

    //ADD Advance For staff
    @PostMapping("/add-advances/{id}")
    public ResponseEntity<Void> addAdvance(@PathVariable Long id, @RequestBody AdvanceRequest request){
        logger.info("Received request to add advance for staff. StaffId: {}, Amount: {}",
                id, request.getAmount());

        try {
            advanceService.addAdvance(id, request);
            logger.info("Successfully added advance for staff with ID: {}", id);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            logger.error("Error occurred while adding advance for staff with ID: {}", id, e);
            throw e;
        }
    }

    //ADD Bonus for Staff
    @PostMapping("/add-bonuses/{id}")
    public ResponseEntity<Void> addBonus(@PathVariable Long id, @RequestBody BonusRequest request){
        logger.info("Received request to add bonus for staff. StaffId: {}, Amount: {}",
                id, request.getAmount());

        try {
            bonusService.addBonus(id, request);
            logger.info("Successfully added bonus for staff with ID: {}", id);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            logger.error("Error occurred while adding bonus for staff with ID: {}", id, e);
            throw e;
        }
    }

    //Generate Salary SLip
    @PostMapping("/generate-slip/{id}")
    public ResponseEntity<SalarySlipResponse> generateSlip(@PathVariable Long id, @RequestBody SalarySlipRequest request){
        logger.info("Received request to generate salary slip. StaffId: {}, Month: {},",
                id, request.getMonth());

        try {
            SalarySlipResponse response = salarySlipService.generateSlip(id, request);
            logger.info("Successfully generated salary slip for staff with ID: {}", id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error occurred while generating salary slip for staff with ID: {}", id, e);
            throw e;
        }
    }
}