package tr.com.meteor.crm.utils.jasper.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tr.com.meteor.crm.domain.CompanyMaterial;
import tr.com.meteor.crm.domain.CorrectGroup;
import tr.com.meteor.crm.repository.CompanyMaterialRepository;
import tr.com.meteor.crm.repository.CorrectGroupRepository;
import tr.com.meteor.crm.service.CompanyMaterialService;
import tr.com.meteor.crm.service.CorrectGroupService;

import java.util.UUID;

@RestController
@RequestMapping("/api/correct_groups")
public class CorrectGroupController extends GenericIdNameAuditingEntityController<CorrectGroup, UUID, CorrectGroupRepository, CorrectGroupService> {

    public CorrectGroupController(CorrectGroupService service) {
        super(service);
    }
}
