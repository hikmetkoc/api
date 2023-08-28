package tr.com.meteor.crm.repository;

import org.springframework.stereotype.Repository;
import tr.com.meteor.crm.domain.City;

import java.util.UUID;

@Repository
public interface CityRepository extends GenericIdNameAuditingEntityRepository<City, UUID> {
}
