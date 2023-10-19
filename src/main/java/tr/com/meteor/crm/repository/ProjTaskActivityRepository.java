package tr.com.meteor.crm.repository;

import org.springframework.stereotype.Repository;
import tr.com.meteor.crm.domain.CustomTask;
import tr.com.meteor.crm.domain.ProjTaskActivity;

import java.util.UUID;

@Repository
public interface ProjTaskActivityRepository extends GenericIdNameAuditingEntityRepository<ProjTaskActivity, UUID> {
}
