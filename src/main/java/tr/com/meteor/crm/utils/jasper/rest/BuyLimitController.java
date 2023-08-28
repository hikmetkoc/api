package tr.com.meteor.crm.utils.jasper.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tr.com.meteor.crm.domain.BuyLimit;
import tr.com.meteor.crm.repository.BuyLimitRepository;
import tr.com.meteor.crm.service.BuyLimitService;

import java.util.UUID;

@RestController
@RequestMapping("/api/buylimits")
public class BuyLimitController extends GenericIdNameAuditingEntityController<BuyLimit, UUID, BuyLimitRepository, BuyLimitService> {

    public BuyLimitController(BuyLimitService service) {
        super(service);
    }
   /*@PostMapping("report")
    public ResponseEntity<ByteArrayResource> reportBuy(@RequestParam Instant startDate, @RequestParam Instant endDate) throws Exception {
        return ResponseEntity.ok()
            .header("Content-Disposition", "attachment; filename=" + service.getEntityMetaData().getName() + ".xlsx")
            .contentType(MediaType.parseMediaType("application/vnd.ms-excel;charset=UTF-8"))
            .body(new ByteArrayResource(service.generateExcelBuyReport(getCurrentUser(), startDate, endDate)));
    }*/
}
