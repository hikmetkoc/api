package tr.com.meteor.crm.utils.jasper.rest;


import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tr.com.meteor.crm.domain.Activity;
import tr.com.meteor.crm.domain.SapSoap;
import tr.com.meteor.crm.repository.ActivityRepository;
import tr.com.meteor.crm.repository.SapSoapRepository;
import tr.com.meteor.crm.service.ActivityService;
import tr.com.meteor.crm.service.SapSoapService;
import tr.com.meteor.crm.service.dto.CheckInOutDTO;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/api/sapsoaps")
public class SapSoapController extends GenericIdNameAuditingEntityController<SapSoap, UUID, SapSoapRepository, SapSoapService> {

    public SapSoapController(SapSoapService service) {
        super(service);
    }
}
