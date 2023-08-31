package tr.com.meteor.crm.trigger;

import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import tr.com.meteor.crm.domain.HolUser;
import tr.com.meteor.crm.domain.Ikfile;
import tr.com.meteor.crm.domain.User;
import tr.com.meteor.crm.repository.HolUserRepository;
import tr.com.meteor.crm.repository.IkfileRepository;
import tr.com.meteor.crm.repository.RoleRepository;
import tr.com.meteor.crm.repository.UserRepository;
import tr.com.meteor.crm.service.BaseConfigurationService;
import tr.com.meteor.crm.service.BaseUserService;
import tr.com.meteor.crm.service.HolManagerService;
import tr.com.meteor.crm.service.UserPermissionService;
import tr.com.meteor.crm.utils.jasper.rest.UserController;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

@Component(UserTrigger.QUALIFIER)
public class UserTrigger extends Trigger<User, Long, UserRepository> {
    final static String QUALIFIER = "UserTrigger";

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final IkfileRepository ikfileRepository;

    private final HolManagerService holManagerService;

    private final UserPermissionService userPermissionService;
    private final HolUserRepository holUserRepository;
    public UserTrigger(CacheManager cacheManager, UserRepository userRepository, BaseUserService baseUserService,
                       BaseConfigurationService baseConfigurationService, RoleRepository roleRepository, IkfileRepository ikfileRepository, HolManagerService holManagerService, UserPermissionService userPermissionService, HolUserRepository holUserRepository) {
        super(cacheManager, User.class, UserTrigger.class, userRepository, baseUserService, baseConfigurationService);
        this.roleRepository = roleRepository;
        this.ikfileRepository = ikfileRepository;
        this.userRepository = userRepository;
        this.holManagerService = holManagerService;
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
        //ÖZLÜK DOSYASI OLUŞTURMA
        Ikfile ikfile = new Ikfile();
        ikfile.setId(UUID.randomUUID());
        ikfile.setOwner(getCurrentUser());
        ikfile.setUser(newEntity);
        ikfile.setCreatedBy(newEntity.getCreatedBy());
        ikfile.setCreatedDate(newEntity.getCreatedDate());
        ikfile.setLastModifiedBy(newEntity.getLastModifiedBy());
        ikfile.setLastModifiedDate(newEntity.getLastModifiedDate());
        ikfile.setSearch(ikfile.getId().toString() + " " + newEntity.getFullName());
        ikfile.setNufus(false);
        ikfile.setNufuskayit(false);
        ikfile.setIkametgah(false);
        ikfile.setSaglik(false);
        ikfile.setDiploma(false);
        ikfile.setSicil(false);
        ikfile.setVesikalik(false);
        ikfile.setAilebelge(false);
        ikfile.setAskerlik(false);
        ikfile.setIssozlesme(false);
        ikfile.setSgk(false);
        ikfile.setFazlamesai(false);
        ikfile.setKvk(false);
        ikfile.setEhliyet(false);
        ikfile.setZimmetliarac(false);
        ikfile.setGizlilik(false);
        ikfile.setIstanimi(false);
        ikfile.setSubject("Dosya");
        ikfileRepository.save(ikfile);
        // İZİN DETAYLARI OLUŞTURMA
        HolUser holuser = new HolUser();
        holuser.setId(UUID.randomUUID());
        holuser.setUser(newEntity);
        holuser.setCreatedBy(newEntity.getCreatedBy());
        holuser.setCreatedDate(newEntity.getCreatedDate());
        holuser.setLastModifiedBy(newEntity.getLastModifiedBy());
        holuser.setLastModifiedDate(newEntity.getLastModifiedDate());
        holuser.setSearch(holuser.getId().toString() + " " + newEntity.getFullName());
        holuser.setDogTar(newEntity.getBirthDate());
        holuser.setIsBas(newEntity.getStartDate());
        Instant startInstant = newEntity.getStartDate();
        ZonedDateTime startZonedDateTime = startInstant.atZone(ZoneId.systemDefault());
        ZonedDateTime resultZonedDateTime = startZonedDateTime.plusYears(1);
        Instant resultInstant = resultZonedDateTime.toInstant();
        holuser.setYilHak(resultInstant);
        holuser.setKulYil(0.00);
        holuser.setKalYil(0.00);
        holuser.setTopYil(0.00);
        holuser.setKulMaz(0.00);
        holuser.setKalMaz(3.00);
        holuser.setKulBaba(0.00);
        holuser.setKulOlum(0.00);
        holuser.setKulEvl(0.00);
        holuser.setKulDog(0.00);
        holuser.setKulRap(0.00);
        holuser.setKulIdr(0.00);
        holuser.setKulUcr(0.00);
        holuser.setYilGun(0.00);
        holuser.setTopHak(0.00);
        holuser.setYilDevir(0.00);
        holuser.setTopKul(0.00);
        holuser.setTopKulMaz(0.00);
        holUserRepository.save(holuser);

        // İZİN AMİRLERİ OLUŞTURMA (HOLMANAGER)
        holManagerService.createHolManager(newEntity);
        // todo:JHI_ROLE_USER , USER_GROUP ve VARSA BUY_LIMIT oluşturma

        // KULLANICI YETKİLERİ OLUŞTURMA (USER_PERMISSION)
        userPermissionService.createUserPermission(newEntity);
        /**Optional<Role> optionalRole = roleRepository.findById("ROLE_USER");
        Role role = optionalRole.orElseThrow(() -> new RuntimeException("Role not found"));

        Set<Role> userRoles = newEntity.getRoles();
        userRoles.add(role);
        newEntity.setRoles(userRoles);

        User group = userRepository.findById(newEntity.getId()).orElseThrow(() -> new RuntimeException("Group not found"));

        Set<User> groups = newEntity.getGroups();
        groups.add(group);
        newEntity.setGroups(groups);

        User member = userRepository.findById(newEntity.getId()).orElseThrow(() -> new RuntimeException("Member not found"));

        Set<User> members = newEntity.getMembers();
        members.add(member);
        newEntity.setMembers(members);**/

        return newEntity;
    }
    @Override
    public User beforeUpdate(@NotNull User oldEntity, @NotNull User newEntity) throws Exception {
        if (getCurrentUserId() != 2 && getCurrentUserId() != 2000){
            throw new Exception("Düzenleme yetkisi sadece IK Personelindedir...");
        } else {
            //newEntity.setSegments(oldEntity.getSegments()); // newEntity nesnesine segments listesinin atanması
        }
        if (newEntity.getTck().length() > 11) {
            throw new Exception("Lütfen 11 haneli TC giriniz...");
        }

        User ne = repository.getOne(newEntity.getId());
        newEntity.setPassword(ne.getPassword());
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

/*for (int a=1; a<=200; a++) {
            Ikfile ikfile = new Ikfile();
            ikfile.setId(UUID.randomUUID());
            ikfile.setOwner(getCurrentUser());
            long uid = a;
            Optional<User> userOptional = baseUserService.getUserFullFetched(uid);
            if (userOptional.isPresent()) {
                ikfile.setUser(baseUserService.getUserFullFetched(uid).get());
                ikfile.setCreatedBy(newEntity.getCreatedBy());
                ikfile.setCreatedDate(newEntity.getCreatedDate());
                ikfile.setLastModifiedBy(newEntity.getLastModifiedBy());
                ikfile.setLastModifiedDate(newEntity.getLastModifiedDate());
                ikfile.setSearch(ikfile.getId().toString() + " " + newEntity.getFullName());
                ikfile.setNufus(false);
                ikfile.setNufuskayit(false);
                ikfile.setIkametgah(false);
                ikfile.setSaglik(false);
                ikfile.setDiploma(false);
                ikfile.setSicil(false);
                ikfile.setVesikalik(false);
                ikfile.setAilebelge(false);
                ikfile.setAskerlik(false);
                ikfile.setIssozlesme(false);
                ikfile.setSgk(false);
                ikfile.setFazlamesai(false);
                ikfile.setKvk(false);
                ikfile.setEhliyet(false);
                ikfile.setZimmetliarac(false);
                ikfile.setGizlilik(false);
                ikfile.setIstanimi(false);
                ikfile.setSubject("Dosya");
                ikfileRepository.save(ikfile);
            }
        }*/

/*
for (int a=1; a<=200; a++) {
            HolUser holuser = new HolUser();
            holuser.setId(UUID.randomUUID());
            long uid = a;
            Optional<User> userOptional = baseUserService.getUserFullFetched(uid);
            if (userOptional.isPresent()) {
                holuser.setUser(baseUserService.getUserFullFetched(uid).get());
                holuser.setCreatedBy(newEntity.getCreatedBy());
                holuser.setCreatedDate(newEntity.getCreatedDate());
                holuser.setLastModifiedBy(newEntity.getLastModifiedBy());
                holuser.setLastModifiedDate(newEntity.getLastModifiedDate());
                holuser.setSearch(holuser.getId().toString() + " " + newEntity.getFullName());
                holuser.setDogTar(newEntity.getBirthDate());
                holuser.setIsBas(newEntity.getStartDate());
                Instant startInstant = newEntity.getStartDate();
                ZonedDateTime startZonedDateTime = startInstant.atZone(ZoneId.systemDefault());
                ZonedDateTime resultZonedDateTime = startZonedDateTime.plusYears(1);
                Instant resultInstant = resultZonedDateTime.toInstant();
                holuser.setYilHak(resultInstant);
                holuser.setYilGun(14.00);
                holuser.setKulYil(0.00);
                holuser.setKalYil(0.00);
                holuser.setTopYil(0.00);
                holuser.setKulMaz(0.00);
                holuser.setKalMaz(3.00);
                holuser.setKulBaba(0.00);
                holuser.setKulOlum(0.00);
                holuser.setKulEvl(0.00);
                holuser.setKulDog(0.00);
                holUserRepository.save(holuser);
            }
        }
 */
