package tr.com.meteor.crm.repository;

import org.springframework.stereotype.Repository;
import tr.com.meteor.crm.domain.CustomActivity;

import java.util.List;
import java.util.UUID;

@Repository
public interface CustomActivityRepository extends GenericIdNameAuditingEntityRepository<CustomActivity, UUID> {
    List<CustomActivity> findAllByCustomtaskId(UUID customtaskid);
}

