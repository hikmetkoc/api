package tr.com.meteor.crm.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tr.com.meteor.crm.domain.*;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentOrderRepository extends GenericIdNameAuditingEntityRepository<PaymentOrder, UUID> {
    Optional<PaymentOrder> findByInvoiceNum(String storeid);
    Optional<PaymentOrder> findById(UUID id);

    Long countByPaymentSubjectAndClosePdfAndOwner(AttributeValue paymentSubject, Boolean closePdf, User owner);

    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM PaymentOrder e WHERE e.invoiceNum = :invoiceNum AND e.customer = :customer AND e.status <> 'Payment_Status_Red'")
    boolean existsByInvoiceNumAndCustomerNotPaymentOrderRed(@Param("invoiceNum") String invoiceNum, @Param("customer") Customer customer);

    @Transactional
    @Modifying
    @Query("UPDATE PaymentOrder p SET p.status = :status, p.cancelDate = :cancelDate, p.cancelUser = :cancelUser, p.okeyMuh = :okeyMuh, p.okeyFirst = :okeyFirst, p.okeySecond = :okeySecond, p.payamount = :payAmount, p.nextamount = :nextAmount, p.description = :description, p.muhasebeGoruntusu = :muhasebeGoruntusu, p.onayMuh = :muhasebeOnaycisi WHERE p.id = :id")
    void updateStatusById(@Param("status") AttributeValue status, @Param("id") UUID id, @Param("cancelDate") Instant cancelDate, @Param("cancelUser") User cancelUser, @Param("okeyMuh") Instant okeyMuh, @Param("okeyFirst") Instant okeyFirst, @Param("okeySecond") Instant okeySecond, @Param("payAmount") BigDecimal payAmount, @Param("nextAmount") BigDecimal nextAmount, @Param("description") String description, @Param("muhasebeGoruntusu") Boolean muhasebeGoruntusu, @Param("muhasebeOnaycisi") User muhasebeOnaycisi);

    @Transactional
    @Modifying
    @Query("UPDATE PaymentOrder p SET p.status = :paymentStatus, p.payamount = :odenen, p.nextamount = :kalan WHERE p.id = :paymentID")
    void updateValuesById(@Param("paymentStatus") AttributeValue paymentStatus, @Param("paymentID") UUID paymentID, @Param("odenen") BigDecimal odenen, @Param("kalan") BigDecimal kalan);

    @Transactional
    @Modifying
    @Query("UPDATE PaymentOrder p SET p.payTl = :pay WHERE p.id = :id")
    void updatePay(@Param("pay") BigDecimal pay, @Param("id") UUID id);

    @Transactional
    @Modifying
    @Query("UPDATE PaymentOrder p SET p.store = :store WHERE p.id = :id")
    void updateStoreId(@Param("id") UUID id, @Param("store") Store store);
}
