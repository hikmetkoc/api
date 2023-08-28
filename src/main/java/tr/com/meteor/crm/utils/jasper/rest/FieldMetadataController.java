package tr.com.meteor.crm.utils.jasper.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tr.com.meteor.crm.domain.FieldMetadata;
import tr.com.meteor.crm.repository.FieldMetadataRepository;
import tr.com.meteor.crm.service.FieldMetadataService;

@RestController
@RequestMapping("/api/field-metadatas")
public class FieldMetadataController extends GenericIdNameAuditingEntityController<FieldMetadata, String, FieldMetadataRepository, FieldMetadataService> {

    public FieldMetadataController(FieldMetadataService service) {
        super(service);
    }
}
