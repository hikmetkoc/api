package tr.com.meteor.crm.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.meteor.crm.domain.Configuration;
import tr.com.meteor.crm.repository.ConfigurationRepository;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class BaseConfigurationService {

    public static final String CONFIGURATION_BY_ID_CACHE = "configurationByIdCache";

    private final ConfigurationRepository repository;

    public BaseConfigurationService(ConfigurationRepository permissionRepository) {
        this.repository = permissionRepository;
    }

    @Cacheable(cacheNames = CONFIGURATION_BY_ID_CACHE)
    public Configuration getConfigurationById(String id) {
        return repository.findById(id).orElse(null);
    }

    public List<Configuration> getConfigurations() {
        return repository.findAll();
    }
}
