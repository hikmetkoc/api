package tr.com.meteor.crm.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.meteor.crm.domain.SurveyAnswer;
import tr.com.meteor.crm.repository.SurveyAnswerRepository;

import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class SurveyAnswerService extends GenericIdNameAuditingEntityService<SurveyAnswer, UUID, SurveyAnswerRepository> {

    public SurveyAnswerService(BaseUserService baseUserService, BaseRoleService baseRoleService,
                               BasePermissionService basePermissionService, BaseFileDescriptorService baseFileDescriptorService,
                               BaseConfigurationService baseConfigurationService,
                               SurveyAnswerRepository repository) {
        super(baseUserService, baseRoleService, basePermissionService, baseFileDescriptorService, baseConfigurationService,
            SurveyAnswer.class, repository);
    }
}
