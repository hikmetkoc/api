package tr.com.meteor.crm.utils.jasper.rest;


import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tr.com.meteor.crm.domain.Activity;
import tr.com.meteor.crm.domain.CustomActivity;
import tr.com.meteor.crm.repository.ActivityRepository;
import tr.com.meteor.crm.repository.CustomActivityRepository;
import tr.com.meteor.crm.service.ActivityService;
import tr.com.meteor.crm.service.CustomActivityService;
import tr.com.meteor.crm.service.dto.CheckInOutDTO;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/api/customactivities")
public class CustomActivityController extends GenericIdNameAuditingEntityController<CustomActivity, UUID, CustomActivityRepository, CustomActivityService> {

    public CustomActivityController(CustomActivityService service) {
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

    @PostMapping("/report")
    public ResponseEntity report(@RequestParam Instant startDate, @RequestParam Instant endDate) throws Exception {
        return ResponseEntity.ok()
            .header("Content-Disposition", "attachment; filename=" + service.getEntityMetaData().getName() + ".xlsx")
            .contentType(MediaType.parseMediaType("application/vnd.ms-excel;charset=UTF-8"))
            .body(new ByteArrayResource(service.generateExcelActivityReportForUser(getCurrentUser(), startDate, endDate)));
    }
}
