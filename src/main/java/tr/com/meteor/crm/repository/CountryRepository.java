package tr.com.meteor.crm.repository;

import org.springframework.stereotype.Repository;
import tr.com.meteor.crm.domain.Country;

import java.util.UUID;

@Repository
public interface CountryRepository extends GenericIdNameAuditingEntityRepository<Country, UUID> {
}
