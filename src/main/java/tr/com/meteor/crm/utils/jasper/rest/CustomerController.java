package tr.com.meteor.crm.utils.jasper.rest;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tr.com.meteor.crm.domain.Customer;
import tr.com.meteor.crm.repository.CustomerRepository;
import tr.com.meteor.crm.service.CustomerService;
import tr.com.meteor.crm.service.dto.NearestCustomersInputDTO;
import tr.com.meteor.crm.service.dto.NearestCustomersOutputDTO;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/customers")
public class CustomerController extends GenericIdNameAuditingEntityController<Customer, UUID, CustomerRepository, CustomerService> {

    public CustomerController(CustomerService service) {
        super(service);
    }

    @PostMapping("get-import-template")
    public ResponseEntity<Resource> getImportTemplate() throws Exception {
        return service.getImportTemplate();
    }

    @PostMapping("nearest-customers")
    public List<NearestCustomersOutputDTO> getNearestCustomers(@RequestBody NearestCustomersInputDTO dto) throws Exception {
        return service.getNearestCustomers(dto);
    }
}
