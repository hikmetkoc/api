package tr.com.meteor.crm.utils.jasper.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tr.com.meteor.crm.domain.District;
import tr.com.meteor.crm.repository.DistrictRepository;
import tr.com.meteor.crm.service.DistrictService;

import java.util.UUID;

@RestController
@RequestMapping("/api/districts")
public class DistrictController extends GenericIdNameAuditingEntityController<District, UUID, DistrictRepository, DistrictService> {

    public DistrictController(DistrictService service) {
        super(service);
    }
}
