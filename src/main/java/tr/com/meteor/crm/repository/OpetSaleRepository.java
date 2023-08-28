package tr.com.meteor.crm.repository;

import org.springframework.stereotype.Repository;
import tr.com.meteor.crm.domain.OpetSale;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public interface OpetSaleRepository extends GenericIdEntityRepository<OpetSale, UUID> {
    List<OpetSale> findAllBySaleEndGreaterThanEqualAndSaleEndLessThan(Instant start, Instant end);

    List<OpetSale> findAllByCustomerIsNullAndFleetCodeIsNotNull();

    List<OpetSale> findAllByFleetCodeEquals(Integer fleetCode);
}
