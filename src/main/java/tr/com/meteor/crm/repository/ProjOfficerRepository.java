package tr.com.meteor.crm.repository;

import org.springframework.stereotype.Repository;
import tr.com.meteor.crm.domain.CustomTask;
import tr.com.meteor.crm.domain.ProjOfficer;

import java.util.UUID;

@Repository
public interface ProjOfficerRepository extends GenericIdNameAuditingEntityRepository<ProjOfficer, UUID> {
}
