package tr.com.meteor.crm.utils.jasper.rest;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tr.com.meteor.crm.domain.CustomTask;
import tr.com.meteor.crm.repository.CustomTaskRepository;
import tr.com.meteor.crm.service.CustomTaskService;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/customtasks")
public class CustomTaskController extends GenericIdNameAuditingEntityController<CustomTask, UUID, CustomTaskRepository, CustomTaskService> {

    public CustomTaskController(CustomTaskService service) {
        super(service);
    }

    @PostMapping("report-custom-task-wizard")
    public ResponseEntity<ByteArrayResource> reportCustomTaskWizard(@RequestParam Instant startDate, @RequestParam Instant endDate) throws Exception {
        return ResponseEntity.ok()
            .header("Content-Disposition", "attachment; filename=" + service.getEntityMetaData().getName() + ".xlsx")
            .contentType(MediaType.parseMediaType("application/vnd.ms-excel;charset=UTF-8"))
            .body(new ByteArrayResource(service.generateExcelCustomTaskWizardReportForUser(getCurrentUser(), startDate, endDate)));
    }

    @PostMapping("report")
    public ResponseEntity<ByteArrayResource> reportCustomTask(@RequestParam Instant startDate, @RequestParam Instant endDate) throws Exception {
        return ResponseEntity.ok()
            .header("Content-Disposition", "attachment; filename=" + service.getEntityMetaData().getName() + ".xlsx")
            .contentType(MediaType.parseMediaType("application/vnd.ms-excel;charset=UTF-8"))
            .body(new ByteArrayResource(service.generateExcelCustomTaskReport(getCurrentUser(), startDate, endDate)));
    }
}
