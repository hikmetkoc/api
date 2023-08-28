package tr.com.meteor.crm.repository;

import org.springframework.stereotype.Repository;
import tr.com.meteor.crm.domain.QuoteVersion;

import java.util.UUID;


@Repository
public interface QuoteVersionRepository extends GenericIdNameAuditingEntityRepository<QuoteVersion, UUID> {
}
