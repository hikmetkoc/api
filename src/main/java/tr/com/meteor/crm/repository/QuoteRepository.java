package tr.com.meteor.crm.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tr.com.meteor.crm.domain.Quote;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface QuoteRepository extends GenericIdNameAuditingEntityRepository<Quote, UUID> {
    Optional<Quote> findTopByOrderByCreatedDateDesc();
}
