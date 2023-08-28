package tr.com.meteor.crm.config;

import io.github.jhipster.config.JHipsterConstants;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import tr.com.meteor.crm.domain.Configuration;
import tr.com.meteor.crm.repository.ConfigurationRepository;
import tr.com.meteor.crm.utils.configuration.Configurations;
import tr.com.meteor.crm.utils.metadata.MetadataReader;
import tr.com.meteor.crm.utils.jasper.rest.errors.RecordNotFoundException;

import javax.annotation.PostConstruct;
import java.util.Optional;

@Component
@Profile(JHipsterConstants.SPRING_PROFILE_PRODUCTION)
public class BackendVersionUpdater {

    private final PlatformTransactionManager platformTransactionManager;
    private final ConfigurationRepository configurationRepository;
    private final MetadataReader metadataReader;

    public BackendVersionUpdater(PlatformTransactionManager platformTransactionManager,
                                 ConfigurationRepository configurationRepository, MetadataReader metadataReader) {
        this.platformTransactionManager = platformTransactionManager;
        this.configurationRepository = configurationRepository;
        this.metadataReader = metadataReader;
    }

    @PostConstruct
    @DependsOn("MetadataReader")
    public void updateVersion() throws Exception {
        TransactionTemplate tmpl = new TransactionTemplate(platformTransactionManager);
        tmpl.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                update(configurationRepository);
            }
        });
    }

    public static void update(ConfigurationRepository configurationRepository) {
        Optional<Configuration> configuration = configurationRepository.findById(Configurations.BACKEND_VERSION.getId());

        if (configuration.isPresent()) {
            Configuration cfg = configuration.get();
            cfg.setValue(cfg.getIntegerValue() + 1);
            try {
                configurationRepository.save(cfg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            throw new RecordNotFoundException(Configuration.class.getSimpleName(), Configurations.BACKEND_VERSION.getId());
        }
    }
}
