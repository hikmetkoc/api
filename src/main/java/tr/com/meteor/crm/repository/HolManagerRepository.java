package tr.com.meteor.crm.repository;

import org.springframework.stereotype.Repository;
import tr.com.meteor.crm.domain.BuyLimit;
import tr.com.meteor.crm.domain.HolManager;

import java.util.List;
import java.util.UUID;

@Repository
public interface HolManagerRepository extends GenericIdNameAuditingEntityRepository<HolManager, UUID> {
    List<HolManager> findByUserId(Long limitid);
}
