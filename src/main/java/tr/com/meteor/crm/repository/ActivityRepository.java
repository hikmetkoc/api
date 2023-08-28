package tr.com.meteor.crm.repository;

import org.springframework.stereotype.Repository;
import tr.com.meteor.crm.domain.Activity;

import java.util.List;
import java.util.UUID;

@Repository
public interface ActivityRepository extends GenericIdNameAuditingEntityRepository<Activity, UUID> {
    List<Activity> findAllByTaskId(UUID taskid);
}

