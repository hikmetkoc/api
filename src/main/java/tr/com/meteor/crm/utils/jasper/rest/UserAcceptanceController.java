package tr.com.meteor.crm.utils.jasper.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tr.com.meteor.crm.domain.Announcement;
import tr.com.meteor.crm.domain.UserAcceptance;
import tr.com.meteor.crm.repository.AnnouncementRepository;
import tr.com.meteor.crm.repository.UserAcceptanceRepository;
import tr.com.meteor.crm.service.AnnouncementService;
import tr.com.meteor.crm.service.UserAcceptanceService;

import java.util.UUID;

@RestController
@RequestMapping("/api/user_acceptances")
public class UserAcceptanceController extends GenericIdNameAuditingEntityController<UserAcceptance, UUID, UserAcceptanceRepository, UserAcceptanceService> {

    public UserAcceptanceController(UserAcceptanceService service) {
        super(service);
    }
}
