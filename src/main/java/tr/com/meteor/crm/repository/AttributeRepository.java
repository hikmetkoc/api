package tr.com.meteor.crm.repository;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import tr.com.meteor.crm.domain.Attribute;

import java.util.List;

@Repository
public interface AttributeRepository extends GenericIdNameEntityRepository<Attribute, String> {

    String cacheName = "attribute";

    @Override
    @Cacheable(cacheNames = cacheName)
    List<Attribute> findAll();
}
