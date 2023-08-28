package tr.com.meteor.crm.utils.jasper.rest;


import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tr.com.meteor.crm.domain.HolUser;
import tr.com.meteor.crm.domain.VocationDay;
import tr.com.meteor.crm.repository.HolUserRepository;
import tr.com.meteor.crm.repository.VocationDayRepository;
import tr.com.meteor.crm.service.HolUserService;
import tr.com.meteor.crm.service.VocationDayService;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/api/vocationdays")
public class VocationDayController extends GenericIdNameAuditingEntityController<VocationDay, UUID, VocationDayRepository, VocationDayService> {

    public VocationDayController(VocationDayService service) {
        super(service);
    }
    @PostMapping("/report")
    public ResponseEntity report(@RequestParam Instant startDate, @RequestParam Instant endDate) throws Exception {
        return ResponseEntity.ok()
            .header("Content-Disposition", "attachment; filename=" + service.getEntityMetaData().getName() + ".xlsx")
            .contentType(MediaType.parseMediaType("application/vnd.ms-excel;charset=UTF-8"))
            .body(new ByteArrayResource(service.generateExcelVocationDayReport(getCurrentUser(), startDate, endDate)));
    }
}
