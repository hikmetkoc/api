package tr.com.meteor.crm.repository;

import org.springframework.stereotype.Repository;
import tr.com.meteor.crm.domain.Permission;
import tr.com.meteor.crm.domain.Role;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface PermissionRepository extends GenericIdNameAuditingEntityRepository<Permission, UUID> {
    List<Permission> findAllByRoleInAndObjectNameEquals(Set<Role> roles, String objectName);

    List<Permission> findAllByRoleIn(Set<Role> roles);
}
