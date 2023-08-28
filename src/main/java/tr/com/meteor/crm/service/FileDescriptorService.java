package tr.com.meteor.crm.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.meteor.crm.domain.FileDescriptor;
import tr.com.meteor.crm.repository.FileDescriptorRepository;

import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class FileDescriptorService extends GenericIdNameAuditingEntityService<FileDescriptor, UUID, FileDescriptorRepository> {

    public FileDescriptorService(BaseUserService baseUserService, BaseRoleService baseRoleService,
                                 BasePermissionService basePermissionService, BaseFileDescriptorService baseFileDescriptorService,
                                 BaseConfigurationService baseConfigurationService,
                                 FileDescriptorRepository repository) {
        super(baseUserService, baseRoleService, basePermissionService, baseFileDescriptorService, baseConfigurationService,
            FileDescriptor.class, repository);
    }
}
