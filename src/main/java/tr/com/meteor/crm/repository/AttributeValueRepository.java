package tr.com.meteor.crm.repository;


import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import tr.com.meteor.crm.domain.AttributeValue;

import java.util.List;
import java.util.Optional;

@Repository
public interface AttributeValueRepository extends GenericIdNameEntityRepository<AttributeValue, String> {
    String cacheName = "attribute-value";

    @Override
    @Cacheable(cacheNames = cacheName)
    //List<AttributeValue> findAll();

    Optional<AttributeValue> findById(String av);
}
