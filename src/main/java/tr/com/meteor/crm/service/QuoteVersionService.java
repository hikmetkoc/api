package tr.com.meteor.crm.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.meteor.crm.domain.QuoteVersion;
import tr.com.meteor.crm.repository.QuoteVersionRepository;

import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class QuoteVersionService extends GenericIdNameAuditingEntityService<QuoteVersion, UUID, QuoteVersionRepository> {

    public QuoteVersionService(BaseUserService baseUserService, BaseRoleService baseRoleService,
                               BasePermissionService basePermissionService, BaseFileDescriptorService baseFileDescriptorService,
                               BaseConfigurationService baseConfigurationService,
                               QuoteVersionRepository repository) {
        super(baseUserService, baseRoleService, basePermissionService, baseFileDescriptorService, baseConfigurationService,
            QuoteVersion.class, repository);
    }
}
