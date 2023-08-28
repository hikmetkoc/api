package tr.com.meteor.crm.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.meteor.crm.domain.SurveyQuestion;
import tr.com.meteor.crm.repository.SurveyQuestionRepository;

import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class SurveyQuestionService extends GenericIdNameAuditingEntityService<SurveyQuestion, UUID, SurveyQuestionRepository> {

    public SurveyQuestionService(BaseUserService baseUserService, BaseRoleService baseRoleService,
                                 BasePermissionService basePermissionService, BaseFileDescriptorService baseFileDescriptorService,
                                 BaseConfigurationService baseConfigurationService,
                                 SurveyQuestionRepository repository) {
        super(baseUserService, baseRoleService, basePermissionService, baseFileDescriptorService, baseConfigurationService,
            SurveyQuestion.class, repository);
    }
}
