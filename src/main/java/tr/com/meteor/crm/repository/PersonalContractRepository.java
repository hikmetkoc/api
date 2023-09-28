package tr.com.meteor.crm.repository;

import org.springframework.stereotype.Repository;
import tr.com.meteor.crm.domain.City;
import tr.com.meteor.crm.domain.PersonalContract;

import java.util.UUID;

@Repository
public interface PersonalContractRepository extends GenericIdNameAuditingEntityRepository<PersonalContract, UUID> {
    PersonalContract findBySozlesme(String sozlesme);
}
