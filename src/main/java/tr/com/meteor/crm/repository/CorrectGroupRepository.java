package tr.com.meteor.crm.repository;

import org.springframework.stereotype.Repository;
import tr.com.meteor.crm.domain.ApprovalUserLimit;
import tr.com.meteor.crm.domain.AttributeValue;
import tr.com.meteor.crm.domain.CorrectGroup;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CorrectGroupRepository extends GenericIdNameAuditingEntityRepository<CorrectGroup, UUID> {
    Optional<CorrectGroup> findByApprovalGroupId(String value);
}

