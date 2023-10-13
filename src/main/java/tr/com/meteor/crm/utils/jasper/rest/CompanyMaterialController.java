package tr.com.meteor.crm.utils.jasper.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tr.com.meteor.crm.domain.Address;
import tr.com.meteor.crm.domain.CompanyMaterial;
import tr.com.meteor.crm.repository.AddressRepository;
import tr.com.meteor.crm.repository.CompanyMaterialRepository;
import tr.com.meteor.crm.service.AddressService;
import tr.com.meteor.crm.service.CompanyMaterialService;

import java.util.UUID;

@RestController
@RequestMapping("/api/company_materials")
public class CompanyMaterialController extends GenericIdNameAuditingEntityController<CompanyMaterial, UUID, CompanyMaterialRepository, CompanyMaterialService> {

    public CompanyMaterialController(CompanyMaterialService service) {
        super(service);
    }
}
