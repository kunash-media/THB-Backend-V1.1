package com.thb.bakery.service.serviceImpl;

import com.thb.bakery.dto.request.SalarySlipRequest;
import com.thb.bakery.dto.response.SalarySlipResponse;
import com.thb.bakery.entity.AdvanceEntity;
import com.thb.bakery.entity.BonusEntity;
import com.thb.bakery.entity.StaffEntity;
import com.thb.bakery.repository.StaffRepository;
import com.thb.bakery.service.SalarySlipService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class SalarySlipServiceImpl implements SalarySlipService {

    private static final Logger logger = LoggerFactory.getLogger(SalarySlipServiceImpl.class);

    @Autowired
    private StaffRepository staffRepository;

    @Override
    public SalarySlipResponse generateSlip(Long staffId, SalarySlipRequest request) {
        logger.info("🟢 Starting salary slip generation. StaffId: {}, Month: {}", staffId, request.getMonth());

        try {
            // Validate request
            if (request.getMonth() == null || request.getMonth().trim().isEmpty()) {
                logger.error("🔴 Month cannot be null or empty. StaffId: {}", staffId);
                throw new RuntimeException("Month cannot be null or empty");
            }

            logger.debug("🔍 Retrieving staff details for ID: {}", staffId);
            StaffEntity staff = staffRepository.findById(staffId)
                    .orElseThrow(() -> {
                        logger.error("🔴 Staff not found with ID: {}", staffId);
                        return new RuntimeException("Staff not found with ID: " + staffId);
                    });

            logger.debug("✅ Staff found: {} (ID: {})", staff.getName(), staffId);
            String month = request.getMonth();

            // Calculate advance total for the month
            logger.debug("💰 Calculating advance total for month: {}", month);
            Double advanceTotal = staff.getAdvances().stream()
                    .filter(a -> a.getMonth().equals(month))
                    .mapToDouble(AdvanceEntity::getAmount)
                    .sum();
            logger.debug("📊 Advance total for {}: {}", month, advanceTotal);

            // Calculate bonus total for the month
            logger.debug("💰 Calculating bonus total for month: {}", month);
            Double bonusTotal = staff.getBonuses().stream()
                    .filter(b -> b.getMonth().equals(month))
                    .mapToDouble(BonusEntity::getAmount)
                    .sum();
            logger.debug("📊 Bonus total for {}: {}", month, bonusTotal);

            // Calculate net salary
            logger.debug("🧮 Calculating net salary. Base: {}, Bonus: {}, Advance: {}",
                    staff.getSalary(), bonusTotal, advanceTotal);
            Double netSalary = staff.getSalary() + bonusTotal - advanceTotal;
            logger.debug("✅ Net salary calculated: {}", netSalary);

            // Build response
            logger.debug("🏗️ Building salary slip response");
            SalarySlipResponse response = new SalarySlipResponse();
            response.setStaffName(staff.getName());

            String capitalizedRole = capitalize(staff.getRole());
            response.setRole(capitalizedRole);

            response.setEmail(staff.getEmail());
            response.setMonth(month);

            String dateOfIssue = LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy"));
            response.setDateOfIssue(dateOfIssue);

            response.setEmployeeId(staff.getStaffid());
            response.setBaseSalary(staff.getSalary());
            response.setBonus(bonusTotal);
            response.setAdvance(advanceTotal);
            response.setNetSalary(netSalary);

            logger.info("🎉 Salary slip generated successfully! Staff: {}, Month: {}, Net Salary: {}",
                    staff.getName(), month, netSalary);
            logger.debug("📄 Salary slip details - Base: {}, Bonus: {}, Advance: {}, Role: {}",
                    staff.getSalary(), bonusTotal, advanceTotal, capitalizedRole);

            return response;

        } catch (RuntimeException e) {
            logger.error("❌ Failed to generate salary slip. StaffId: {}, Month: {}, Error: {}",
                    staffId, request.getMonth(), e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            logger.error("💥 Unexpected error while generating salary slip. StaffId: {}, Month: {}",
                    staffId, request.getMonth(), e);
            throw new RuntimeException("Failed to generate salary slip due to unexpected error", e);
        }
    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            logger.warn("⚠️ Attempted to capitalize null or empty string");
            return str;
        }
        String capitalized = str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
        logger.debug("🔠 Capitalized '{}' to '{}'", str, capitalized);
        return capitalized;
    }
}
