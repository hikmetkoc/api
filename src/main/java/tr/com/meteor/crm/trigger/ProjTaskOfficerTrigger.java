package tr.com.meteor.crm.trigger;

import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import tr.com.meteor.crm.domain.ProjOfficer;
import tr.com.meteor.crm.domain.ProjTaskOfficer;
import tr.com.meteor.crm.repository.ProjOfficerRepository;
import tr.com.meteor.crm.repository.ProjTaskOfficerRepository;
import tr.com.meteor.crm.service.BaseConfigurationService;
import tr.com.meteor.crm.service.BaseUserService;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Component(ProjTaskOfficerTrigger.QUALIFIER)
public class ProjTaskOfficerTrigger extends Trigger<ProjTaskOfficer, UUID, ProjTaskOfficerRepository> {
    final static String QUALIFIER = "ProjTaskOfficerTrigger";

    private final ProjOfficerRepository projOfficerRepository;

    public ProjTaskOfficerTrigger(CacheManager cacheManager, ProjTaskOfficerRepository contactRepository,
                                  BaseUserService baseUserService, BaseConfigurationService baseConfigurationService, ProjOfficerRepository projOfficerRepository) {
        super(cacheManager, ProjTaskOfficer.class, ProjTaskOfficerTrigger.class, contactRepository, baseUserService, baseConfigurationService);
        this.projOfficerRepository = projOfficerRepository;
    }

    @Override
    public ProjTaskOfficer beforeInsert(@NotNull ProjTaskOfficer newEntity) throws Exception {
        Boolean ownerControl = false;
        List<ProjOfficer> projOfficers = projOfficerRepository.findAll();
        for (ProjOfficer projOfficer: projOfficers) { // Eğer Görev Sorumlusu, Proje Sorumlularından biriyse veya Proje Yöneticisi ise ownerControl = true
            if ((projOfficer.getOwner().getId().equals(newEntity.getOwner().getId()) ||
                projOfficer.getProject().getOwner().getId().equals(newEntity.getOwner().getId())) &&
                projOfficer.getProject().getId().equals(newEntity.getProjtask().getProject().getId()) ) {
                ownerControl = true;
            }
        }
        if (!ownerControl) {
            throw new Exception("Eklemek istediğiniz sorumluyu öncelikle proje sorumlularına eklemelisiniz!");
        }
        return newEntity;
    }

    @Override
    public ProjTaskOfficer beforeUpdate(@NotNull ProjTaskOfficer oldEntity, @NotNull ProjTaskOfficer newEntity) throws Exception {
        return newEntity;
    }


}
