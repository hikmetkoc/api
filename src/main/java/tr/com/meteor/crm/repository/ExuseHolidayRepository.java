package tr.com.meteor.crm.repository;

import org.springframework.stereotype.Repository;
import tr.com.meteor.crm.domain.ExuseHoliday;
import tr.com.meteor.crm.domain.Holiday;

import java.util.UUID;

@Repository
public interface ExuseHolidayRepository extends GenericIdNameAuditingEntityRepository<ExuseHoliday, UUID> {
}
