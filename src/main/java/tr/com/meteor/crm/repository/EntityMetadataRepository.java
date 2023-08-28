package tr.com.meteor.crm.repository;

import org.springframework.stereotype.Repository;
import tr.com.meteor.crm.domain.EntityMetadata;

@Repository
public interface EntityMetadataRepository extends GenericIdNameAuditingEntityRepository<EntityMetadata, String> {

}
