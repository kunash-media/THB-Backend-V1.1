package com.thb.bakery.service.serviceImpl;

import com.thb.bakery.dto.request.IncentiveDto;
import com.thb.bakery.dto.request.StaffCreateRequest;
import com.thb.bakery.dto.response.AdvanceDto;
import com.thb.bakery.dto.response.BonusDto;
import com.thb.bakery.dto.response.StaffResponse;
import com.thb.bakery.entity.AdvanceEntity;
import com.thb.bakery.entity.BonusEntity;
import com.thb.bakery.entity.IncentiveEntity;
import com.thb.bakery.entity.StaffEntity;
import com.thb.bakery.repository.AdvanceRepository;
import com.thb.bakery.repository.AttendanceRepository;
import com.thb.bakery.repository.BonusRepository;
import com.thb.bakery.repository.IncentiveRepository;
import com.thb.bakery.repository.StaffRepository;
import com.thb.bakery.service.StaffService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StaffServiceImpl implements StaffService {

    private static final Logger logger = LoggerFactory.getLogger(StaffServiceImpl.class);

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private AdvanceRepository advanceRepository;

    @Autowired
    private BonusRepository bonusRepository;

    @Autowired
    private IncentiveRepository incentiveRepository; // NEW

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Override
    public boolean staffExists(Long staffId) {
        return staffRepository.existsById(staffId);
    }

    @Override
    public StaffResponse createStaff(StaffCreateRequest request) {
        logger.info("🟢 Starting staff creation process. Name: {}, Email: {}, Role: {}",
                request.getName(), request.getEmail(), request.getRole());

        try {
            // Validate request
            if (request.getName() == null || request.getName().trim().isEmpty()) {
                logger.error("🔴 Staff name cannot be null or empty");
                throw new RuntimeException("Staff name cannot be null or empty");
            }
            if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
                logger.error("🔴 Staff email cannot be null or empty");
                throw new RuntimeException("Staff email cannot be null or empty");
            }

            logger.debug("🏗️ Creating new staff entity");
            StaffEntity staff = new StaffEntity();
            staff.setName(request.getName());
            staff.setEmail(request.getEmail());
            staff.setPhone(request.getPhone());
            staff.setAccountNo(request.getAccountNo()); // NEW
            staff.setDateOfJoining(request.getDateOfJoining()); // NEW
            staff.setRole(request.getRole());
            staff.setStatus(request.getStatus());
            staff.setSalary(request.getSalary());
            staff.setLastActive(LocalDate.now());

            logger.debug("💾 Saving staff to database...");
            staff = staffRepository.save(staff);
            logger.debug("✅ Staff saved successfully. Generated ID: {}", staff.getStaffid());

            StaffResponse response = mapToResponse(staff);

            logger.info("🎉 Staff created successfully! ID: {}, Name: {}, Email: {}",
                    staff.getStaffid(), staff.getName(), staff.getEmail());

            return response;

        } catch (RuntimeException e) {
            logger.error("❌ Failed to create staff. Name: {}, Email: {}, Error: {}",
                    request.getName(), request.getEmail(), e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            logger.error("💥 Unexpected error while creating staff. Name: {}, Email: {}",
                    request.getName(), request.getEmail(), e);
            throw new RuntimeException("Failed to create staff due to unexpected error", e);
        }
    }

    @Override
    public StaffResponse updateStaff(Long id, StaffCreateRequest request) {
        logger.info("🟢 Starting staff update process. StaffId: {}, Name: {}", id, request.getName());

        try {
            logger.debug("🔍 Retrieving staff for update. StaffId: {}", id);
            StaffEntity staff = staffRepository.findById(id)
                    .orElseThrow(() -> {
                        logger.error("🔴 Staff not found for update. StaffId: {}", id);
                        return new RuntimeException("Staff not found with ID: " + id);
                    });

            logger.debug("✅ Staff found: {} (ID: {})", staff.getName(), id);
            logger.debug("🔄 Applying updates to staff...");

            // Track changes for logging
            boolean changesMade = false;

            if (request.getName() != null && !request.getName().equals(staff.getName())) {
                logger.debug("📝 Updating name: {} → {}", staff.getName(), request.getName());
                staff.setName(request.getName());
                changesMade = true;
            }
            if (request.getEmail() != null && !request.getEmail().equals(staff.getEmail())) {
                logger.debug("📝 Updating email: {} → {}", staff.getEmail(), request.getEmail());
                staff.setEmail(request.getEmail());
                changesMade = true;
            }
            if (request.getPhone() != null && !request.getPhone().equals(staff.getPhone())) {
                logger.debug("📝 Updating phone: {} → {}", staff.getPhone(), request.getPhone());
                staff.setPhone(request.getPhone());
                changesMade = true;
            }
            if (request.getAccountNo() != null && !request.getAccountNo().equals(staff.getAccountNo())) {
                logger.debug("📝 Updating account number: {} → {}", staff.getAccountNo(), request.getAccountNo());
                staff.setAccountNo(request.getAccountNo());
                changesMade = true;
            }
            if (request.getDateOfJoining() != null && !request.getDateOfJoining().equals(staff.getDateOfJoining())) {
                logger.debug("📝 Updating date of joining: {} → {}", staff.getDateOfJoining(), request.getDateOfJoining());
                staff.setDateOfJoining(request.getDateOfJoining());
                changesMade = true;
            }
            if (request.getRole() != null && !request.getRole().equals(staff.getRole())) {
                logger.debug("📝 Updating role: {} → {}", staff.getRole(), request.getRole());
                staff.setRole(request.getRole());
                changesMade = true;
            }
            if (request.getStatus() != null && !request.getStatus().equals(staff.getStatus())) {
                logger.debug("📝 Updating status: {} → {}", staff.getStatus(), request.getStatus());
                staff.setStatus(request.getStatus());
                changesMade = true;
            }
            if (request.getSalary() != null && !request.getSalary().equals(staff.getSalary())) {
                logger.debug("📝 Updating salary: {} → {}", staff.getSalary(), request.getSalary());
                staff.setSalary(request.getSalary());
                changesMade = true;
            }

            staff.setLastActive(LocalDate.now());
            logger.debug("📅 Updated last active to: {}", LocalDate.now());

            if (changesMade) {
                logger.debug("💾 Saving updated staff...");
                staff = staffRepository.save(staff);
                logger.debug("✅ Staff updated successfully");
            } else {
                logger.debug("ℹ️ No changes detected, skipping save");
            }

            StaffResponse response = mapToResponse(staff);

            logger.info("🎉 Staff updated successfully! ID: {}, Name: {}", id, staff.getName());

            return response;

        } catch (RuntimeException e) {
            logger.error("❌ Failed to update staff. StaffId: {}, Error: {}", id, e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            logger.error("💥 Unexpected error while updating staff. StaffId: {}", id, e);
            throw new RuntimeException("Failed to update staff due to unexpected error", e);
        }
    }

    @Override
    @Transactional
    public void deleteStaff(Long staffId) {
        logger.info("🟢 Starting staff deletion process. StaffId: {}", staffId);

        try {
            // Get the staff entity first
            StaffEntity staff = staffRepository.findById(staffId)
                    .orElseThrow(() -> {
                        logger.error("🔴 Staff not found for deletion. StaffId: {}", staffId);
                        return new RuntimeException("Staff not found with ID: " + staffId);
                    });

            logger.debug("🗑️ Found staff: {} (ID: {})", staff.getName(), staffId);

            // Manually clear relationships if needed (optional)
            logger.debug("🧹 Clearing staff relationships...");
            staff.getAdvances().clear();
            staff.getBonuses().clear();
            staff.getIncentives().clear(); // NEW
            staff.getAttendanceRecords().clear();

            // Save to update relationships
            staffRepository.save(staff);

            // Now delete the staff
            logger.debug("🗑️ Deleting staff entity...");
            staffRepository.delete(staff);

            logger.info("🎉 Staff deleted successfully! StaffId: {}, Name: {}", staffId, staff.getName());

        } catch (RuntimeException e) {
            logger.error("❌ Failed to delete staff. StaffId: {}, Error: {}", staffId, e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            logger.error("💥 Unexpected error while deleting staff. StaffId: {}", staffId, e);
            throw new RuntimeException("Failed to delete staff due to unexpected error", e);
        }
    }

    @Override
    @Transactional
    public void deleteStaffWithDependencies(Long staffId) {
        logger.info("🟢 Starting staff deletion WITH DEPENDENCIES. StaffId: {}", staffId);

        try {
            // Get the staff entity first
            StaffEntity staff = staffRepository.findById(staffId)
                    .orElseThrow(() -> {
                        logger.error("🔴 Staff not found for deletion. StaffId: {}", staffId);
                        return new RuntimeException("Staff not found with ID: " + staffId);
                    });

            logger.debug("🗑️ Found staff: {} (ID: {})", staff.getName(), staffId);

            // 1. Delete all advances for this staff
            logger.debug("🧹 Deleting advances...");
            advanceRepository.deleteByStaffId(staffId);
            logger.debug("✅ Deleted advances for staff ID: {}", staffId);

            // 2. Delete all bonuses for this staff
            logger.debug("🧹 Deleting bonuses...");
            bonusRepository.deleteByStaffId(staffId);
            logger.debug("✅ Deleted bonuses for staff ID: {}", staffId);

            // 3. Delete all incentives for this staff
            logger.debug("🧹 Deleting incentives...");
            incentiveRepository.deleteByStaffId(staffId);
            logger.debug("✅ Deleted incentives for staff ID: {}", staffId);

            // 4. Delete all attendance records for this staff
            logger.debug("🧹 Deleting attendance records...");
            attendanceRepository.deleteByStaffId(staffId);
            logger.debug("✅ Deleted attendance records for staff ID: {}", staffId);

            // 5. Now delete the staff
            logger.debug("🗑️ Deleting staff entity...");
            staffRepository.delete(staff);

            logger.info("🎉 Staff and all dependencies deleted successfully! StaffId: {}, Name: {}", staffId, staff.getName());

        } catch (RuntimeException e) {
            logger.error("❌ Failed to delete staff with dependencies. StaffId: {}, Error: {}", staffId, e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            logger.error("💥 Unexpected error while deleting staff with dependencies. StaffId: {}", staffId, e);
            throw new RuntimeException("Failed to delete staff with dependencies due to unexpected error", e);
        }
    }

    @Override
    public List<StaffResponse> getAllStaff(String search, String role, String status) {
        logger.info("🟢 Retrieving all staff with filters. Search: {}, Role: {}, Status: {}",
                search, role, status);

        try {
            logger.debug("🔍 Querying staff with filters...");
            List<StaffEntity> staffEntityList = staffRepository.findFiltered(search, role, status);
            logger.debug("✅ Found {} staff members", staffEntityList.size());

            List<StaffResponse> responses = staffEntityList.stream()
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());

            logger.info("🎉 Successfully retrieved {} staff members", responses.size());

            return responses;

        } catch (RuntimeException e) {
            logger.error("❌ Failed to retrieve staff list. Error: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            logger.error("💥 Unexpected error while retrieving staff list", e);
            throw new RuntimeException("Failed to retrieve staff list due to unexpected error", e);
        }
    }

    @Override
    public StaffResponse getStaffById(Long staffId) {
        logger.info("🟢 Retrieving staff by ID: {}", staffId);

        try {
            logger.debug("🔍 Looking up staff with ID: {}", staffId);
            StaffEntity staff = staffRepository.findById(staffId)
                    .orElseThrow(() -> {
                        logger.error("🔴 Staff not found with ID: {}", staffId);
                        return new RuntimeException("Staff not found with ID: " + staffId);
                    });

            logger.debug("✅ Staff found: {} (ID: {})", staff.getName(), staffId);
            StaffResponse response = mapToResponse(staff);

            logger.info("🎉 Successfully retrieved staff. ID: {}, Name: {}", staffId, staff.getName());

            return response;

        } catch (RuntimeException e) {
            logger.error("❌ Failed to retrieve staff. StaffId: {}, Error: {}", staffId, e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            logger.error("💥 Unexpected error while retrieving staff. StaffId: {}", staffId, e);
            throw new RuntimeException("Failed to retrieve staff due to unexpected error", e);
        }
    }

    private StaffResponse mapToResponse(StaffEntity staff) {
        logger.debug("📄 Mapping staff entity to response. StaffId: {}, Name: {}",
                staff.getStaffid(), staff.getName());

        StaffResponse response = new StaffResponse();
        response.setId(staff.getStaffid());
        response.setName(staff.getName());
        response.setEmail(staff.getEmail());
        response.setPhone(staff.getPhone());
        response.setAccountNo(staff.getAccountNo()); // NEW
        response.setDateOfJoining(staff.getDateOfJoining()); // NEW
        response.setRole(staff.getRole());
        response.setStatus(staff.getStatus());
        response.setSalary(staff.getSalary());
        response.setLastActive(staff.getLastActive());

        // Map ADVANCES List
        logger.debug("💰 Mapping advances for staff: {}", staff.getName());
        List<AdvanceDto> advanceDtos = new ArrayList<>();
        if (staff.getAdvances() != null) {
            for (AdvanceEntity advance : staff.getAdvances()) {
                AdvanceDto dto = new AdvanceDto();
                dto.setMonth(advance.getMonth());
                dto.setAmount(advance.getAmount());
                dto.setAdvanceDate(advance.getAdvanceDate());
                advanceDtos.add(dto);
            }
            logger.debug("📊 Mapped {} advances", advanceDtos.size());
        } else {
            logger.debug("ℹ️ No advances found for staff");
        }
        response.setAdvances(advanceDtos);

        // Map BONUSES List
        logger.debug("💰 Mapping bonuses for staff: {}", staff.getName());
        List<BonusDto> bonusDtos = new ArrayList<>();
        if (staff.getBonuses() != null) {
            for (BonusEntity bonus : staff.getBonuses()) {
                BonusDto dto = new BonusDto();
                dto.setMonth(bonus.getMonth());
                dto.setAmount(bonus.getAmount());
                dto.setBonusDate(bonus.getBonusDate());
                bonusDtos.add(dto);
            }
            logger.debug("📊 Mapped {} bonuses", bonusDtos.size());
        } else {
            logger.debug("ℹ️ No bonuses found for staff");
        }
        response.setBonuses(bonusDtos);

        // Map INCENTIVES List
        logger.debug("💰 Mapping incentives for staff: {}", staff.getName());
        List<IncentiveDto> incentiveDtos = new ArrayList<>();
        if (staff.getIncentives() != null) {
            for (IncentiveEntity incentive : staff.getIncentives()) {
                IncentiveDto dto = new IncentiveDto();
                dto.setMonth(incentive.getMonth());
                dto.setAmount(incentive.getAmount());
                dto.setIncentiveDate(incentive.getIncentiveDate());
                dto.setNotes(incentive.getNotes());
                incentiveDtos.add(dto);
            }
            logger.debug("📊 Mapped {} incentives", incentiveDtos.size());
        } else {
            logger.debug("ℹ️ No incentives found for staff");
        }
        response.setIncentives(incentiveDtos);

        logger.debug("✅ Successfully mapped staff to response. StaffId: {}", staff.getStaffid());

        return response;
    }
}