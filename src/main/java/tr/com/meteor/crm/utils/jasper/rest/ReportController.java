package tr.com.meteor.crm.utils.jasper.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tr.com.meteor.crm.domain.Report;
import tr.com.meteor.crm.repository.ReportRepository;
import tr.com.meteor.crm.service.ReportService;

import java.util.UUID;

@RestController
@RequestMapping("/api/reports")
public class ReportController extends GenericIdNameAuditingEntityController<Report, UUID, ReportRepository, ReportService> {

    public ReportController(ReportService service) {
        super(service);
    }
}
