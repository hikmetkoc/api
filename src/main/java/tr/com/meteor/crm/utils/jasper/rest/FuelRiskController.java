package tr.com.meteor.crm.utils.jasper.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tr.com.meteor.crm.domain.FuelRisk;
import tr.com.meteor.crm.repository.FuelRiskRepository;
import tr.com.meteor.crm.service.FuelRiskService;

import java.util.UUID;

@RestController
@RequestMapping("/api/fuel_risks")
public class FuelRiskController extends GenericIdNameAuditingEntityController<FuelRisk, UUID, FuelRiskRepository, FuelRiskService> {

    public FuelRiskController(FuelRiskService service) {
        super(service);
    }

    @PutMapping("/addRisk")
    public ResponseEntity<String> addRisk(@RequestBody FuelRisk fuelRisk, @RequestParam String limitId) throws Exception {
        try {
            UUID id = UUID.fromString(limitId);
            String response = service.newPerson(fuelRisk, id);
            return ResponseEntity.ok(response);
        } catch (NullPointerException exception) {
            // Hata durumunda uygun bir hata yanıtı döndürün
            return ResponseEntity.badRequest().body("Eksik veya hatalı veri girişi yapıldı!");
        }
    }
}
