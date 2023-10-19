package tr.com.meteor.crm.repository;

import org.springframework.stereotype.Repository;
import tr.com.meteor.crm.domain.Announcement;
import tr.com.meteor.crm.domain.UserAcceptance;

import java.util.UUID;

@Repository
public interface UserAcceptanceRepository extends GenericIdNameAuditingEntityRepository<UserAcceptance, UUID> {
}
