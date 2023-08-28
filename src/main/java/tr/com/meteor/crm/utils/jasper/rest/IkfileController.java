package tr.com.meteor.crm.utils.jasper.rest;


import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tr.com.meteor.crm.domain.Activity;
import tr.com.meteor.crm.domain.Ikfile;
import tr.com.meteor.crm.repository.ActivityRepository;
import tr.com.meteor.crm.repository.IkfileRepository;
import tr.com.meteor.crm.service.ActivityService;
import tr.com.meteor.crm.service.IkfileService;
import tr.com.meteor.crm.service.dto.CheckInOutDTO;
import tr.com.meteor.crm.service.dto.QuoteSendDocumentDTO;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/ikfiles")
public class IkfileController extends GenericIdNameAuditingEntityController<Ikfile, UUID, IkfileRepository, IkfileService> {

    public IkfileController(IkfileService service) {
        super(service);
    }

    @PutMapping("/checkIn")
    public void updateCheckIn(@RequestBody CheckInOutDTO checkInOutDTO) throws Exception {
        service.updateCheckIn(checkInOutDTO);
    }

    @PutMapping("/checkOut")
    public void updateCheckOut(@RequestBody CheckInOutDTO checkInOutDTO) throws Exception {
        service.updateCheckOut(checkInOutDTO);
    }

    @PostMapping("documents")
    public Map<String, String> getDocumentList() {
        return service.getDocumentList();
    }

    @PutMapping("documents")
    public void sendDocuments(@RequestBody QuoteSendDocumentDTO dto) throws Exception {
        service.sendDocuments(dto, getCurrentUser());
    }
    @PostMapping("/report")
    public ResponseEntity report(@RequestParam Instant startDate, @RequestParam Instant endDate) throws Exception {
        return ResponseEntity.ok()
            .header("Content-Disposition", "attachment; filename=" + service.getEntityMetaData().getName() + ".xlsx")
            .contentType(MediaType.parseMediaType("application/vnd.ms-excel;charset=UTF-8"))
            .body(new ByteArrayResource(service.generateExcelActivityReportForUser(getCurrentUser(), startDate, endDate)));
    }
}
