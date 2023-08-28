package tr.com.meteor.crm.utils.jasper.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tr.com.meteor.crm.domain.Country;
import tr.com.meteor.crm.repository.CountryRepository;
import tr.com.meteor.crm.service.CountryService;

import java.util.UUID;

@RestController
@RequestMapping("/api/countries")
public class CountryController extends GenericIdNameAuditingEntityController<Country, UUID, CountryRepository, CountryService> {

    public CountryController(CountryService service) {
        super(service);
    }
}
