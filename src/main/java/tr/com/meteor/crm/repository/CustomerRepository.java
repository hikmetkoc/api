package tr.com.meteor.crm.repository;

import org.springframework.stereotype.Repository;
import tr.com.meteor.crm.domain.Contract;
import tr.com.meteor.crm.domain.Customer;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Repository
public interface CustomerRepository extends GenericIdNameAuditingEntityRepository<Customer, UUID> {
    //List<Customer> findAllByFleetCodeIn(Collection<Integer> fleetCodes);
    List<Customer> findAllByIdIn(List<UUID> ids);
    List<Customer> findByTaxNumber(String tnum);

    boolean existsByTaxNumber(String taxNumber);
}
