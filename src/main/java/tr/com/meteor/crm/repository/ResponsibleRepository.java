package tr.com.meteor.crm.repository;

import org.springframework.stereotype.Repository;
import tr.com.meteor.crm.domain.Ikfile;
import tr.com.meteor.crm.domain.Responsible;

import java.util.List;
import java.util.UUID;

@Repository
public interface ResponsibleRepository extends GenericIdNameAuditingEntityRepository<Responsible, UUID> {
    List<Responsible> findByCustomerId(UUID customerid);
}

