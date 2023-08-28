package tr.com.meteor.crm.utils.jasper.rest;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tr.com.meteor.crm.domain.UserPermission;
import tr.com.meteor.crm.repository.UserPermissionRepository;
import tr.com.meteor.crm.service.UserPermissionService;

import java.util.UUID;

@RestController
@RequestMapping("/api/user_permissions")
public class UserPermissionController extends GenericIdNameAuditingEntityController<UserPermission, UUID, UserPermissionRepository, UserPermissionService> {

    public UserPermissionController(UserPermissionService service) {
        super(service);
    }

    @GetMapping("/controlholiday/{id}")
    public Boolean controlHoliday(@PathVariable Long id) throws Exception {
        try {
            /*boolean result = service.controlHoliday(id);
            return String.valueOf(result);*/
            return service.controlHoliday(id);
        } catch (Exception e) {
            System.out.println("HATA");
            return false;
        }
    }
}
