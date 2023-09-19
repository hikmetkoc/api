package tr.com.meteor.crm.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tr.com.meteor.crm.domain.ContProduct;
import tr.com.meteor.crm.domain.HolUser;
import tr.com.meteor.crm.domain.Holiday;
import tr.com.meteor.crm.domain.Store;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public interface HolidayRepository extends GenericIdNameAuditingEntityRepository<Holiday, UUID> {

    List<Holiday> findByStartDateBetweenOrEndDateBetween(Instant startOfSelectedMonth, Instant endOfSelectedMonth, Instant startOfSelectedMonth2, Instant endOfSelectedMonth2);

    @Transactional
    @Modifying
    @Query("UPDATE Holiday h SET h.base64File = :base64File WHERE h.id = :id")
    void updateBase64(@Param("id") UUID id, @Param("base64File") String base64File);
}
