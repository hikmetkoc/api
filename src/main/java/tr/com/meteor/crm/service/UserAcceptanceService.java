package tr.com.meteor.crm.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.meteor.crm.domain.Announcement;
import tr.com.meteor.crm.domain.UserAcceptance;
import tr.com.meteor.crm.repository.AnnouncementRepository;
import tr.com.meteor.crm.repository.UserAcceptanceRepository;

import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserAcceptanceService extends GenericIdNameAuditingEntityService<UserAcceptance, UUID, UserAcceptanceRepository> {

    public UserAcceptanceService(BaseUserService baseUserService, BaseRoleService baseRoleService,
                                 BasePermissionService basePermissionService, BaseFileDescriptorService baseFileDescriptorService,
                                 BaseConfigurationService baseConfigurationService,
                                 UserAcceptanceRepository repository) {
        super(baseUserService, baseRoleService, basePermissionService, baseFileDescriptorService, baseConfigurationService,
            UserAcceptance.class, repository);
    }
}
