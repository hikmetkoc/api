package tr.com.meteor.crm.repository;

import org.springframework.stereotype.Repository;
import tr.com.meteor.crm.domain.FieldMetadata;

@Repository
public interface FieldMetadataRepository extends GenericIdNameAuditingEntityRepository<FieldMetadata, String> {

}
