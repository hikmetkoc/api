package tr.com.meteor.crm.utils.jasper.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tr.com.meteor.crm.domain.AttributeValue;
import tr.com.meteor.crm.repository.AttributeValueRepository;
import tr.com.meteor.crm.service.AttributeValueService;

@RestController
@RequestMapping("/api/attribute-values")
public class AttributeValueController extends GenericIdNameEntityController<AttributeValue, String, AttributeValueRepository, AttributeValueService> {

    public AttributeValueController(AttributeValueService service) {
        super(service);
    }

}
