package tr.com.meteor.crm.utils.jasper.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tr.com.meteor.crm.domain.Address;
import tr.com.meteor.crm.repository.AddressRepository;
import tr.com.meteor.crm.service.AddressService;

import java.util.UUID;

@RestController
@RequestMapping("/api/addresses")
public class AddressController extends GenericIdNameAuditingEntityController<Address, UUID, AddressRepository, AddressService> {

    public AddressController(AddressService service) {
        super(service);
    }
}
