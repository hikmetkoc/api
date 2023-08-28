package tr.com.meteor.crm.utils.jasper.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tr.com.meteor.crm.domain.Announcement;
import tr.com.meteor.crm.repository.AnnouncementRepository;
import tr.com.meteor.crm.service.AnnouncementService;

import java.util.UUID;

@RestController
@RequestMapping("/api/announcements")
public class AnnouncementController extends GenericIdNameAuditingEntityController<Announcement, UUID, AnnouncementRepository, AnnouncementService> {

    public AnnouncementController(AnnouncementService service) {
        super(service);
    }
}
