package tr.com.meteor.crm.repository;

import org.springframework.stereotype.Repository;
import tr.com.meteor.crm.domain.Target;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public interface TargetRepository extends GenericIdNameAuditingEntityRepository<Target, UUID> {
    List<Target> findAllByTermStartGreaterThanEqualAndTermStartLessThan(Instant start, Instant end);

    List<Target> findAllByTermStartGreaterThanEqualAndTermStartLessThanAndOwnerIdIn(Instant start, Instant end, List<Long> ids);
}
