package tr.com.meteor.crm.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tr.com.meteor.crm.domain.AttributeValue;
import tr.com.meteor.crm.domain.InvoiceList;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface InvoiceListRepository extends GenericIdNameAuditingEntityRepository<InvoiceList, UUID> {
    boolean existsByInvoiceNum(String invoiceNum);
    List<InvoiceList> findByInvoiceNum (String invoiceNum);
    @Transactional
    @Modifying
    @Query("UPDATE InvoiceList p SET p.invoiceStatus = :status, p.description = :description, p.owner=null WHERE p.id = :id")
    void updateStatusById(@Param("status") AttributeValue status, @Param("id") UUID id, @Param("description") String description);

    @Transactional
    @Modifying
    @Query("UPDATE InvoiceList p SET p.invoiceStatus = :status, p.description = :description, p.cost = null, p.iban = null, p.maturityDate = null, p.dekont = null, p.exchange = null, p.success = null, p.paymentSubject = null, p.odemeYapanSirket = null, p.autopay = null, p.kismi = null, p.paymentType = null, p.successDate = null, p.paymentStyle = null, p.payTl = null WHERE p.id = :id")
    void updateInvoice(@Param("status") AttributeValue status, @Param("id") UUID id, @Param("description") String description);
}
