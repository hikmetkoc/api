package tr.com.meteor.crm.utils.jasper.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tr.com.meteor.crm.domain.Attribute;
import tr.com.meteor.crm.repository.AttributeRepository;
import tr.com.meteor.crm.service.AttributeService;

@RestController
@RequestMapping("/api/attributes")
public class AttributeController extends GenericIdNameEntityController<Attribute, String, AttributeRepository, AttributeService> {

    public AttributeController(AttributeService service) {
        super(service);
    }
}
