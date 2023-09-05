package tr.com.meteor.crm.utils.jasper.rest;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import tr.com.meteor.crm.domain.InvoiceList;
import tr.com.meteor.crm.repository.InvoiceListRepository;
import tr.com.meteor.crm.service.InvoiceListService;
import tr.com.meteor.crm.service.InvoiceListenService;
import tr.com.meteor.crm.utils.attributevalues.InvoiceStatus;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/invoice_lists")
public class InvoiceListController extends GenericIdNameAuditingEntityController<InvoiceList, UUID, InvoiceListRepository, InvoiceListService> {

    public InvoiceListController(InvoiceListService service, InvoiceListenService invoiceListenService) {
        super(service);
        this.invoiceListenService = invoiceListenService;
    }
    public final InvoiceListenService invoiceListenService;

    @PostMapping("report")
    public ResponseEntity<ByteArrayResource> reportOrder(@RequestParam Instant startDate, @RequestParam Instant endDate) throws Exception {
        return ResponseEntity.ok()
            .header("Content-Disposition", "attachment; filename=" + service.getEntityMetaData().getName() + ".xlsx")
            .contentType(MediaType.parseMediaType("application/vnd.ms-excel;charset=UTF-8"))
            .body(new ByteArrayResource(service.generateExcelOrderReport(getCurrentUser(), startDate, endDate)));
    }

    @GetMapping("/{invoiceNum}")
    public ResponseEntity<String> getEttntByInvoiceNum(@PathVariable String invoiceNum) {
        try {
            String base64 = service.getEttntByInvoiceNum(invoiceNum);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + invoiceNum + ".pdf");
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
            List<InvoiceStatus> invoiceStatuses = Arrays.asList(InvoiceStatus.values());
            for (InvoiceStatus invoiceStatus: invoiceStatuses) {
                if (invoiceStatus.getId().equals(status)) {
                    service.updateStatus(id, invoiceStatus.getAttributeValue(), description);
                }
            }
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            // Hata durumunda uygun bir hata yanıtı döndürebilirsiniz
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/soapservice")
    public ResponseEntity<?> runSoap() throws Exception {
        invoiceListenService.SoapService();
        return ResponseEntity.ok().build();
    }

}
