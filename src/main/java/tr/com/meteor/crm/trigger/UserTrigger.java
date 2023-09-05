package tr.com.meteor.crm.trigger;

import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import tr.com.meteor.crm.domain.Role;
import tr.com.meteor.crm.domain.User;
import tr.com.meteor.crm.repository.HolUserRepository;
import tr.com.meteor.crm.repository.IkfileRepository;
import tr.com.meteor.crm.repository.RoleRepository;
import tr.com.meteor.crm.repository.UserRepository;
import tr.com.meteor.crm.security.RolesConstants;
import tr.com.meteor.crm.service.*;
import tr.com.meteor.crm.utils.jasper.rest.UserController;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Component(UserTrigger.QUALIFIER)
public class UserTrigger extends Trigger<User, Long, UserRepository> {
    final static String QUALIFIER = "UserTrigger";

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final IkfileRepository ikfileRepository;

    private final HolManagerService holManagerService;

    private final PasswordEncoder passwordEncoder;
    private final UserService service;
    private final HolUserService holUserService;
    private final IkfileService ikfileService;
    private final UserPermissionService userPermissionService;
    private final HolUserRepository holUserRepository;
    public UserTrigger(CacheManager cacheManager, UserRepository userRepository, BaseUserService baseUserService,
                       BaseConfigurationService baseConfigurationService, RoleRepository roleRepository, IkfileRepository ikfileRepository, HolManagerService holManagerService, PasswordEncoder passwordEncoder, UserService service, HolUserService holUserService, IkfileService ikfileService, UserPermissionService userPermissionService, HolUserRepository holUserRepository) {
        super(cacheManager, User.class, UserTrigger.class, userRepository, baseUserService, baseConfigurationService);
        this.roleRepository = roleRepository;
        this.ikfileRepository = ikfileRepository;
        this.userRepository = userRepository;
        this.holManagerService = holManagerService;
        this.passwordEncoder = passwordEncoder;
        this.service = service;
        this.holUserService = holUserService;
        this.ikfileService = ikfileService;
        this.userPermissionService = userPermissionService;
        this.holUserRepository = holUserRepository;
    }

    @Override
    public User beforeInsert(@NotNull User newEntity) throws Exception {

        if (newEntity.getPassword() == null) {
            newEntity.setPassword("$2a$12$fJXM0XDuDLH1RXej5cPLyeAgrAvfTT95OVpSVQW3UPTHz0crTXTgO");
        }
        if (newEntity.getTck().length() > 11) {
            throw new Exception("Lütfen 11 haneli TC giriniz...");
        }

        if (newEntity.getStartDate() == null) {
            throw new Exception("Lütfen işe başlangıç tarihini girin.");
        }
        if (newEntity.getBirthDate() == null) {
            throw new Exception("Lütfen doğum tarihini girin.");
        }
        return newEntity;
    }

    @Override
    public User afterInsert(User newEntity) throws Exception {
        // STRING ŞİFRE ENCRYPT İŞLEMİ
        String encryptedPassword = passwordEncoder.encode(newEntity.getPassword());
        newEntity.setPassword(encryptedPassword);
        //ÖZLÜK DOSYASI OLUŞTURMA
        ikfileService.createIkFile(newEntity);

        // İZİN DETAYLARI OLUŞTURMA
        holUserService.createHolUser(newEntity);

        // İZİN AMİRLERİ OLUŞTURMA (HOLMANAGER)
        holManagerService.createHolManager(newEntity);

        // todo:JHI_ROLE_USER , USER_GROUP ve VARSA BUY_LIMIT oluşturma
        Set<Role> roles = new HashSet<>();
        roleRepository.findById(RolesConstants.USER).ifPresent(roles::add);
        newEntity.setRoles(roles);

        Set<User> groups = new HashSet<>();
        groups.add(newEntity);
        // ADD = GROUP_ID , NEWENTITY = MEMBER_ID
        //groups.add(baseUserService.getUserFullFetched(2000L).get());
        newEntity.setGroups(groups);

        // KULLANICI YETKİLERİ OLUŞTURMA (USER_PERMISSION)
        userPermissionService.createUserPermission(newEntity);

        return newEntity;
    }
    @Override
    public User beforeUpdate(@NotNull User oldEntity, @NotNull User newEntity) throws Exception {
        if (getCurrentUserId() != 2 && getCurrentUserId() != 2000){
            throw new Exception("Düzenleme yetkisi Yönetici Personelindedir...");
        }
        if (newEntity.getTck().length() > 11) {
            throw new Exception("Lütfen 11 haneli TC giriniz...");
        }

        if (newEntity.getPassword() != null && !newEntity.getPassword().isEmpty()) {
            String encryptedPassword = passwordEncoder.encode(newEntity.getPassword());
            newEntity.setPassword(encryptedPassword);
        } else {
            User ne = repository.getOne(newEntity.getId());
            newEntity.setPassword(ne.getPassword());
        }
        return newEntity;
    }
    @Override
    public void onClearCache(User user) {
        Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE)).evict(user.getLogin());
        Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_EMAIL_CACHE)).evict(user.getEmail());
        Objects.requireNonNull(cacheManager.getCache(User.class.getName())).clear();
        Objects.requireNonNull(cacheManager.getCache(User.class.getName() + ".roles")).clear();
    }
}
