package tr.com.meteor.crm.repository;

import org.springframework.stereotype.Repository;
import tr.com.meteor.crm.domain.Operation;

@Repository
public interface OperationRepository extends GenericIdNameAuditingEntityRepository<Operation, String> {
}
