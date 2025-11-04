package com.thb.bakery.service.serviceImpl;

import com.thb.bakery.dto.request.IncentiveRequest;
import com.thb.bakery.entity.IncentiveEntity;
import com.thb.bakery.entity.StaffEntity;
import com.thb.bakery.repository.IncentiveRepository;
import com.thb.bakery.repository.StaffRepository;
import com.thb.bakery.service.IncentiveService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IncentiveServiceImpl implements IncentiveService {

    private static final Logger logger = LoggerFactory.getLogger(IncentiveServiceImpl.class);

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private IncentiveRepository incentiveRepository;

    @Override
    public void addIncentive(Long staffId, IncentiveRequest request) {
        logger.info("🟢 Starting incentive addition process. StaffId: {}, Month: {}, Amount: {}, Date: {}",
                staffId, request.getMonth(), request.getAmount(), request.getIncentiveDate());

        try {
            // Input validation
            if (request.getMonth() == null || request.getMonth().trim().isEmpty()) {
                logger.error("🔴 Incentive month is null or empty. StaffId: {}", staffId);
                throw new RuntimeException("Incentive month cannot be null or empty");
            }

            if (request.getAmount() == null || request.getAmount() <= 0) {
                logger.error("🔴 Invalid incentive amount: {}. StaffId: {}", request.getAmount(), staffId);
                throw new RuntimeException("Incentive amount must be greater than 0");
            }

            if (request.getIncentiveDate() == null) {
                logger.error("🔴 Incentive date is null. StaffId: {}", staffId);
                throw new RuntimeException("Incentive date cannot be null");
            }

            logger.debug("🔍 Looking up staff with ID: {}", staffId);
            StaffEntity staff = staffRepository.findById(staffId)
                    .orElseThrow(() -> {
                        logger.error("🔴 Staff not found with ID: {}", staffId);
                        return new RuntimeException("Staff not found with ID: " + staffId);
                    });

            logger.debug("✅ Staff found: {} (ID: {})", staff.getName(), staffId);

            // Create incentive entity
            logger.debug("🏗️ Creating incentive entity for staff: {}", staff.getName());
            IncentiveEntity incentive = new IncentiveEntity();
            incentive.setMonth(request.getMonth());
            incentive.setAmount(request.getAmount());
            incentive.setIncentiveDate(request.getIncentiveDate());
            incentive.setNotes(request.getNotes());
            incentive.setStaff(staff);

            // Save incentive
            logger.debug("💾 Saving incentive to database...");
            IncentiveEntity savedIncentive = incentiveRepository.save(incentive);
            logger.debug("✅ Incentive saved successfully. Incentive ID: {}", savedIncentive.getIncentiveId());

            // Log success
            logger.info("🎉 Incentive added successfully! Staff: {}, Incentive ID: {}, Amount: {}, Month: {}",
                    staff.getName(), savedIncentive.getIncentiveId(), request.getAmount(), request.getMonth());

        } catch (RuntimeException e) {
            logger.error("❌ Failed to add incentive for staff ID: {}. Error: {}", staffId, e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            logger.error("💥 Unexpected error while adding incentive for staff ID: {}", staffId, e);
            throw new RuntimeException("Failed to add incentive due to unexpected error", e);
        }
    }
}
