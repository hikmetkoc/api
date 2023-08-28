package tr.com.meteor.crm.repository;

import org.springframework.stereotype.Repository;
import tr.com.meteor.crm.domain.SummaryUserCustomer;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public interface SummaryUserCustomerRepository extends GenericIdEntityRepository<SummaryUserCustomer, UUID> {
    List<SummaryUserCustomer> findAllByDateGreaterThanEqualAndDateLessThan(Instant start, Instant end);

    List<SummaryUserCustomer> findAllByDate(Instant date);
}
