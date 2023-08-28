package tr.com.meteor.crm.utils.jasper.rest;

import org.json.JSONObject;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tr.com.meteor.crm.domain.Buy;
import tr.com.meteor.crm.domain.FuelLimit;
import tr.com.meteor.crm.repository.BuyRepository;
import tr.com.meteor.crm.repository.FuelLimitRepository;
import tr.com.meteor.crm.service.BuyService;
import tr.com.meteor.crm.service.FuelLimitService;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/fuellimits")
public class FuelLimitController extends GenericIdNameAuditingEntityController<FuelLimit, UUID, FuelLimitRepository, FuelLimitService> {

    public FuelLimitController(FuelLimitService service) {
        super(service);
    }

    @PutMapping("weekly")
    public List<FuelLimit> saveWeekly(@RequestBody List<FuelLimit> FuelLimits) throws Exception {
        return service.saveWeekly(FuelLimits);
    }

    @PostMapping("report")
    public ResponseEntity<ByteArrayResource> reportFuelLimit(@RequestParam Instant startDate, @RequestParam Instant endDate) throws Exception {
        return ResponseEntity.ok()
            .header("Content-Disposition", "attachment; filename=" + service.getEntityMetaData().getName() + ".xlsx")
            .contentType(MediaType.parseMediaType("application/vnd.ms-excel;charset=UTF-8"))
            .body(new ByteArrayResource(service.generateExcelFuelLimitReport(getCurrentUser(), startDate, endDate)));
    }

    /*@PostMapping("/makeApiRequest")
    public ResponseEntity<String> makeApiRequest(@RequestParam String curcode, @RequestParam BigDecimal fuelTl, @RequestParam String description, @RequestParam Instant startDate, @RequestParam Instant endDate) {
        ResponseEntity<String> apiResponse = service.makeApiRequest(curcode, fuelTl, description, startDate, endDate);

        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("status", apiResponse.getStatusCodeValue());
        jsonResponse.put("body", new JSONObject(apiResponse.getBody()));

        return ResponseEntity.ok(jsonResponse.toString());
    }*/

    /*@PostMapping("/makeApiRequest")
    public ResponseEntity<String> makeApiRequest(@RequestParam String curcode, @RequestParam Integer fuelTl, @RequestParam String description, @RequestParam String startDate, @RequestParam String endDate) {
        ResponseEntity<String> apiResponse = service.makeApiRequest(curcode, fuelTl, description, startDate, endDate);

        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("status", apiResponse.getStatusCodeValue());
        jsonResponse.put("body", new JSONObject(apiResponse.getBody()));

        return ResponseEntity.ok(jsonResponse.toString());
    }*/

    /*@PostMapping("/makeApiRequest")
    public ResponseEntity<String> makeApiRequest(@RequestBody FuelLimitRequest request) {
        // Burada gelen JSON verisini kullanarak işlemleri gerçekleştirin.
        // Örnek olarak:
        // service.processFuelLimitRequest(request);

        // Başarılı cevap döndürme örneği:
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("status", 200);
        jsonResponse.put("message", "Request processed successfully.");
        return ResponseEntity.ok(jsonResponse.toString());
    }*/
}
