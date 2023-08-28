package tr.com.meteor.crm.utils.jasper.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tr.com.meteor.crm.domain.SummaryOpetSale;
import tr.com.meteor.crm.repository.SummaryOpetSaleRepository;
import tr.com.meteor.crm.service.SummaryOpetSaleService;

import java.util.UUID;

@RestController
@RequestMapping("/api/summary-opet-sales")
public class SummaryOpetSaleController extends GenericIdEntityController<SummaryOpetSale, UUID, SummaryOpetSaleRepository, SummaryOpetSaleService> {

    public SummaryOpetSaleController(SummaryOpetSaleService service) {
        super(service);
    }
}
