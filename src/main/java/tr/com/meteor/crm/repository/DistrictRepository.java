package tr.com.meteor.crm.repository;

import org.springframework.stereotype.Repository;
import tr.com.meteor.crm.domain.District;

import java.util.UUID;

@Repository
public interface DistrictRepository extends GenericIdNameAuditingEntityRepository<District, UUID> {
}
