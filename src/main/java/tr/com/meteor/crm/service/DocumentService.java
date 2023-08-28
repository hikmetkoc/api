package tr.com.meteor.crm.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.meteor.crm.domain.Document;
import tr.com.meteor.crm.domain.Ikfile;
import tr.com.meteor.crm.domain.Role;
import tr.com.meteor.crm.domain.User;
import tr.com.meteor.crm.repository.ContractRepository;
import tr.com.meteor.crm.repository.DocumentRepository;
import tr.com.meteor.crm.repository.IkfileRepository;
import tr.com.meteor.crm.repository.QuoteRepository;
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
public class DocumentService extends GenericIdNameAuditingEntityService<Document, UUID, DocumentRepository> {
    private final QuoteRepository quoteRepository;
    private final ContractRepository contractRepository;
    private final MailService mailService;

    public DocumentService(BaseUserService baseUserService, BaseRoleService baseRoleService,
                           BasePermissionService basePermissionService, BaseFileDescriptorService baseFileDescriptorService,
                           BaseConfigurationService baseConfigurationService, DocumentRepository repository,
                           QuoteRepository quoteRepository, ContractRepository contractRepository, MailService mailService) {
        super(baseUserService, baseRoleService, basePermissionService, baseFileDescriptorService, baseConfigurationService,
            Document.class, repository);
        this.quoteRepository = quoteRepository;
        this.contractRepository = contractRepository;
        this.mailService = mailService;
    }
}
