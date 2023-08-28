package tr.com.meteor.crm.repository;

import org.springframework.stereotype.Repository;
import tr.com.meteor.crm.domain.SummaryOpetSale;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public interface SummaryOpetSaleRepository extends GenericIdEntityRepository<SummaryOpetSale, UUID> {
    List<SummaryOpetSale> findAllBySaleEndGreaterThanEqualAndSaleEndLessThan(Instant start, Instant end);

    List<SummaryOpetSale> findAllByCustomerNotNullAndSaleEndGreaterThanEqualAndSaleEndLessThan(Instant start, Instant end);

    List<SummaryOpetSale> findAllByCustomerIsNullAndFleetCodeIsNotNull();

    List<SummaryOpetSale> findAllByFleetCodeEquals(Integer fleetCode);
}
