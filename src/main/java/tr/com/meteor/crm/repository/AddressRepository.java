package tr.com.meteor.crm.repository;

import org.springframework.stereotype.Repository;
import tr.com.meteor.crm.domain.Address;
import tr.com.meteor.crm.domain.Contact;

import java.util.List;
import java.util.UUID;

@Repository
public interface AddressRepository extends GenericIdNameAuditingEntityRepository<Address, UUID> {
    List<Address> findAllByCustomerId(UUID customerId);
}
