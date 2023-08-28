package tr.com.meteor.crm.repository;

import org.springframework.stereotype.Repository;
import tr.com.meteor.crm.domain.OpetFleet;

import java.util.UUID;

@Repository
public interface OpetFleetRepository extends GenericIdEntityRepository<OpetFleet, UUID> {

}
