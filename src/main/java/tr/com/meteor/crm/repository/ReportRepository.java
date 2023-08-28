package tr.com.meteor.crm.repository;

import org.springframework.stereotype.Repository;
import tr.com.meteor.crm.domain.Report;

import java.util.UUID;

@Repository
public interface ReportRepository extends GenericIdNameAuditingEntityRepository<Report, UUID> {
}
