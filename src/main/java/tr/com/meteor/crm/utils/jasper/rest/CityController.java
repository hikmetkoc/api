package tr.com.meteor.crm.utils.jasper.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tr.com.meteor.crm.domain.City;
import tr.com.meteor.crm.repository.CityRepository;
import tr.com.meteor.crm.service.CityService;

import java.util.UUID;

@RestController
@RequestMapping("/api/cities")
public class CityController extends GenericIdNameAuditingEntityController<City, UUID, CityRepository, CityService> {

    public CityController(CityService service) {
        super(service);
    }
}
