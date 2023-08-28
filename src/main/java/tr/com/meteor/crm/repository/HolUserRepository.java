package tr.com.meteor.crm.repository;

import org.springframework.stereotype.Repository;
import tr.com.meteor.crm.domain.HolUser;
import tr.com.meteor.crm.domain.Holiday;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface HolUserRepository extends GenericIdNameAuditingEntityRepository<HolUser, UUID> {
    List<HolUser> findByUserId(Long userid);
}
