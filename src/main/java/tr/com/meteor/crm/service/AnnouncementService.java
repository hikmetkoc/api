package tr.com.meteor.crm.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.meteor.crm.domain.Announcement;
import tr.com.meteor.crm.repository.AnnouncementRepository;

import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class AnnouncementService extends GenericIdNameAuditingEntityService<Announcement, UUID, AnnouncementRepository> {

    public AnnouncementService(BaseUserService baseUserService, BaseRoleService baseRoleService,
                               BasePermissionService basePermissionService, BaseFileDescriptorService baseFileDescriptorService,
                               BaseConfigurationService baseConfigurationService,
                               AnnouncementRepository repository) {
        super(baseUserService, baseRoleService, basePermissionService, baseFileDescriptorService, baseConfigurationService,
            Announcement.class, repository);
    }
}
