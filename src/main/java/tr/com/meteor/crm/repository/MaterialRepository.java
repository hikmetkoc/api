package tr.com.meteor.crm.repository;

import org.springframework.stereotype.Repository;
import tr.com.meteor.crm.domain.Material;

import java.util.UUID;

@Repository
public interface MaterialRepository extends GenericIdNameAuditingEntityRepository<Material, UUID> {
}

