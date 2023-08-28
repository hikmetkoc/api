package tr.com.meteor.crm.repository;

import org.springframework.stereotype.Repository;
import tr.com.meteor.crm.domain.CustomTask;

import java.util.UUID;

@Repository
public interface CustomTaskRepository extends GenericIdNameAuditingEntityRepository<CustomTask, UUID> {
}
