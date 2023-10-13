package tr.com.meteor.crm.repository;

import org.springframework.stereotype.Repository;
import tr.com.meteor.crm.domain.ApprovalUserLimit;
import tr.com.meteor.crm.domain.User;

import java.util.List;
import java.util.UUID;

@Repository
public interface ApprovalUserLimitRepository extends GenericIdNameAuditingEntityRepository<ApprovalUserLimit, UUID> {
    ApprovalUserLimit findByUser(User user);
}

