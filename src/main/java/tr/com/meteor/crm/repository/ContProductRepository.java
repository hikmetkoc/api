package tr.com.meteor.crm.repository;

import org.springframework.stereotype.Repository;
import tr.com.meteor.crm.domain.ContProduct;
import tr.com.meteor.crm.domain.CustomActivity;

import java.util.List;
import java.util.UUID;

@Repository
public interface ContProductRepository extends GenericIdNameAuditingEntityRepository<ContProduct, UUID> {
    List<ContProduct> findByBuyId(UUID buyid);
}

