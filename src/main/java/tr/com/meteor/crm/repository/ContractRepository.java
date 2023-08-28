package tr.com.meteor.crm.repository;

import org.springframework.stereotype.Repository;
import tr.com.meteor.crm.domain.Contract;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public interface ContractRepository extends GenericIdNameAuditingEntityRepository<Contract, UUID> {
    //boolean existsByQuoteIdAndStatus_IdNotAndEndDateGreaterThanEqual(String statusId, Instant endDate);

    //List<Contract> findAllByQuoteIdIn(List<UUID> ids);
    List<Contract> findAllByIdIn(List<UUID> ids);

    //List<Contract> findAllByEndDateAfter(Instant instant);
}
