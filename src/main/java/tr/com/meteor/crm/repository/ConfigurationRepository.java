package tr.com.meteor.crm.repository;

import org.springframework.stereotype.Repository;
import tr.com.meteor.crm.domain.Configuration;

@Repository
public interface ConfigurationRepository extends GenericIdNameAuditingEntityRepository<Configuration, String> {

}
