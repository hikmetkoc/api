package tr.com.meteor.crm.repository;

import org.springframework.stereotype.Repository;
import tr.com.meteor.crm.domain.FileContainer;

import java.util.List;
import java.util.UUID;

@Repository
public interface FileContainerRepository extends GenericIdNameAuditingEntityRepository<FileContainer, UUID> {
    List <FileContainer> findByLocNameAndLocation(String locName, String location);

    List <FileContainer> findByLocNameAndLocationAndSubject(String locName, String location, String subject);
}
