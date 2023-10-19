package tr.com.meteor.crm.trigger;

import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import tr.com.meteor.crm.domain.Material;
import tr.com.meteor.crm.domain.UserAcceptance;
import tr.com.meteor.crm.repository.MaterialRepository;
import tr.com.meteor.crm.repository.UserAcceptanceRepository;
import tr.com.meteor.crm.service.BaseConfigurationService;
import tr.com.meteor.crm.service.BaseUserService;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Component(MaterialTrigger.QUALIFIER)
public class MaterialTrigger extends Trigger<Material, UUID, MaterialRepository> {
    final static String QUALIFIER = "MaterialTrigger";

    private final UserAcceptanceRepository userAcceptanceRepository;

    public MaterialTrigger(CacheManager cacheManager, MaterialRepository repository,
                           BaseUserService baseUserService, BaseConfigurationService baseConfigurationService, UserAcceptanceRepository userAcceptanceRepository) {
        super(cacheManager, Material.class, MaterialTrigger.class, repository, baseUserService, baseConfigurationService);
        this.userAcceptanceRepository = userAcceptanceRepository;
    }

    @Override
    public Material beforeInsert(@NotNull Material newEntity) throws Exception {
        newEntity.setName(newEntity.getType().getLabel() + "-" + newEntity.getBrand().getLabel() + "-" + newEntity.getModel() + " - " + newEntity.getSerialNumber());
        if (newEntity.getUser() != null) {
            UserAcceptance userAcceptance = new UserAcceptance();
            userAcceptance.setMaterial(newEntity);
            userAcceptance.setUser(newEntity.getUser());
            userAcceptanceRepository.save(userAcceptance);
        }

        return newEntity;
    }

    @Override
    public Material beforeUpdate(@NotNull Material oldEntity, Material newEntity) throws Exception {
        newEntity.setName(newEntity.getType().getLabel() + "-" + newEntity.getBrand().getLabel() + "-" + newEntity.getModel() + " - " + newEntity.getSerialNumber());
        return newEntity;
    }
}
