package tr.com.meteor.crm.utils.jasper.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tr.com.meteor.crm.domain.ApprovalUserLimit;
import tr.com.meteor.crm.repository.ApprovalUserLimitRepository;
import tr.com.meteor.crm.service.ApprovalUserLimitService;
import tr.com.meteor.crm.service.TcmbExchangeService;

import java.text.Normalizer;
import java.util.Locale;
import java.util.UUID;

@RestController
@RequestMapping("/api/approval_user_limits")
public class ApprovalUserLimitController extends GenericIdNameAuditingEntityController<ApprovalUserLimit, UUID, ApprovalUserLimitRepository, ApprovalUserLimitService> {

    private final TcmbExchangeService tcmbExchangeService;
    public ApprovalUserLimitController(ApprovalUserLimitService service, TcmbExchangeService tcmbExchangeService) {
        super(service);
        this.tcmbExchangeService = tcmbExchangeService;
    }

    @PostMapping("/tcmbservice")
    public ResponseEntity<?> runTcmb() throws Exception {
        tcmbExchangeService.TcmbService();
        return ResponseEntity.ok().build();
    }
}
