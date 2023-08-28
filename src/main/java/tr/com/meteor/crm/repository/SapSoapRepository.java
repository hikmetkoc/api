package tr.com.meteor.crm.repository;

import org.springframework.stereotype.Repository;
import tr.com.meteor.crm.domain.Activity;
import tr.com.meteor.crm.domain.SapSoap;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SapSoapRepository extends GenericIdNameAuditingEntityRepository<SapSoap, UUID> {
    boolean existsByFaturano(String sapSoap);

    List<SapSoap> findByFaturano(String sapSoap);
}

