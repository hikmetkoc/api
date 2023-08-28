package tr.com.meteor.crm.repository;

import org.springframework.stereotype.Repository;
import tr.com.meteor.crm.domain.Task;

import java.util.UUID;

@Repository
public interface TaskRepository extends GenericIdNameAuditingEntityRepository<Task, UUID> {
}
