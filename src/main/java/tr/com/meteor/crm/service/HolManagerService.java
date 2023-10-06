package tr.com.meteor.crm.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.meteor.crm.domain.HolManager;
import tr.com.meteor.crm.domain.User;
import tr.com.meteor.crm.repository.HolManagerRepository;
import tr.com.meteor.crm.utils.attributevalues.TaskType;

import java.time.Instant;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class HolManagerService extends GenericIdNameAuditingEntityService<HolManager, UUID, HolManagerRepository> {

    private final HolManagerRepository holManagerRepository;
    public HolManagerService(BaseUserService baseUserService, BaseRoleService baseRoleService,
                             BasePermissionService basePermissionService, BaseFileDescriptorService baseFileDescriptorService,
                             BaseConfigurationService baseConfigurationService,
                             HolManagerRepository repository, HolManagerRepository holManagerRepository) {
        super(baseUserService, baseRoleService, basePermissionService, baseFileDescriptorService, baseConfigurationService,
            HolManager.class, repository);
        this.holManagerRepository = holManagerRepository;
    }

    public void createHolManager(User user) {
        User defaultUser = baseUserService.getUserFullFetched(2L).get();
        Long chiefUser = Long.valueOf(user.getBirim().getIlgilibirim());
        if (!getCurrentUser().getId().equals(2000L) || !getCurrentUser().getId().equals(2001L) || !getCurrentUser().getId().equals(2L)) {
            chiefUser = getCurrentUserId();
        }
        User chief = baseUserService.getUserFullFetched(chiefUser).get();
        User manager = lookManager(chief);
        User director = baseUserService.getUserFullFetched(101L).get();
        if (manager.getId().equals(90L) || manager.getId().equals(91L) || manager.getId().equals(93L)) {
            director = baseUserService.getUserFullFetched(99L).get();
        } else if (manager.getId().equals(103L) || manager.getId().equals(133L)) {
            director = baseUserService.getUserFullFetched(102L).get();
        }

        HolManager holManager = new HolManager();
        holManager.setId(UUID.randomUUID());
        holManager.setUser(user);
        holManager.setCreatedBy(defaultUser);
        holManager.setCreatedDate(Instant.now());
        holManager.setLastModifiedBy(defaultUser);
        holManager.setLastModifiedDate(Instant.now());
        holManager.setSearch(holManager.getId().toString() + " " + user.getLogin());
        holManager.setName(user.getLogin());
        holManager.setChief(chief);
        holManager.setManager(manager);
        holManager.setDirector(director);
        holManagerRepository.save(holManager);
    }

    public User lookManager(User chief) {
        User manager = new User();
        if (chief.getBirim().getId().equals(TaskType.TaskBirim.BIRIM_IK.getId())) {
            manager = baseUserService.getUserFullFetched(90L).get();
        } else if (chief.getBirim().getId().equals(TaskType.TaskBirim.BIRIM_IT.getId())) {
            manager = baseUserService.getUserFullFetched(90L).get();
        } else if (chief.getBirim().getId().equals(TaskType.TaskBirim.BIRIM_Muh.getId())) {
            manager = baseUserService.getUserFullFetched(90L).get();
        } else if (chief.getBirim().getId().equals(TaskType.TaskBirim.BIRIM_Risk.getId())) {
            manager = baseUserService.getUserFullFetched(90L).get();
        } else if (chief.getBirim().getId().equals(TaskType.TaskBirim.BIRIM_Imim.getId())) {
            manager = baseUserService.getUserFullFetched(90L).get();
        } else if (chief.getBirim().getId().equals(TaskType.TaskBirim.BIRIM_Paz.getId())) {
            manager = baseUserService.getUserFullFetched(90L).get();
        } else if (chief.getBirim().getId().equals(TaskType.TaskBirim.BIRIM_Web.getId())) {
            manager = baseUserService.getUserFullFetched(21L).get();
        } else if (chief.getBirim().getId().equals(TaskType.TaskBirim.BIRIM_Fin.getId())) {
            manager = baseUserService.getUserFullFetched(99L).get();
        } else if (chief.getBirim().getId().equals(TaskType.TaskBirim.BIRIM_Kasa.getId())) {
            manager = baseUserService.getUserFullFetched(99L).get();
        } else if (chief.getBirim().getId().equals(TaskType.TaskBirim.BIRIM_Satis.getId())) {
            manager = baseUserService.getUserFullFetched(99L).get();
        } else if (chief.getBirim().getId().equals(TaskType.TaskBirim.BIRIM_Satin.getId())) {
            manager = baseUserService.getUserFullFetched(91L).get();
        } else if (chief.getBirim().getId().equals(TaskType.TaskBirim.BIRIM_Ter.getId())) {
            manager = baseUserService.getUserFullFetched(91L).get();
        } else if (chief.getBirim().getId().equals(TaskType.TaskBirim.BIRIM_Ins.getId())) {
            manager = baseUserService.getUserFullFetched(94L).get();
        } else if (chief.getBirim().getId().equals(TaskType.TaskBirim.BIRIM_Kafe.getId())) {
            manager = baseUserService.getUserFullFetched(91L).get();
        } else if (chief.getBirim().getId().equals(TaskType.TaskBirim.BIRIM_Loher.getId())) {
            manager = baseUserService.getUserFullFetched(102L).get();
        } else if (chief.getBirim().getId().equals(TaskType.TaskBirim.BIRIM_Avelice.getId())) {
            manager = baseUserService.getUserFullFetched(102L).get();
        } else if (chief.getBirim().getId().equals(TaskType.TaskBirim.BIRIM_Yonetim.getId())) {
            manager = baseUserService.getUserFullFetched(101L).get();
        } else if (chief.getBirim().getId().equals(TaskType.TaskBirim.BIRIM_Gnl.getId())) {
            manager = baseUserService.getUserFullFetched(101L).get();
        }
        return manager;
    }
}
