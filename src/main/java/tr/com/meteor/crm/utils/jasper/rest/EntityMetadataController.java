package tr.com.meteor.crm.utils.jasper.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tr.com.meteor.crm.domain.EntityMetadata;
import tr.com.meteor.crm.repository.EntityMetadataRepository;
import tr.com.meteor.crm.service.EntityMetadataService;

@RestController
@RequestMapping("/api/entity-metadatas")
public class EntityMetadataController extends GenericIdNameAuditingEntityController<EntityMetadata, String, EntityMetadataRepository, EntityMetadataService> {

    public EntityMetadataController(EntityMetadataService service) {
        super(service);
    }
}
