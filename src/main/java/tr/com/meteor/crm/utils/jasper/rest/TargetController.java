package tr.com.meteor.crm.utils.jasper.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tr.com.meteor.crm.domain.Target;
import tr.com.meteor.crm.repository.TargetRepository;
import tr.com.meteor.crm.service.TargetService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/targets")
public class TargetController extends GenericIdNameAuditingEntityController<Target, UUID, TargetRepository, TargetService> {

    public TargetController(TargetService service) {
        super(service);
    }

    @PostMapping("yearly")
    public List<Target> getTargetsYearly(int year) throws Exception {
        return service.getYearlyTargets(year);
    }

    @PutMapping("yearly")
    public ResponseEntity<List<Target>> saveTargetsYearly(@RequestBody List<Target> targets) throws Exception {
        return service.saveYearlyTargets(targets);
    }

    @PostMapping("report")
    public ResponseEntity report(@RequestParam int year) throws Exception {
        return service.report(getCurrentUser(), year);
    }
}
