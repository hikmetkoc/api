package tr.com.meteor.crm.utils.jasper.rest;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tr.com.meteor.crm.domain.Buy;
import tr.com.meteor.crm.domain.Store;
import tr.com.meteor.crm.repository.BuyRepository;
import tr.com.meteor.crm.repository.StoreRepository;
import tr.com.meteor.crm.service.BuyService;
import tr.com.meteor.crm.service.StoreService;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/stores")
public class StoreController extends GenericIdNameAuditingEntityController<Store, UUID, StoreRepository, StoreService> {

    public StoreController(StoreService service) {
        super(service);
    }

    @PutMapping("weekly")
    public List<Store> saveWeekly(@RequestBody List<Store> Stores) throws Exception {
        return service.saveWeekly(Stores);
    }
    @PostMapping("report")
    public ResponseEntity<ByteArrayResource> report(@RequestParam Instant startDate, @RequestParam Instant endDate) throws Exception {
        return ResponseEntity.ok()
            .header("Content-Disposition", "attachment; filename=" + service.getEntityMetaData().getName() + ".xlsx")
            .contentType(MediaType.parseMediaType("application/vnd.ms-excel;charset=UTF-8"))
            .body(new ByteArrayResource(service.generateExcelBuyReport(getCurrentUser(), startDate, endDate)));
    }
}
