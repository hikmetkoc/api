package tr.com.meteor.crm.repository;

import org.springframework.stereotype.Repository;
import tr.com.meteor.crm.domain.MotionSums;

import java.util.UUID;

@Repository
public interface MotionSumsRepository extends GenericIdNameAuditingEntityRepository<MotionSums, UUID> {
}
