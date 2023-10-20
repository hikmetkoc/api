package tr.com.meteor.crm.utils.jasper.rest;


import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tr.com.meteor.crm.domain.Activity;
import tr.com.meteor.crm.domain.Behavior;
import tr.com.meteor.crm.repository.ActivityRepository;
import tr.com.meteor.crm.repository.BehaviorRepository;
import tr.com.meteor.crm.service.ActivityService;
import tr.com.meteor.crm.service.BehaviorService;
import tr.com.meteor.crm.service.dto.CheckInOutDTO;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/behaviors")
public class BehaviorController extends GenericIdNameAuditingEntityController<Behavior, UUID, BehaviorRepository, BehaviorService> {

    public BehaviorController(BehaviorService service) {
        super(service);
    }
    @PostMapping("/report")
    public ResponseEntity report() throws Exception {
        return ResponseEntity.ok()
            .header("Content-Disposition", "attachment; filename=" + service.getEntityMetaData().getName() + ".xlsx")
            .contentType(MediaType.parseMediaType("application/vnd.ms-excel;charset=UTF-8"))
            .body(new ByteArrayResource(service.generateExcelBehaviorReportForUser(getCurrentUser())));
    }

    @PostMapping("selectedExcelReport")
    public ResponseEntity<ByteArrayResource> reportExcel(@RequestBody Map<String, List<UUID>> requestMap) throws Exception {
        List<UUID> ids = requestMap.get("ids");
        return ResponseEntity.ok()
            .header("Content-Disposition", "attachment; filename=" + service.getEntityMetaData().getName() + ".xlsx")
            .contentType(MediaType.parseMediaType("application/vnd.ms-excel;charset=UTF-8"))
            .body(new ByteArrayResource(service.generateSelectedExcelReport(ids, getCurrentUser())));
    }
}
