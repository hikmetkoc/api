package tr.com.meteor.crm.utils.jasper.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tr.com.meteor.crm.domain.Permission;
import tr.com.meteor.crm.repository.PermissionRepository;
import tr.com.meteor.crm.service.PermissionService;

import java.util.UUID;

@RestController
@RequestMapping("/api/permissions")
public class PermissionController extends GenericIdNameEntityController<Permission, UUID, PermissionRepository, PermissionService> {

    public PermissionController(PermissionService service) {
        super(service);
    }
}
