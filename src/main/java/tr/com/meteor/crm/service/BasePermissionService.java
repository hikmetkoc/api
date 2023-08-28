package tr.com.meteor.crm.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.meteor.crm.domain.Permission;
import tr.com.meteor.crm.domain.Role;
import tr.com.meteor.crm.repository.PermissionRepository;

import java.util.List;
import java.util.Set;

@Service
@Transactional(rollbackFor = Exception.class)
public class BasePermissionService {

    private final PermissionRepository repository;

    public BasePermissionService(PermissionRepository permissionRepository) {
        this.repository = permissionRepository;
    }

    public List<Permission> getPermissionsByRoleAndObject(Set<Role> roles, String objectName) {
        return repository.findAllByRoleInAndObjectNameEquals(roles, objectName);
    }

    public List<Permission> getPermissionsByRoles(Set<Role> roles) {
        return repository.findAllByRoleIn(roles);
    }
}
