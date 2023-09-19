package tr.com.meteor.crm.utils.jasper.rest;


import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tr.com.meteor.crm.domain.Holiday;
import tr.com.meteor.crm.repository.HolidayRepository;
import tr.com.meteor.crm.service.HolidayService;
import tr.com.meteor.crm.service.dto.PaymentOrderDTO;

import java.time.Instant;
import java.util.Base64;
import java.util.UUID;

@RestController
@RequestMapping("/api/holidays")
public class HolidayController extends GenericIdNameAuditingEntityController<Holiday, UUID, HolidayRepository, HolidayService> {

    public HolidayController(HolidayService service) {
        super(service);
    }

    @PostMapping("/report")
    public ResponseEntity report(@RequestParam String year, @RequestParam String month) throws Exception {
        return ResponseEntity.ok()
            .header("Content-Disposition", "attachment; filename=" + service.getEntityMetaData().getName() + ".xlsx")
            .contentType(MediaType.parseMediaType("application/vnd.ms-excel;charset=UTF-8"))
            .body(new ByteArrayResource(service.generateExcelHolidayReport(getCurrentUser(), year, month)));
    }

    @PostMapping("/sendnotificationmail")
    public ResponseEntity sendMail(@RequestParam String receiver, @RequestParam String subject, @RequestParam String message) throws Exception{
        service.notificationMail(receiver, subject, message);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/uploadPDF")
    public ResponseEntity<ByteArrayResource> uploadBase64(@RequestParam String id, @RequestBody byte[] binaryValue) throws Exception{
        try {
            UUID uuid = UUID.fromString(id);
            String base64Data = Base64.getEncoder().encodeToString(binaryValue);
            service.uploadHoliday(uuid, base64Data);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ByteArrayResource(new byte[0]));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ByteArrayResource(new byte[0]));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getEttntByInvoiceNum(@PathVariable UUID id) {
        try {
            String base64 = service.getEttntById(id);
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
}
