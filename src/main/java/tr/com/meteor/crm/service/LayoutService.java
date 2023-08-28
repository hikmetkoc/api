package tr.com.meteor.crm.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.meteor.crm.domain.Layout;
import tr.com.meteor.crm.repository.LayoutRepository;

import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class LayoutService extends GenericIdNameAuditingEntityService<Layout, UUID, LayoutRepository> {

    public LayoutService(BaseUserService baseUserService, BaseRoleService baseRoleService,
                         BasePermissionService basePermissionService, BaseFileDescriptorService baseFileDescriptorService,
                         BaseConfigurationService baseConfigurationService,
                         LayoutRepository repository) {
        super(baseUserService, baseRoleService, basePermissionService, baseFileDescriptorService, baseConfigurationService,
            Layout.class, repository);
    }
}
