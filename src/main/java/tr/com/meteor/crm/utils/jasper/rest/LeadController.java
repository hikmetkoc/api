package tr.com.meteor.crm.utils.jasper.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tr.com.meteor.crm.domain.Lead;
import tr.com.meteor.crm.repository.LeadRepository;
import tr.com.meteor.crm.service.LeadService;

import java.util.UUID;

@RestController
@RequestMapping("/api/leads")
public class LeadController extends GenericIdNameAuditingEntityController<Lead, UUID, LeadRepository, LeadService> {

    public LeadController(LeadService service) {
        super(service);
    }
}
