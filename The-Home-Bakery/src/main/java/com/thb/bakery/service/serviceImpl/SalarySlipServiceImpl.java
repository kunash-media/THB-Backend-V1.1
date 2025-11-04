package com.thb.bakery.service.serviceImpl;

import com.thb.bakery.dto.request.SalarySlipRequest;
import com.thb.bakery.dto.response.SalarySlipResponse;
import com.thb.bakery.dto.response.SalarySlipResponseDTO;
import com.thb.bakery.entity.AdvanceEntity;
import com.thb.bakery.entity.BonusEntity;
import com.thb.bakery.entity.IncentiveEntity;
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
    public SalarySlipResponse generateSalarySlip(Long staffId, LocalDate month) {
        logger.info("🟢 Starting salary slip generation. StaffId: {}, Month: {}", staffId, month);

        try {
            // Get staff
            StaffEntity staff = staffRepository.findById(staffId)
                    .orElseThrow(() -> new RuntimeException("Staff not found with ID: " + staffId));

            // Calculate total advances for the month
            double totalAdvance = staff.getAdvances().stream()
                    .filter(advance -> advance.getMonth().equals(month.format(DateTimeFormatter.ofPattern("yyyy-MM"))))
                    .mapToDouble(AdvanceEntity::getAmount)
                    .sum();

            // Calculate total bonus for the month
            double totalBonus = staff.getBonuses().stream()
                    .filter(bonus -> bonus.getMonth().equals(month.format(DateTimeFormatter.ofPattern("yyyy-MM"))))
                    .mapToDouble(BonusEntity::getAmount)
                    .sum();

            // Calculate total incentive for the month
            double totalIncentive = staff.getIncentives().stream()
                    .filter(incentive -> incentive.getMonth().equals(month.format(DateTimeFormatter.ofPattern("yyyy-MM"))))
                    .mapToDouble(IncentiveEntity::getAmount)
                    .sum();

            // Calculate net salary
            double netSalary = staff.getSalary() + totalBonus + totalIncentive - totalAdvance;

            // Create response
            SalarySlipResponse response = new SalarySlipResponse();
            response.setStaffName(staff.getName());
            response.setRole(staff.getRole());
            response.setEmail(staff.getEmail());
            response.setMonth(month.format(DateTimeFormatter.ofPattern("yyyy-MM")));
            response.setDateOfIssue(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            response.setEmployeeId(staff.getStaffid());
            response.setBaseSalary(staff.getSalary());
            response.setBonus(totalBonus);
            response.setAdvance(totalAdvance);
            response.setIncentive(totalIncentive); // NEW
            response.setNetSalary(netSalary);

            logger.info("✅ Salary slip generated successfully for staff: {}", staff.getName());

            return response;

        } catch (Exception e) {
            logger.error("❌ Failed to generate salary slip for staff ID: {}", staffId, e);
            throw new RuntimeException("Failed to generate salary slip", e);
        }
    }

    @Override
    public SalarySlipResponse generateSlip(Long staffId, SalarySlipRequest request) {
        return null;
    }

    @Override
    public SalarySlipResponseDTO generateSalarySlip(Long staffId, String month) {
        return null;
    }


}