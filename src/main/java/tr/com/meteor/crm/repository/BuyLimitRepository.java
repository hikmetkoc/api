package tr.com.meteor.crm.repository;

import org.springframework.stereotype.Repository;
import tr.com.meteor.crm.domain.AttributeValue;
import tr.com.meteor.crm.domain.BuyLimit;

import java.util.List;
import java.util.UUID;

@Repository
public interface BuyLimitRepository extends GenericIdNameAuditingEntityRepository<BuyLimit, UUID> {
    //List<BuyLimit> findByUserId(Long limitid);
    List<BuyLimit> findByUserIdAndMaliyet(Long userId, AttributeValue maliyet);

    List<BuyLimit> findByUserId(Long userId);
}
