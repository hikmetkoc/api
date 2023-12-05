package tr.com.meteor.crm.utils.jasper.rest;


import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tr.com.meteor.crm.domain.Behavior;
import tr.com.meteor.crm.domain.RiskAnalysis;
import tr.com.meteor.crm.repository.BehaviorRepository;
import tr.com.meteor.crm.repository.RiskAnalysisRepository;
import tr.com.meteor.crm.service.BehaviorService;
import tr.com.meteor.crm.service.RiskAnalysisService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/risk_analysies")
public class RiskAnalysisController extends GenericIdNameAuditingEntityController<RiskAnalysis, UUID, RiskAnalysisRepository, RiskAnalysisService> {

    public RiskAnalysisController(RiskAnalysisService service) {
        super(service);
    }
}
