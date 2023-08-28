package tr.com.meteor.crm.repository;

import org.springframework.stereotype.Repository;
import tr.com.meteor.crm.domain.Document;
import tr.com.meteor.crm.domain.Ikfile;

import java.util.List;
import java.util.UUID;

@Repository
public interface DocumentRepository extends GenericIdNameAuditingEntityRepository<Document, UUID> {
}

