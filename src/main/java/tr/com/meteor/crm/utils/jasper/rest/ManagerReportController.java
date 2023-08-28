package tr.com.meteor.crm.utils.jasper.rest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tr.com.meteor.crm.service.ManagerReportService;
import tr.com.meteor.crm.service.dto.Table;

import java.util.List;

@RestController
@RequestMapping("/api/manager-report")
public class ManagerReportController {

    private final ManagerReportService service;

    public ManagerReportController(ManagerReportService service) {
        this.service = service;
    }

    @PostMapping
    public List<Table> reports() throws Exception {
        return service.post();
    }
}
