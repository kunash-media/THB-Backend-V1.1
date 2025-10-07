package com.thb.bakery.repository;

import com.thb.bakery.Enum.PaymentStatus;
import com.thb.bakery.entity.PaymentOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentOrderRepository extends JpaRepository<PaymentOrder, Long> {

    Optional<PaymentOrder> findByRazorpayOrderId(String razorpayOrderId);

    List<PaymentOrder> findByStatus(PaymentStatus status);

    List<PaymentOrder> findByUserUserIdOrderByCreatedAtDesc(Long userId);

    // ADD THIS METHOD
    Optional<PaymentOrder> findByRazorpayOrderIdAndUserUserId(String razorpayOrderId, Long userId);

    @Query("SELECT p FROM PaymentOrder p WHERE p.user.userId = :userId AND p.status = :status")
    List<PaymentOrder> findByUserIdAndStatus(@Param("userId") Long userId,
                                             @Param("status") PaymentStatus status);

    Optional<PaymentOrder> findByRazorpayPaymentId(String razorpayPaymentId);

    @Query("SELECT COUNT(p) FROM PaymentOrder p WHERE p.status = :status")
    Long countByStatus(@Param("status") PaymentStatus status);

    List<PaymentOrder> findByUserUserIdAndStatus(Long userId, PaymentStatus paymentStatus);
}
