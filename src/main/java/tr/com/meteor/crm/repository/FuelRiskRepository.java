package tr.com.meteor.crm.repository;

import org.springframework.stereotype.Repository;
import tr.com.meteor.crm.domain.ApprovalUserLimit;
import tr.com.meteor.crm.domain.FuelRisk;
import tr.com.meteor.crm.domain.User;

import java.util.UUID;

@Repository
public interface FuelRiskRepository extends GenericIdNameAuditingEntityRepository<FuelRisk, UUID> {
}

