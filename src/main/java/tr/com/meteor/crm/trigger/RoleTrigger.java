package tr.com.meteor.crm.trigger;

import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import tr.com.meteor.crm.domain.Role;
import tr.com.meteor.crm.repository.RoleRepository;
import tr.com.meteor.crm.service.BaseConfigurationService;
import tr.com.meteor.crm.service.BaseRoleService;
import tr.com.meteor.crm.service.BaseUserService;

import java.util.Objects;

@Component(RoleTrigger.QUALIFIER)
public class RoleTrigger extends Trigger<Role, String, RoleRepository> {
    final static String QUALIFIER = "RoleTrigger";

    public RoleTrigger(CacheManager cacheManager, RoleRepository taskRepository,
                       BaseUserService baseUserService, BaseConfigurationService baseConfigurationService) {
        super(cacheManager, Role.class, RoleTrigger.class, taskRepository, baseUserService, baseConfigurationService);
    }

    @Override
    public void onClearCache(Role entity) {
        Objects.requireNonNull(cacheManager.getCache(BaseRoleService.ROLE_ALL_CACHE)).clear();
        Objects.requireNonNull(cacheManager.getCache(BaseRoleService.ROLE_BY_ID_CACHE)).clear();
    }
}
