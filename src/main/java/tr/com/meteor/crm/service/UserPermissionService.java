package tr.com.meteor.crm.service;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.meteor.crm.domain.Ikfile;
import tr.com.meteor.crm.domain.Role;
import tr.com.meteor.crm.domain.User;
import tr.com.meteor.crm.domain.UserPermission;
import tr.com.meteor.crm.repository.ContractRepository;
import tr.com.meteor.crm.repository.IkfileRepository;
import tr.com.meteor.crm.repository.QuoteRepository;
import tr.com.meteor.crm.repository.UserPermissionRepository;
import tr.com.meteor.crm.service.dto.CheckInOutDTO;
import tr.com.meteor.crm.service.dto.QuoteSendDocumentDTO;
import tr.com.meteor.crm.utils.Documents;
import tr.com.meteor.crm.utils.filter.Filter;
import tr.com.meteor.crm.utils.filter.FilterItem;
import tr.com.meteor.crm.utils.jasper.PdfTemplates;
import tr.com.meteor.crm.utils.operations.Operations;
import tr.com.meteor.crm.utils.request.Request;

import java.io.ByteArrayOutputStream;
import java.time.Instant;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserPermissionService extends GenericIdNameAuditingEntityService<UserPermission, UUID, UserPermissionRepository> {
    private final MailService mailService;

    public UserPermissionService(BaseUserService baseUserService, BaseRoleService baseRoleService,
                                 BasePermissionService basePermissionService, BaseFileDescriptorService baseFileDescriptorService,
                                 BaseConfigurationService baseConfigurationService, UserPermissionRepository repository,
                                 MailService mailService) {
        super(baseUserService, baseRoleService, basePermissionService, baseFileDescriptorService, baseConfigurationService,
            UserPermission.class, repository);
        this.mailService = mailService;
    }

    public Boolean controlHoliday(Long id) {
        return repository.findByHolidayView(id);
    }
}
