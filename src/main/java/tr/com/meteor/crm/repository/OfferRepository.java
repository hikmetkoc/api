package tr.com.meteor.crm.repository;

import org.springframework.stereotype.Repository;
import tr.com.meteor.crm.domain.Offer;

import java.util.UUID;

@Repository
public interface OfferRepository extends GenericIdNameAuditingEntityRepository<Offer, UUID> {
}

