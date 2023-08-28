package tr.com.meteor.crm.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.meteor.crm.domain.Campaign;
import tr.com.meteor.crm.repository.CampaignRepository;

import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class CampaignService extends GenericIdNameAuditingEntityService<Campaign, UUID, CampaignRepository> {

    public CampaignService(BaseUserService baseUserService, BaseRoleService baseRoleService,
                           BasePermissionService basePermissionService, BaseFileDescriptorService baseFileDescriptorService,
                           BaseConfigurationService baseConfigurationService,
                           CampaignRepository repository) {
        super(baseUserService, baseRoleService, basePermissionService, baseFileDescriptorService, baseConfigurationService,
            Campaign.class, repository);
    }
}
