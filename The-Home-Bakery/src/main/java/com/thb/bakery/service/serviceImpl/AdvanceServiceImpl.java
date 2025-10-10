package com.thb.bakery.service.serviceImpl;

import com.thb.bakery.dto.request.AdvanceRequest;
import com.thb.bakery.entity.AdvanceEntity;
import com.thb.bakery.entity.StaffEntity;
import com.thb.bakery.repository.AdvanceRepository;
import com.thb.bakery.repository.StaffRepository;
import com.thb.bakery.service.AdvanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdvanceServiceImpl implements AdvanceService {

    private static final Logger logger = LoggerFactory.getLogger(AdvanceServiceImpl.class);

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private AdvanceRepository advanceRepository;

    @Override
    public void addAdvance(Long staffId, AdvanceRequest request) {
        logger.info("Processing add advance request. StaffId: {}, Month: {}, Amount: {}, Salary: {}, Date: {}",
                staffId, request.getMonth(), request.getAmount(), request.getSalary(), request.getAdvanceDate());

        try {
            // Validate request
            //added date validation
            if (request.getAdvanceDate() == null){
                throw new RuntimeException("Date cannot be null");
            }
            if (request.getMonth() == null || request.getMonth().trim().isEmpty()) {
                throw new RuntimeException("Month cannot be null or empty");
            }
            if (request.getAmount() == null || request.getAmount() <= 0) {
                throw new RuntimeException("Amount must be greater than 0");
            }
            if (request.getSalary() == null || request.getSalary() <= 0) {
                throw new RuntimeException("Salary must be greater than 0");
            }

            StaffEntity staff = staffRepository.findById(staffId)
                    .orElseThrow(() -> {
                        logger.error("Staff not found with ID: {}", staffId);
                        return new RuntimeException("Staff not found with ID: " + staffId);
                    });

            logger.debug("Staff found. StaffId: {}, Name: {}", staff.getStaffid(), staff.getName());

            AdvanceEntity advance = new AdvanceEntity();
            advance.setMonth(request.getMonth());
            advance.setAmount(request.getAmount());
            advance.setSalary(request.getSalary());
            advance.setAdvanceDate(request.getAdvanceDate());//for date
            advance.setStaff(staff);

            // Save the advance
            AdvanceEntity savedAdvance = advanceRepository.save(advance);

            logger.info("Successfully added advance for staff. StaffId: {}, AdvanceId: {}, Amount: {}",
                    staffId, savedAdvance.getAdvanceId(), request.getAmount(), request.getAdvanceDate()); // CHANGED: getAdvanceid()

        } catch (RuntimeException e) {
            logger.error("Error occurred while adding advance for staff. StaffId: {}, Error: {}",
                    staffId, e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error occurred while adding advance for staff. StaffId: {}",
                    staffId, e);
            throw new RuntimeException("Failed to add advance", e);
        }
    }
}