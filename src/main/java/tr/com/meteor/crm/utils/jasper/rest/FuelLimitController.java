package tr.com.meteor.crm.utils.jasper.rest;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tr.com.meteor.crm.domain.AttributeValue;
import tr.com.meteor.crm.domain.FuelLimit;
import tr.com.meteor.crm.repository.AttributeValueRepository;
import tr.com.meteor.crm.repository.FuelLimitRepository;
import tr.com.meteor.crm.service.FuelLimitService;
import tr.com.meteor.crm.utils.attributevalues.FuelStatus;
import tr.com.meteor.crm.utils.attributevalues.InvoiceStatus;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/fuellimits")
public class FuelLimitController extends GenericIdNameAuditingEntityController<FuelLimit, UUID, FuelLimitRepository, FuelLimitService> {

    public FuelLimitController(FuelLimitService service, AttributeValueRepository attributeValueRepository) {
        super(service);
        this.attributeValueRepository = attributeValueRepository;
    }

    public final AttributeValueRepository attributeValueRepository;

    @PostMapping("report")
    public ResponseEntity<ByteArrayResource> reportFuelLimit(@RequestParam Instant startDate, @RequestParam Instant endDate) throws Exception {
        return ResponseEntity.ok()
            .header("Content-Disposition", "attachment; filename=" + service.getEntityMetaData().getName() + ".xlsx")
            .contentType(MediaType.parseMediaType("application/vnd.ms-excel;charset=UTF-8"))
            .body(new ByteArrayResource(service.generateExcelFuelLimitReport(getCurrentUser(), startDate, endDate)));
    }

    @PutMapping("/updateStatus")
    public Boolean updateStatus(@RequestParam String id, @RequestParam String status, @RequestParam String unvan,
                                @RequestParam String totalTl, @RequestParam String startDate, @RequestParam String endDate) throws Exception {
        try {
            UUID newId = UUID.fromString(id);
            startDate = startDate + "T05:00:00Z";
            endDate = endDate + "T20:59:59Z";
            Instant start = Instant.parse(startDate);
            Instant end = Instant.parse(endDate);

            List<FuelStatus> invoiceStatuses = Arrays.asList(FuelStatus.values());
            for (FuelStatus invoiceStatus: invoiceStatuses) {
                if (invoiceStatus.getId().equals(status)) {
                    return service.updateStatus(newId, invoiceStatus.getAttributeValue(), unvan, totalTl, start, end);
                }
            }

        } catch (Exception e) {
            // Hata durumunda uygun bir hata yanıtı döndürebilirsiniz
            return false;
        }
        return false;
    }
}
