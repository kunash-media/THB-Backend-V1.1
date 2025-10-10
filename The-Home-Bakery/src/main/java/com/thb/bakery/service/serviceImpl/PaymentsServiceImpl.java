package com.thb.bakery.service.serviceImpl;

import com.thb.bakery.dto.response.StaffPaymentResponse;
import com.thb.bakery.entity.AdvanceEntity;
import com.thb.bakery.entity.BonusEntity;
import com.thb.bakery.entity.StaffEntity;
import com.thb.bakery.repository.StaffRepository;
import com.thb.bakery.service.PaymentsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PaymentsServiceImpl implements PaymentsService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentsServiceImpl.class);

    @Autowired
    private StaffRepository staffRepository;

    @Override
    public List<StaffPaymentResponse> getPayments(Long staffId) {
        logger.info("🟢 Starting payments retrieval process. StaffId: {}", staffId != null ? staffId : "ALL");

        try {
            List<StaffEntity> staffList;

            // Retrieve staff based on ID or all staff
            if (staffId == null) {
                logger.debug("🔍 Retrieving all staff members");
                staffList = staffRepository.findAll();
                logger.info("✅ Found {} staff members", staffList.size());
            } else {
                logger.debug("🔍 Retrieving specific staff with ID: {}", staffId);
                staffList = List.of(staffRepository.findById(staffId)
                        .orElseThrow(() -> {
                            logger.error("🔴 Staff not found with ID: {}", staffId);
                            return new RuntimeException("Staff not found with ID: " + staffId);
                        }));
                logger.info("✅ Found staff: {} (ID: {})", staffList.get(0).getName(), staffId);
            }

            List<StaffPaymentResponse> payments = new ArrayList<>();
            int totalPaymentRecords = 0;

            // Process each staff member
            for (StaffEntity staff : staffList) {
                logger.debug("📊 Processing payments for staff: {} (ID: {})", staff.getName(), staff.getStaffid());

                // Collect unique months from advances and bonuses
                Set<String> months = new HashSet<>();
                months.addAll(staff.getAdvances().stream()
                        .map(AdvanceEntity::getMonth)
                        .collect(Collectors.toSet()));
                months.addAll(staff.getBonuses().stream()
                        .map(BonusEntity::getMonth)
                        .collect(Collectors.toSet()));

                // Add current month if not present
                String currentMonth = YearMonth.now().toString();
                months.add(currentMonth);

                logger.debug("📅 Found {} unique months for staff {}: {}", months.size(), staff.getName(), months);

                // Sort months descending (recent first)
                List<String> sortedMonths = months.stream()
                        .sorted(Comparator.reverseOrder())
                        .collect(Collectors.toList());

                logger.debug("🔄 Processing {} months for staff {}", sortedMonths.size(), staff.getName());

                // Calculate payments for each month
                for (String month : sortedMonths) {
                    // Calculate advance total for the month
                    Double advanceTotal = staff.getAdvances().stream()
                            .filter(a -> a.getMonth().equals(month))
                            .mapToDouble(AdvanceEntity::getAmount)
                            .sum();

                    // Calculate bonus total for the month
                    Double bonusTotal = staff.getBonuses().stream()
                            .filter(b -> b.getMonth().equals(month))
                            .mapToDouble(BonusEntity::getAmount)
                            .sum();

                    // Calculate net salary
                    Double netSalary = staff.getSalary() + bonusTotal - advanceTotal;

                    // Determine status
                    String status = (advanceTotal > 0 || bonusTotal > 0 || month.equals(currentMonth)) ? "Pending" : "Paid";

                    logger.debug("💰 Month: {}, Base: {}, Advance: {}, Bonus: {}, Net: {}, Status: {}",
                            month, staff.getSalary(), advanceTotal, bonusTotal, netSalary, status);

                    // Create payment response
                    StaffPaymentResponse payment = new StaffPaymentResponse();
                    payment.setStaffId(staff.getStaffid());
                    payment.setStaffName(staff.getName());
                    payment.setMonth(month);
                    payment.setBaseSalary(staff.getSalary());
                    payment.setBonus(bonusTotal);
                    payment.setAdvance(advanceTotal);
                    payment.setNetSalary(netSalary);
                    payment.setStatus(status);

                    payments.add(payment);
                    totalPaymentRecords++;
                }

                logger.debug("✅ Completed processing for staff: {}. Records created: {}",
                        staff.getName(), sortedMonths.size());
            }

            logger.info("🎉 Successfully generated {} payment records for {} staff members",
                    totalPaymentRecords, staffList.size());

            return payments;

        } catch (RuntimeException e) {
            logger.error("❌ Failed to retrieve payments. StaffId: {}, Error: {}",
                    staffId != null ? staffId : "ALL", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            logger.error("💥 Unexpected error while retrieving payments. StaffId: {}",
                    staffId != null ? staffId : "ALL", e);
            throw new RuntimeException("Failed to retrieve payments due to unexpected error", e);
        }
    }
}
