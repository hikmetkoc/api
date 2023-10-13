package tr.com.meteor.crm.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.meteor.crm.domain.*;
import tr.com.meteor.crm.repository.FuelLimitRepository;
import tr.com.meteor.crm.repository.FuelRiskRepository;

import java.util.*;

@Service
@Transactional(rollbackFor = Exception.class)
public class FuelRiskService extends GenericIdNameAuditingEntityService<FuelRisk, UUID, FuelRiskRepository> {

    private final FuelLimitRepository fuelLimitRepository;

    public FuelRiskService(BaseUserService baseUserService, BaseRoleService baseRoleService,
                           BasePermissionService basePermissionService, BaseFileDescriptorService baseFileDescriptorService,
                           BaseConfigurationService baseConfigurationService,
                           FuelRiskRepository repository, FuelLimitRepository fuelLimitRepository) {
        super(baseUserService, baseRoleService, basePermissionService, baseFileDescriptorService, baseConfigurationService,
            FuelRisk.class, repository);
        this.fuelLimitRepository = fuelLimitRepository;
    }

    public String newPerson(FuelRisk risk, UUID id) throws Exception {
        Optional<FuelLimit> fuelLimit = fuelLimitRepository.findById(id);
        if (fuelLimit.isPresent()) {
            risk.setFuellimit(fuelLimit.get());
            repository.save(risk);
        } else {
            throw new Exception("ID HATALI");
        }
        return "Kayıt Başarılı";
    }
}
