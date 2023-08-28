package tr.com.meteor.crm.repository;

import org.springframework.stereotype.Repository;
import tr.com.meteor.crm.domain.Announcement;

import java.util.UUID;

@Repository
public interface AnnouncementRepository extends GenericIdNameAuditingEntityRepository<Announcement, UUID> {
}
