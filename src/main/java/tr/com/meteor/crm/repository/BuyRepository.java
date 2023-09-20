package tr.com.meteor.crm.repository;

import org.springframework.stereotype.Repository;
import tr.com.meteor.crm.domain.Buy;

import java.util.List;
import java.util.UUID;

@Repository
public interface BuyRepository extends GenericIdNameAuditingEntityRepository<Buy, UUID> {
    List<Buy> findByStoreId(UUID storeid);
}
