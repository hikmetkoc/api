package tr.com.meteor.crm.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.meteor.crm.domain.EntityMetadata;
import tr.com.meteor.crm.repository.EntityMetadataRepository;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class BaseEntityMetadataService {

    private final EntityMetadataRepository repository;

    public BaseEntityMetadataService(EntityMetadataRepository repository) {
        this.repository = repository;
    }

    public List<EntityMetadata> findAll() {
        return repository.findAll();
    }
}
