package tr.com.meteor.crm.utils.jasper.rest;


import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tr.com.meteor.crm.domain.Holiday;
import tr.com.meteor.crm.repository.HolidayRepository;
import tr.com.meteor.crm.service.HolidayService;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/api/holidays")
public class HolidayController extends GenericIdNameAuditingEntityController<Holiday, UUID, HolidayRepository, HolidayService> {

    public HolidayController(HolidayService service) {
        super(service);
    }

    @PostMapping("/report")
    public ResponseEntity report(@RequestParam String year, @RequestParam String month) throws Exception {
        return ResponseEntity.ok()
            .header("Content-Disposition", "attachment; filename=" + service.getEntityMetaData().getName() + ".xlsx")
            .contentType(MediaType.parseMediaType("application/vnd.ms-excel;charset=UTF-8"))
            .body(new ByteArrayResource(service.generateExcelHolidayReport(getCurrentUser(), year, month)));
    }

    @PostMapping("/sendnotificationmail")
    public ResponseEntity sendMail(@RequestParam String receiver, @RequestParam String subject, @RequestParam String message) throws Exception{
        service.notificationMail(receiver, subject, message);
        return ResponseEntity.ok().build();
    }
}
