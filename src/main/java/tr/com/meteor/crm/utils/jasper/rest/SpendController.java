package tr.com.meteor.crm.utils.jasper.rest;


import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tr.com.meteor.crm.domain.CustomActivity;
import tr.com.meteor.crm.domain.Spend;
import tr.com.meteor.crm.repository.CustomActivityRepository;
import tr.com.meteor.crm.repository.SpendRepository;
import tr.com.meteor.crm.service.CustomActivityService;
import tr.com.meteor.crm.service.SpendService;
import tr.com.meteor.crm.service.dto.CheckInOutDTO;
import tr.com.meteor.crm.utils.attributevalues.PaymentStatus;
import tr.com.meteor.crm.utils.attributevalues.SpendStatus;

import java.time.Instant;
import java.util.*;

@RestController
@RequestMapping("/api/spends")
public class SpendController extends GenericIdNameAuditingEntityController<Spend, UUID, SpendRepository, SpendService> {

    public SpendController(SpendService service) {
        super(service);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateSpendStatus(@PathVariable UUID id, @RequestParam String status, @RequestParam String description) throws Exception {
        try {
            List<SpendStatus> spendList = Arrays.asList(SpendStatus.values());
            for (SpendStatus spendStatus: spendList) {
                if (spendStatus.getId().equals(status)) {
                    service.updateSpendStatus(id, spendStatus.getAttributeValue(), description);
                }
            }
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            // Hata durumunda uygun bir hata yanıtı döndürebilirsiniz
            return ResponseEntity.badRequest().build();
        }
    }
    @PutMapping("/updatepaymentstatus/{id}")
    public ResponseEntity<String> updateSpendPaymentStatus(@PathVariable UUID id, @RequestParam String status) throws Exception {
        try {
            service.updateSpendPaymentStatus(id, status);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            // Hata durumunda uygun bir hata yanıtı döndürebilirsiniz
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("selectedExcelSpendReport")
    public ResponseEntity<ByteArrayResource> reportExcel(@RequestBody Map<String, List<UUID>> requestMap, @RequestParam String type, @RequestParam String qualification, @RequestParam String description) throws Exception {
        List<UUID> ids = requestMap.get("ids");
        return ResponseEntity.ok()
            .header("Content-Disposition", "attachment; filename=" + service.getEntityMetaData().getName() + ".xlsx")
            .contentType(MediaType.parseMediaType("application/vnd.ms-excel;charset=UTF-8"))
            .body(new ByteArrayResource(service.generateSelectedExcelSpendReport(ids, getCurrentUser(), type, qualification, description)));
    }

    @PostMapping("excelReport")
    public ResponseEntity<ByteArrayResource> excelReport() throws Exception {
        return ResponseEntity.ok()
            .header("Content-Disposition", "attachment; filename=" + service.getEntityMetaData().getName() + ".xlsx")
            .contentType(MediaType.parseMediaType("application/vnd.ms-excel;charset=UTF-8"))
            .body(new ByteArrayResource(service.excelReport(getCurrentUser())));
    }

    @PutMapping("/paytotl/{id}")
    public ResponseEntity<String> updateSpendPay(@PathVariable UUID id, @RequestParam String money) throws Exception {
        try {
            service.updateSpendPay(id, money);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            // Hata durumunda uygun bir hata yanıtı döndürebilirsiniz
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/uploadPDF")
    public ResponseEntity<ByteArrayResource> uploadBase64(@RequestParam String id, @RequestBody byte[] binaryValue) throws Exception{
        try {
            UUID uuid = UUID.fromString(id);
            String base64Data = Base64.getEncoder().encodeToString(binaryValue);
            service.uploadPDF(uuid, base64Data);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ByteArrayResource(new byte[0]));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ByteArrayResource(new byte[0]));
        }
    }

    @GetMapping("/toShowDekont/{id}")
    public ResponseEntity<String> getEttntByInvoiceNum(@PathVariable UUID id) {
        try {
            String base64 = service.getShowDekont(id);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + id.toString() + ".pdf");
            return ResponseEntity.ok()
                .headers(headers)
                .body(base64);
        } catch (Exception e) {
            // Hata durumunda uygun bir hata yanıtı döndürebilirsiniz
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/controltotalspends")
    public ResponseEntity<String> controlTotalSpends() throws Exception{
        try {
            return ResponseEntity.ok(service.controlTotal());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("HATALI İŞLEM");
        }
    }
}
