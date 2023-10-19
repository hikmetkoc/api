package tr.com.meteor.crm.utils.jasper.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tr.com.meteor.crm.domain.Material;
import tr.com.meteor.crm.repository.MaterialRepository;
import tr.com.meteor.crm.service.MaterialService;

import java.util.UUID;

@RestController
@RequestMapping("/api/materials")
public class MaterialController extends GenericIdNameAuditingEntityController<Material, UUID, MaterialRepository, MaterialService> {

    public MaterialController(MaterialService service) {
        super(service);
    }
}
