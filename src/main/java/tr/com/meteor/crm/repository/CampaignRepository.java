package tr.com.meteor.crm.repository;

import org.springframework.stereotype.Repository;
import tr.com.meteor.crm.domain.Campaign;

import java.util.UUID;

@Repository
public interface CampaignRepository extends GenericIdNameAuditingEntityRepository<Campaign, UUID> {
}
