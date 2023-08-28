package tr.com.meteor.crm.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tr.com.meteor.crm.domain.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SpendRepository extends GenericIdNameAuditingEntityRepository<Spend, UUID> {

    List<Spend> findByPaymentorderId(UUID id);

    @Transactional
    @Modifying
    @Query("UPDATE Spend p SET p.paymentStatus = :status WHERE p.id = :id")
    void updatePaymentStatus(@Param("status") String status, @Param("id") UUID id);

    @Transactional
    @Modifying
    @Query("UPDATE Spend p SET p.payTl = :pay, p.exchangeMoney = :exchangeMoney WHERE p.id = :id")
    void updatePay(@Param("exchangeMoney") BigDecimal exchangeMoney, @Param("pay") BigDecimal pay, @Param("id") UUID id);
    @Transactional
    @Modifying
    @Query("UPDATE Spend p SET p.status = :status, p.spendDate = :okeyFirst, p.lock = :lock, p.paymentStatus = :spendPaymentStatus, p.owner = :islemyapan WHERE p.id = :id")
    void updateStatusById(@Param("status") AttributeValue status, @Param("id") UUID id, @Param("okeyFirst") Instant okeyFirst, @Param("lock") Boolean lock, @Param("spendPaymentStatus") String spendPaymentStatus, @Param("islemyapan") User islemyapan);

    @Transactional
    @Modifying
    @Query("UPDATE Spend p SET p.status = :status, p.spendDate = :okeyFirst, p.lock = :lock, p.paymentStatus = :spendPaymentStatus, p.owner = :islemyapan, p.description = :description WHERE p.id = :id")
    void updateCancelStatusById(@Param("status") AttributeValue status, @Param("id") UUID id, @Param("okeyFirst") Instant okeyFirst, @Param("lock") Boolean lock, @Param("spendPaymentStatus") String spendPaymentStatus, @Param("islemyapan") User islemyapan, @Param("description") String description);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO Spend (id, created_by_id, status_id, paymentorder_id, amount, payment_status, lock, finance_id, created_date, maturity_date, payment_num, pay_tl, owner_id, spend_date, customer_id) VALUES (:id, :owner, :status, :paymentorder, :amount, :paymentStatus, :lock, :finance, :createdDate, :maturityDate, :paymentNum, :payTl, :owner, :createdDate, :customer)", nativeQuery = true)
    void insertSpend(@Param("id") UUID id, @Param("owner") Long owner, @Param("status") String status, @Param("paymentorder") UUID paymentorder, @Param("amount") BigDecimal amount, @Param("paymentStatus") String paymentStatus, @Param("lock") Boolean lock, @Param("finance") Long finance, @Param("createdDate") Instant createdDate, @Param("maturityDate") Instant maturityDate, @Param("paymentNum") String paymentNum, @Param("payTl") BigDecimal payTl, @Param("customer") UUID customer);
}

