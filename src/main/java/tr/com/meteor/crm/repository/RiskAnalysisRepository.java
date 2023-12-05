package tr.com.meteor.crm.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tr.com.meteor.crm.domain.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RiskAnalysisRepository extends GenericIdNameAuditingEntityRepository<RiskAnalysis, UUID> {
}
