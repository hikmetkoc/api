package tr.com.meteor.crm.utils.jasper.rest;


import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tr.com.meteor.crm.domain.Offer;
import tr.com.meteor.crm.repository.OfferRepository;
import tr.com.meteor.crm.service.OfferService;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/api/offers")
public class OfferController extends GenericIdNameAuditingEntityController<Offer, UUID, OfferRepository, OfferService> {

    public OfferController(OfferService service) {
        super(service);
    }

    @PostMapping("/report")
    public ResponseEntity report(@RequestParam Instant startDate, @RequestParam Instant endDate) throws Exception {
        return ResponseEntity.ok()
            .header("Content-Disposition", "attachment; filename=" + service.getEntityMetaData().getName() + ".xlsx")
            .contentType(MediaType.parseMediaType("application/vnd.ms-excel;charset=UTF-8"))
            .body(new ByteArrayResource(service.generateExcelOfferReportForUser(getCurrentUser(), startDate, endDate)));
    }
}
