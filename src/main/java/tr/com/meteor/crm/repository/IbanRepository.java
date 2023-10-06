package tr.com.meteor.crm.repository;

import org.springframework.stereotype.Repository;
import tr.com.meteor.crm.domain.Address;
import tr.com.meteor.crm.domain.Iban;

import java.util.List;
import java.util.UUID;

@Repository
public interface IbanRepository extends GenericIdNameAuditingEntityRepository<Iban, UUID> {

    boolean existsByName(String name);
    List<Iban> findAllByCustomerId(UUID customerId);
}
