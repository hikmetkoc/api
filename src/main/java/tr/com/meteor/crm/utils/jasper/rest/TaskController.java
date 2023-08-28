package tr.com.meteor.crm.utils.jasper.rest;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tr.com.meteor.crm.domain.PaymentOrder;
import tr.com.meteor.crm.domain.Task;
import tr.com.meteor.crm.repository.TaskRepository;
import tr.com.meteor.crm.service.TaskService;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tasks")
public class TaskController extends GenericIdNameAuditingEntityController<Task, UUID, TaskRepository, TaskService> {

    public TaskController(TaskService service) {
        super(service);
    }

    @PutMapping("weekly")
    public List<Task> saveWeekly(@RequestBody List<Task> tasks) throws Exception {
        return service.saveWeekly(tasks);
    }

    @PostMapping("report-task-wizard")
    public ResponseEntity<ByteArrayResource> reportTaskWizard(@RequestParam Instant startDate, @RequestParam Instant endDate) throws Exception {
        return ResponseEntity.ok()
            .header("Content-Disposition", "attachment; filename=" + service.getEntityMetaData().getName() + ".xlsx")
            .contentType(MediaType.parseMediaType("application/vnd.ms-excel;charset=UTF-8"))
            .body(new ByteArrayResource(service.generateExcelTaskWizardReportForUser(getCurrentUser(), startDate, endDate)));
    }

    @PostMapping("report")
    public ResponseEntity<ByteArrayResource> reportTask(@RequestParam Instant startDate, @RequestParam Instant endDate) throws Exception {
        return ResponseEntity.ok()
            .header("Content-Disposition", "attachment; filename=" + service.getEntityMetaData().getName() + ".xlsx")
            .contentType(MediaType.parseMediaType("application/vnd.ms-excel;charset=UTF-8"))
            .body(new ByteArrayResource(service.generateExcelTaskReport(getCurrentUser(), startDate, endDate)));
    }

    @PostMapping("reportBT")
    public ResponseEntity<ByteArrayResource> reportTaskBT(@RequestParam Instant startDate, @RequestParam Instant endDate) throws Exception {
        return ResponseEntity.ok()
            .header("Content-Disposition", "attachment; filename=" + service.getEntityMetaData().getName() + ".xlsx")
            .contentType(MediaType.parseMediaType("application/vnd.ms-excel;charset=UTF-8"))
            .body(new ByteArrayResource(service.generateExcelTaskBTReport(getCurrentUser(), startDate, endDate)));
    }

    @PostMapping("/sendnotificationmail")
    public ResponseEntity sendMail(@RequestParam String receiver, @RequestParam String subject, @RequestParam String message) throws Exception{
        service.notificationMail(receiver, subject, message);
        return ResponseEntity.ok().build();
    }
}
