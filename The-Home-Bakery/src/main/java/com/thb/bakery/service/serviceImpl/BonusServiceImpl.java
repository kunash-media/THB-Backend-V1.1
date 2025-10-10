package com.thb.bakery.service.serviceImpl;

import com.thb.bakery.dto.request.BonusRequest;
import com.thb.bakery.entity.BonusEntity;
import com.thb.bakery.entity.StaffEntity;
import com.thb.bakery.repository.BonusRepository;
import com.thb.bakery.repository.StaffRepository;
import com.thb.bakery.service.BonusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BonusServiceImpl implements BonusService {

    private static final Logger logger = LoggerFactory.getLogger(BonusServiceImpl.class);

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private BonusRepository bonusRepository;

    @Override
    public void addBonus(Long staffId, BonusRequest request) {
        logger.info("🟢 Starting bonus addition process. StaffId: {}, Month: {}, Amount: {}, Date: {}",
                staffId, request.getMonth(), request.getAmount(),request.getBonusDate());

        try {
            // Input validation
            if (request.getMonth() == null || request.getMonth().trim().isEmpty()) {
                logger.error("🔴 Bonus month is null or empty. StaffId: {}", staffId);
                throw new RuntimeException("Bonus month cannot be null or empty");
            }

            if (request.getAmount() == null || request.getAmount() <= 0) {
                logger.error("🔴 Invalid bonus amount: {}. StaffId: {}", request.getAmount(), staffId);
                throw new RuntimeException("Bonus amount must be greater than 0");
            }

            logger.debug("🔍 Looking up staff with ID: {}", staffId);
            StaffEntity staff = staffRepository.findById(staffId)
                    .orElseThrow(() -> {
                        logger.error("🔴 Staff not found with ID: {}", staffId);
                        return new RuntimeException("Staff not found with ID: " + staffId);
                    });

            logger.debug("✅ Staff found: {} (ID: {})", staff.getName(), staffId);

            // Create bonus entity
            logger.debug("🏗️ Creating bonus entity for staff: {}", staff.getName());
            BonusEntity bonus = new BonusEntity();
            bonus.setMonth(request.getMonth());
            bonus.setAmount(request.getAmount());
            bonus.setBonusDate(request.getBonusDate());
            bonus.setStaff(staff);
            // Add to staff's bonus list (optional - handled by cascade)
            staff.getBonuses().add(bonus);

            // Save bonus
            logger.debug("💾 Saving bonus to database...");
            BonusEntity savedBonus = bonusRepository.save(bonus);
            logger.debug("✅ Bonus saved successfully. Bonus ID: {}", savedBonus.getBonusId());

            // Log success
            logger.info("🎉 Bonus added successfully! Staff: {}, Bonus ID: {}, Amount: {}, Month: {}",
                    staff.getName(), savedBonus.getBonusId(), request.getAmount(), request.getMonth());

        } catch (RuntimeException e) {
            logger.error("❌ Failed to add bonus for staff ID: {}. Error: {}", staffId, e.getMessage(), e);
            throw e; // Re-throw to maintain existing error handling
        } catch (Exception e) {
            logger.error("💥 Unexpected error while adding bonus for staff ID: {}", staffId, e);
            throw new RuntimeException("Failed to add bonus due to unexpected error", e);
        }
    }
}
