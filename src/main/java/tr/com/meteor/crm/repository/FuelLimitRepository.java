package tr.com.meteor.crm.repository;

import org.springframework.stereotype.Repository;
import tr.com.meteor.crm.domain.FuelLimit;

import java.util.List;
import java.util.UUID;

@Repository
public interface FuelLimitRepository extends GenericIdNameAuditingEntityRepository<FuelLimit, UUID> {
    //List<Buy> findByStoreId(UUID storeid);
}
