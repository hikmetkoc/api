package tr.com.meteor.crm.utils.jasper.rest;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tr.com.meteor.crm.utils.jasper.rest.errors.ObjectAccessDeniedException;
import tr.com.meteor.crm.domain.Configuration;
import tr.com.meteor.crm.repository.ConfigurationRepository;
import tr.com.meteor.crm.service.ConfigurationService;

@RestController
@RequestMapping("/api/configurations")
public class ConfigurationController extends GenericIdNameAuditingEntityController<Configuration, String, ConfigurationRepository, ConfigurationService> {

    public ConfigurationController(ConfigurationService service) {
        super(service);
    }

    @Override
    public void update(@RequestBody Configuration configuration) throws Exception {
        if (configuration.getId() == null) {
            throw new ObjectAccessDeniedException(ObjectAccessDeniedException.AccessType.UPDATE, Configuration.class.getSimpleName());
        }

        super.update(configuration);
    }

    @Override
    public void delete(String id) throws Exception {
        throw new ObjectAccessDeniedException(ObjectAccessDeniedException.AccessType.DELETE, Configuration.class.getSimpleName());
    }
}
