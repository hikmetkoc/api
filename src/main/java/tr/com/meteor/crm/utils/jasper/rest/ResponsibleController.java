package tr.com.meteor.crm.utils.jasper.rest;


import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tr.com.meteor.crm.domain.Activity;
import tr.com.meteor.crm.domain.Responsible;
import tr.com.meteor.crm.repository.ActivityRepository;
import tr.com.meteor.crm.repository.ResponsibleRepository;
import tr.com.meteor.crm.service.ActivityService;
import tr.com.meteor.crm.service.ResponsibleService;
import tr.com.meteor.crm.service.dto.CheckInOutDTO;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/api/responsibles")
public class ResponsibleController extends GenericIdNameAuditingEntityController<Responsible, UUID, ResponsibleRepository, ResponsibleService> {

    public ResponsibleController(ResponsibleService service) {
        super(service);
    }
}
