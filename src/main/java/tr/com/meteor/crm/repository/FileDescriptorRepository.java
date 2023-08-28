package tr.com.meteor.crm.repository;

import org.springframework.stereotype.Repository;
import tr.com.meteor.crm.domain.FileDescriptor;

import java.util.List;
import java.util.UUID;

@Repository
public interface FileDescriptorRepository extends GenericIdNameAuditingEntityRepository<FileDescriptor, UUID> {
    List<FileDescriptor> findAllByEntityId(String entityId);
}


