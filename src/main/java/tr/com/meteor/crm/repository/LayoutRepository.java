package tr.com.meteor.crm.repository;

import org.springframework.stereotype.Repository;
import tr.com.meteor.crm.domain.Layout;

import java.util.UUID;

@Repository
public interface LayoutRepository extends GenericIdNameAuditingEntityRepository<Layout, UUID> {
}

