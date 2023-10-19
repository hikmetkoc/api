package tr.com.meteor.crm.trigger;

import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import tr.com.meteor.crm.domain.Material;
import tr.com.meteor.crm.domain.UserAcceptance;
import tr.com.meteor.crm.repository.MaterialRepository;
import tr.com.meteor.crm.repository.UserAcceptanceRepository;
import tr.com.meteor.crm.service.BaseConfigurationService;
import tr.com.meteor.crm.service.BaseUserService;
import tr.com.meteor.crm.service.MobileNotificationService;
import tr.com.meteor.crm.service.PostaGuverciniService;

import javax.validation.constraints.NotNull;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component(UserAcceptanceTrigger.QUALIFIER)
public class UserAcceptanceTrigger extends Trigger<UserAcceptance, UUID, UserAcceptanceRepository> {
    final static String QUALIFIER = "UserAcceptanceTrigger";
    private final PostaGuverciniService postaGuverciniService;

    private final MaterialRepository materialRepository;

    private final MobileNotificationService notificationService;

    public UserAcceptanceTrigger(CacheManager cacheManager, UserAcceptanceRepository repository,
                                 BaseUserService baseUserService, BaseConfigurationService baseConfigurationService,
                                 PostaGuverciniService postaGuverciniService, MaterialRepository materialRepository, MobileNotificationService notificationService) {
        super(cacheManager, UserAcceptance.class, UserAcceptanceTrigger.class, repository, baseUserService, baseConfigurationService);
        this.postaGuverciniService = postaGuverciniService;
        this.materialRepository = materialRepository;
        this.notificationService = notificationService;
    }

    @Override
    public UserAcceptance beforeInsert(@NotNull UserAcceptance newEntity) throws Exception {
        newEntity.setName(newEntity.getUser().getFullName() + "-" + newEntity.getMaterial().getId());
        newEntity.setAssigner(getCurrentUser());
        //todo: KONTROL ET HATA VAR
        List<UserAcceptance> userAcceptanceList = repository.findAll();
        for (UserAcceptance userAcceptance : userAcceptanceList) {
            if (userAcceptance.getMaterial().getName().equals(newEntity.getMaterial().getName()) && userAcceptance.getSubmitDate() == null) {
                throw new Exception("Bu demirbaş şu an " + userAcceptance.getUser().getFullName() + " adlı personelde gözüküyor!");
            }
        }
        // Kullanıcıya kaydedilen zimmet, demirbaşlardaki kayıtlarda da değişir.
        Optional<Material> materials = materialRepository.findById(newEntity.getMaterial().getId());
        materials.get().setUser(newEntity.getUser());
        materialRepository.save(materials.get());
        return newEntity;
    }

    @Override
    public UserAcceptance beforeUpdate(@NotNull UserAcceptance oldEntity, UserAcceptance newEntity) throws Exception {
        if (newEntity.getSubmitDate() != null) {
            Optional<Material> materials = materialRepository.findById(newEntity.getMaterial().getId());
            materials.get().setUser(null);
            materials.get().setDescription("Önceki kullanıcısı: " + newEntity.getUser().getFullName() + ", " +
                DateTimeFormatter.ofPattern("dd-MM-yyyy").withZone(ZoneId.systemDefault()).format(newEntity.getSubmitDate()) + " tarihinde teslim etti.");
            materialRepository.save(materials.get());
        }
        return newEntity;
    }
}
