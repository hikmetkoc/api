package tr.com.meteor.crm.utils.jasper.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tr.com.meteor.crm.domain.Role;
import tr.com.meteor.crm.repository.RoleRepository;
import tr.com.meteor.crm.service.RoleService;

@RestController
@RequestMapping("/api/roles")
public class RoleController extends GenericIdEntityController<Role, String, RoleRepository, RoleService> {

    public RoleController(RoleService service) {
        super(service);
    }
}
