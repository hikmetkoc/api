package tr.com.meteor.crm.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.meteor.crm.domain.QuoteVersionLineItem;
import tr.com.meteor.crm.repository.QuoteVersionLineItemRepository;

import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class QuoteVersionLineItemService extends GenericIdEntityService<QuoteVersionLineItem, UUID, QuoteVersionLineItemRepository> {

    public QuoteVersionLineItemService(BaseUserService baseUserService, BaseRoleService baseRoleService,
                                       BasePermissionService basePermissionService, BaseFileDescriptorService baseFileDescriptorService,
                                       BaseConfigurationService baseConfigurationService,
                                       QuoteVersionLineItemRepository repository) {
        super(baseUserService, baseRoleService, basePermissionService, baseFileDescriptorService, baseConfigurationService,
            QuoteVersionLineItem.class, repository);
    }
}
