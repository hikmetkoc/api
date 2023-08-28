package tr.com.meteor.crm.repository;

import org.springframework.stereotype.Repository;
import tr.com.meteor.crm.domain.Ikfile;
import tr.com.meteor.crm.domain.UserPermission;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserPermissionRepository extends GenericIdNameAuditingEntityRepository<UserPermission, UUID> {
    Boolean findByHolidayView(Long id);
}

