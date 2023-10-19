package tr.com.meteor.crm.repository;

import org.springframework.stereotype.Repository;
import tr.com.meteor.crm.domain.Behavior;

import java.util.List;
import java.util.UUID;

@Repository
public interface BehaviorRepository extends GenericIdNameAuditingEntityRepository<Behavior, UUID> {
    List<Behavior> findAllByMotionsumsId(UUID motionsumsid);
}

