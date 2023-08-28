package tr.com.meteor.crm.repository;

import org.springframework.stereotype.Repository;
import tr.com.meteor.crm.domain.VocationDay;
import java.util.UUID;

@Repository
public interface VocationDayRepository extends GenericIdNameAuditingEntityRepository<VocationDay, UUID> {
}
