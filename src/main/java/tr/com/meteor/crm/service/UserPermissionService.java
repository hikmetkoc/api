package tr.com.meteor.crm.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.meteor.crm.domain.*;
import tr.com.meteor.crm.repository.UserPermissionRepository;

import java.time.Instant;
import java.util.*;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserPermissionService extends GenericIdNameAuditingEntityService<UserPermission, UUID, UserPermissionRepository> {
    private final MailService mailService;

    private final UserPermissionRepository userPermissionRepository;

    public UserPermissionService(BaseUserService baseUserService, BaseRoleService baseRoleService,
                                 BasePermissionService basePermissionService, BaseFileDescriptorService baseFileDescriptorService,
                                 BaseConfigurationService baseConfigurationService, UserPermissionRepository repository,
                                 MailService mailService, UserPermissionRepository userPermissionRepository) {
        super(baseUserService, baseRoleService, basePermissionService, baseFileDescriptorService, baseConfigurationService,
            UserPermission.class, repository);
        this.mailService = mailService;
        this.userPermissionRepository = userPermissionRepository;
    }

    public Boolean controlHoliday(Long id) {
        return repository.findByHolidayView(id);
    }

    public void createUserPermission(User user) {
        User defaultUser = baseUserService.getUserFullFetched(2L).get();
        UserPermission userPermission = new UserPermission();
        userPermission.setId(UUID.randomUUID());
        userPermission.setUser(user);
        userPermission.setOwner(defaultUser);
        userPermission.setCreatedBy(defaultUser);
        userPermission.setCreatedDate(Instant.now());
        userPermission.setLastModifiedBy(defaultUser);
        userPermission.setLastModifiedDate(Instant.now());
        userPermission.setSearch(userPermission.getId().toString() + " " + user.getFullName());
        userPermission.setHolidayView(false);
        userPermission.setCreatePayment(false);
        userPermission.setSendInvoice(false);
        userPermission.setSpendInvoice(false);
        userPermission.setCreateUser(false);
        userPermissionRepository.save(userPermission);
    }
}
