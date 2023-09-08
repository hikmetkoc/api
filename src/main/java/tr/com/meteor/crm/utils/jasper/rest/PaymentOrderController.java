package tr.com.meteor.crm.utils.jasper.rest;

import com.sun.net.httpserver.HttpsParameters;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tr.com.meteor.crm.domain.*;
import tr.com.meteor.crm.repository.AttributeValueRepository;
import tr.com.meteor.crm.repository.BuyRepository;
import tr.com.meteor.crm.repository.PaymentOrderRepository;
import tr.com.meteor.crm.repository.SpendRepository;
import tr.com.meteor.crm.service.BuyService;
import tr.com.meteor.crm.service.PaymentOrderService;
import tr.com.meteor.crm.service.dto.CheckInOutDTO;
import tr.com.meteor.crm.service.dto.PaymentOrderDTO;
import tr.com.meteor.crm.service.dto.PaymentOrderFileDTO;
import tr.com.meteor.crm.utils.attributevalues.PaymentStatus;

import java.awt.*;
import java.time.Instant;
import java.util.*;
import java.util.List;

@RestController
@RequestMapping("/api/payment_orders")
public class PaymentOrderController extends GenericIdNameAuditingEntityController<PaymentOrder, UUID, PaymentOrderRepository, PaymentOrderService> {

    public static final AttributeValueRepository attributeValueRepository = null;
    public static final SpendRepository spendRepository = null;

    public PaymentOrderController(PaymentOrderService service) {
        super(service);
    }

    @PostMapping("report")
    public ResponseEntity<ByteArrayResource> reportOrder(@RequestParam Instant startDate, @RequestParam Instant endDate) throws Exception {
        return ResponseEntity.ok()
            .header("Content-Disposition", "attachment; filename=" + service.getEntityMetaData().getName() + ".xlsx")
            .contentType(MediaType.parseMediaType("application/vnd.ms-excel;charset=UTF-8"))
            .body(new ByteArrayResource(service.generateExcelOrderReport(getCurrentUser(), startDate, endDate)));
    }

    @GetMapping("/get-file-details")
    public ResponseEntity<?> getFileDetails(@RequestParam UUID fileId) {
        try {
            Optional<FileDescriptor> fileDescriptor = service.getFileDetails(fileId);
            if (fileDescriptor.isPresent()) {
                FileDescriptor file = fileDescriptor.get();
                return ResponseEntity.ok(file);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Dosya bilgileri bulunamadı.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Dosya bilgileri alınamadı.");
        }
    }
    /*@PostMapping("/uploadPDF")
    public ResponseEntity uploadBase64(@RequestParam String id, @RequestParam String base64String) throws Exception{
        try {
            UUID uuid = UUID.fromString(id);
            //System.out.println(uuid);
            service.updatePaymentOrder(uuid, base64String);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Geçersiz UUID formatı.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Veritabanına kaydedilirken bir hata oluştu.");
        }
    }*/
    @PostMapping("/uploadPDF")
    public ResponseEntity<ByteArrayResource> uploadBase64(@RequestParam String id, @RequestBody byte[] binaryValue) throws Exception{
        try {
            UUID uuid = UUID.fromString(id);
            String base64Data = Base64.getEncoder().encodeToString(binaryValue);
            service.updatePaymentOrder(uuid, base64Data);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ByteArrayResource(new byte[0]));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ByteArrayResource(new byte[0]));
        }
    }

    @PutMapping("/updatePayment")
    public void updateStatus(@RequestBody PaymentOrderDTO paymentOrderDTO) throws Exception {
        System.out.println("PUT ATTI");
        service.updateStatus(paymentOrderDTO);
        System.out.println("PUT ATTI");
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

    @PutMapping("/{id}")
    public ResponseEntity<String> updatePaymentOrderStatus(@PathVariable UUID id, @RequestParam String status, @RequestParam String description) throws Exception {
        try {
            System.out.println(description);
            List<PaymentStatus> paymentStatuses = Arrays.asList(PaymentStatus.values());
            for (PaymentStatus paymentStatus: paymentStatuses) {
                if (paymentStatus.getId().equals(status)) {
                    service.updatePaymentOrderStatus(id, paymentStatus.getAttributeValue(), description);
                }
            }
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            // Hata durumunda uygun bir hata yanıtı döndürebilirsiniz
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("selectedExcelReport")
    public ResponseEntity<ByteArrayResource> reportExcel(@RequestBody Map<String, List<UUID>> requestMap) throws Exception {
        List<UUID> ids = requestMap.get("ids");
        return ResponseEntity.ok()
            .header("Content-Disposition", "attachment; filename=" + service.getEntityMetaData().getName() + ".xlsx")
            .contentType(MediaType.parseMediaType("application/vnd.ms-excel;charset=UTF-8"))
            .body(new ByteArrayResource(service.generateSelectedExcelReport(ids, getCurrentUser())));
    }

    @PutMapping("changeStore")
    public ResponseEntity<String> changeStore(@RequestParam String id, @RequestParam String storeid) throws Exception {
        try {
            UUID changeid = UUID.fromString(id);
            UUID changestoreid = UUID.fromString(storeid);
            service.updateStoreId(changeid, changestoreid);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            // Hata durumunda uygun bir hata yanıtı döndürebilirsiniz
            return ResponseEntity.badRequest().build();
        }
    }
}
