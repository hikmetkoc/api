package tr.com.meteor.crm.repository;

import org.springframework.stereotype.Repository;
import tr.com.meteor.crm.domain.Contact;

import java.util.List;
import java.util.UUID;

@Repository
public interface ContactRepository extends GenericIdNameAuditingEntityRepository<Contact, UUID> {
    List<Contact> findAllByCustomerId(UUID customerId);
}
