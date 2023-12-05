package tr.com.meteor.crm.service;

import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.meteor.crm.domain.Behavior;
import tr.com.meteor.crm.domain.MotionSums;
import tr.com.meteor.crm.domain.RiskAnalysis;
import tr.com.meteor.crm.domain.User;
import tr.com.meteor.crm.repository.BehaviorRepository;
import tr.com.meteor.crm.repository.ContractRepository;
import tr.com.meteor.crm.repository.MotionSumsRepository;
import tr.com.meteor.crm.repository.RiskAnalysisRepository;
import tr.com.meteor.crm.utils.filter.Filter;
import tr.com.meteor.crm.utils.filter.FilterItem;
import tr.com.meteor.crm.utils.request.Request;

import java.io.ByteArrayOutputStream;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class RiskAnalysisService extends GenericIdNameAuditingEntityService<RiskAnalysis, UUID, RiskAnalysisRepository> {
    private final MailService mailService;

    public RiskAnalysisService(BaseUserService baseUserService, BaseRoleService baseRoleService,
                               BasePermissionService basePermissionService, BaseFileDescriptorService baseFileDescriptorService,
                               BaseConfigurationService baseConfigurationService, RiskAnalysisRepository repository,
                               MailService mailService) {
        super(baseUserService, baseRoleService, basePermissionService, baseFileDescriptorService, baseConfigurationService,
            RiskAnalysis.class, repository);
        this.mailService = mailService;
    }
}
