package tr.com.meteor.crm.service;

import tr.com.meteor.crm.domain.Role;
import tr.com.meteor.crm.domain.User;
import tr.com.meteor.crm.security.SecurityUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class BaseService {

    final BaseUserService baseUserService;
    final BaseRoleService baseRoleService;
    final BasePermissionService basePermissionService;
    final BaseFileDescriptorService baseFileDescriptorService;
    final BaseConfigurationService baseConfigurationService;

    public BaseService(BaseUserService baseUserService, BaseRoleService baseRoleService,
                       BasePermissionService basePermissionService, BaseFileDescriptorService baseFileDescriptorService,
                       BaseConfigurationService baseConfigurationService) {
        this.baseUserService = baseUserService;
        this.baseRoleService = baseRoleService;
        this.basePermissionService = basePermissionService;
        this.baseFileDescriptorService = baseFileDescriptorService;
        this.baseConfigurationService = baseConfigurationService;
    }

    public User getCurrentUser() {
        return baseUserService.getUserFullFetched(SecurityUtils.getCurrentUserId().get()).get();
    }

    public Long getCurrentUserId() {
        return SecurityUtils.getCurrentUserId().get();
    }

    public boolean isUserHaveOperation(Long userId, String operationId) {
        List<String> userRoleIds = baseUserService.getUserFullFetched(userId).get()
            .getRoles().stream().map(Role::getId).collect(Collectors.toList());

        return baseRoleService.getRoles().stream()
            .filter(x -> userRoleIds.contains(x.getId()))
            .anyMatch(role -> role.getOperations().stream().anyMatch(x -> x.getId().equals(operationId)));
    }

    public boolean isUserHaveRole(Long userId, String roleId) {
        Set<Role> userRoles = baseUserService.getUserFullFetched(userId).get().getRoles();

        return userRoles.stream().anyMatch(role -> role.getId().equals(roleId));
    }

    public boolean isUserHaveRole(Long userId, String... roleIds) {
        for (String roleId : roleIds) {
            if(isUserHaveRole(userId, roleId)) {
                return true;
            }
        }

        return false;
    }
}
