package tr.com.meteor.crm.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.meteor.crm.domain.Report;
import tr.com.meteor.crm.repository.ReportRepository;

import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class ReportService extends GenericIdNameAuditingEntityService<Report, UUID, ReportRepository> {

    public ReportService(BaseUserService baseUserService, BaseRoleService baseRoleService,
                         BasePermissionService basePermissionService, BaseFileDescriptorService baseFileDescriptorService,
                         BaseConfigurationService baseConfigurationService,
                         ReportRepository repository) {
        super(baseUserService, baseRoleService, basePermissionService, baseFileDescriptorService, baseConfigurationService,
            Report.class, repository);
    }
}
