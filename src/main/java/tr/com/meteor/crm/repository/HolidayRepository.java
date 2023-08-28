package tr.com.meteor.crm.repository;

import org.springframework.stereotype.Repository;
import tr.com.meteor.crm.domain.ContProduct;
import tr.com.meteor.crm.domain.HolUser;
import tr.com.meteor.crm.domain.Holiday;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public interface HolidayRepository extends GenericIdNameAuditingEntityRepository<Holiday, UUID> {

    List<Holiday> findByStartDateBetweenOrEndDateBetween(Instant startOfSelectedMonth, Instant endOfSelectedMonth, Instant startOfSelectedMonth2, Instant endOfSelectedMonth2);

}
