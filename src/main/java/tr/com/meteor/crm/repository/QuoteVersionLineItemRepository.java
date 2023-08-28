package tr.com.meteor.crm.repository;

import org.springframework.stereotype.Repository;
import tr.com.meteor.crm.domain.QuoteVersionLineItem;

import java.util.UUID;

@Repository
public interface QuoteVersionLineItemRepository extends GenericIdEntityRepository<QuoteVersionLineItem, UUID> {

}
