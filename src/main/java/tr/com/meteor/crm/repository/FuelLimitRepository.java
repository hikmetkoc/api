package tr.com.meteor.crm.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tr.com.meteor.crm.domain.AttributeValue;
import tr.com.meteor.crm.domain.FuelLimit;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public interface FuelLimitRepository extends GenericIdNameAuditingEntityRepository<FuelLimit, UUID> {
    @Transactional
    @Modifying
    @Query("UPDATE FuelLimit p SET p.status = :status, p.unvan = :unvan, p.totalTl = :totalTl, p.startDate = :startDate, p.endDate = :endDate, p.okeyFirst = :okeyFirst WHERE p.id = :id")
    void updateStatusById(@Param("status") AttributeValue status, @Param("unvan") String unvan, @Param("totalTl") BigDecimal totalTl, @Param("startDate") Instant startDate, @Param("endDate") Instant endDate, @Param("okeyFirst") Instant okeyFirst, @Param("id") UUID id);
}
