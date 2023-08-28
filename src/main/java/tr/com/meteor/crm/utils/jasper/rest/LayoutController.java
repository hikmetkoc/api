package tr.com.meteor.crm.utils.jasper.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tr.com.meteor.crm.domain.Layout;
import tr.com.meteor.crm.repository.LayoutRepository;
import tr.com.meteor.crm.service.LayoutService;

import java.util.UUID;

@RestController
@RequestMapping("/api/layouts")
public class LayoutController extends GenericIdNameAuditingEntityController<Layout, UUID, LayoutRepository, LayoutService> {

    public LayoutController(LayoutService service) {
        super(service);
    }
}
