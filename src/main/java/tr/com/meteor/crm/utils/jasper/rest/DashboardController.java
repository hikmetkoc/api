package tr.com.meteor.crm.utils.jasper.rest;

import org.springframework.web.bind.annotation.*;
import tr.com.meteor.crm.security.SecurityUtils;
import tr.com.meteor.crm.service.DashboardService;
import tr.com.meteor.crm.service.dto.DashboardSearchDTO;
import tr.com.meteor.crm.service.dto.QuickActivityDTO;

import java.util.UUID;

@RestController
@RequestMapping("/api/dashboards")
public class DashboardController {

    private final DashboardService service;

    public DashboardController(DashboardService service) {
        this.service = service;
    }

    @PostMapping("/search")
    public DashboardSearchDTO search(@RequestParam String search) throws Exception {
        return service.searchAll(search);
    }

    /*@PutMapping("/quick-activity")
    public QuickActivityDTO quickActivity(@RequestBody QuickActivityDTO quickActivityDTO) throws Exception {
        return service.quickActivity(quickActivityDTO);
    }
*/
    @PutMapping("/sendInfoDocument")
    public void sendInfoDocument(@RequestBody QuickActivityDTO quickActivityDTO) throws Exception {
        service.sendInfoDocument(quickActivityDTO, service.getCurrentUser());
    }

    @PutMapping("/sendContractDocument")
    public void sendContractDocument(@RequestBody QuickActivityDTO quickActivityDTO) throws Exception {
        service.sendContractDocument(quickActivityDTO, SecurityUtils.getCurrentUserId().get());
    }

    @PostMapping
    public Object dashboard(@RequestParam(required = false) Long userId, @RequestParam(required = true) Integer year) throws Exception {
        return service.dashboard(userId, year);
    }

    /*@PostMapping("customer-sales-report")
    public Object salesReport(@RequestParam(required = false) UUID customerId, @RequestParam(required = false) Integer year) throws Exception {
        return service.salesReport(customerId, year);
    }*/
}
