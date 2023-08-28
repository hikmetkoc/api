package tr.com.meteor.crm.repository;

import org.springframework.stereotype.Repository;
import tr.com.meteor.crm.domain.Lead;

import java.util.UUID;

@Repository
public interface LeadRepository extends GenericIdNameAuditingEntityRepository<Lead, UUID> {
}
