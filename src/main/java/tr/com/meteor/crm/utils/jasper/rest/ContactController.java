package tr.com.meteor.crm.utils.jasper.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tr.com.meteor.crm.domain.Contact;
import tr.com.meteor.crm.repository.ContactRepository;
import tr.com.meteor.crm.service.ContactService;

import java.util.UUID;

@RestController
@RequestMapping("/api/contacts")
public class ContactController extends GenericIdNameAuditingEntityController<Contact, UUID, ContactRepository, ContactService> {

    public ContactController(ContactService service) {
        super(service);
    }
}
