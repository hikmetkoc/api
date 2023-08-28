package tr.com.meteor.crm.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.meteor.crm.domain.FieldMetadata;
import tr.com.meteor.crm.repository.FieldMetadataRepository;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class BaseFieldMetadataService {

    private final FieldMetadataRepository repository;

    public BaseFieldMetadataService(FieldMetadataRepository repository) {
        this.repository = repository;
    }

    public List<FieldMetadata> findAll() {
        return repository.findAll();
    }
}
