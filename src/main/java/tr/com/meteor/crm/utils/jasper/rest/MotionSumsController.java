package tr.com.meteor.crm.utils.jasper.rest;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tr.com.meteor.crm.domain.MotionSums;
import tr.com.meteor.crm.repository.MotionSumsRepository;
import tr.com.meteor.crm.service.MotionSumsService;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/motionsumss")
public class MotionSumsController extends GenericIdNameAuditingEntityController<MotionSums, UUID, MotionSumsRepository, MotionSumsService> {

    public MotionSumsController(MotionSumsService service) {
        super(service);
    }

    @PostMapping("report-custom-task-wizard")
    public ResponseEntity<ByteArrayResource> reportMotionSumsWizard(@RequestParam Instant startDate, @RequestParam Instant endDate) throws Exception {
        return ResponseEntity.ok()
            .header("Content-Disposition", "attachment; filename=" + service.getEntityMetaData().getName() + ".xlsx")
            .contentType(MediaType.parseMediaType("application/vnd.ms-excel;charset=UTF-8"))
            .body(new ByteArrayResource(service.generateExcelMotionSumsWizardReportForUser(getCurrentUser(), startDate, endDate)));
    }

    @PostMapping("report")
    public ResponseEntity<ByteArrayResource> reportMotionSums(@RequestParam Instant startDate, @RequestParam Instant endDate) throws Exception {
        return ResponseEntity.ok()
            .header("Content-Disposition", "attachment; filename=" + service.getEntityMetaData().getName() + ".xlsx")
            .contentType(MediaType.parseMediaType("application/vnd.ms-excel;charset=UTF-8"))
            .body(new ByteArrayResource(service.generateExcelMotionSumsReport(getCurrentUser(), startDate, endDate)));
    }
}
