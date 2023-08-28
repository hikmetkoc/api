package tr.com.meteor.crm.utils.jasper.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tr.com.meteor.crm.domain.Address;
import tr.com.meteor.crm.domain.Iban;
import tr.com.meteor.crm.repository.AddressRepository;
import tr.com.meteor.crm.repository.IbanRepository;
import tr.com.meteor.crm.service.AddressService;
import tr.com.meteor.crm.service.IbanService;

import java.util.UUID;

@RestController
@RequestMapping("/api/ibans")
public class IbanController extends GenericIdNameAuditingEntityController<Iban, UUID, IbanRepository, IbanService> {

    public IbanController(IbanService service) {
        super(service);
    }
}
