package tr.com.meteor.crm.repository;

import org.springframework.stereotype.Repository;
import tr.com.meteor.crm.domain.ApprovalUserLimit;
import tr.com.meteor.crm.domain.CompanyMaterial;

import java.util.UUID;

@Repository
public interface CompanyMaterialRepository extends GenericIdNameAuditingEntityRepository<CompanyMaterial, UUID> {
}

