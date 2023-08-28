package tr.com.meteor.crm.utils.jasper.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tr.com.meteor.crm.domain.Contract;
import tr.com.meteor.crm.repository.ContractRepository;
import tr.com.meteor.crm.service.ContractService;

import java.util.UUID;

@RestController
@RequestMapping("/api/contracts")
public class ContractController extends GenericIdNameAuditingEntityController<Contract, UUID, ContractRepository, ContractService> {

    public ContractController(ContractService service) {
        super(service);
    }

    @GetMapping("run-contract-report")
    public void contractReport() throws Exception {
        service.contractReport();
    }
}
