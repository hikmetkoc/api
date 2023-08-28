package tr.com.meteor.crm.repository;

import org.springframework.stereotype.Repository;
import tr.com.meteor.crm.domain.Buy;
import tr.com.meteor.crm.domain.Store;

import java.util.List;
import java.util.UUID;

@Repository
public interface StoreRepository extends GenericIdNameAuditingEntityRepository<Store, UUID> {
}
