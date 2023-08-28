package tr.com.meteor.crm.utils.jasper.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tr.com.meteor.crm.domain.Quote;
import tr.com.meteor.crm.repository.QuoteRepository;
import tr.com.meteor.crm.security.RolesConstants;
import tr.com.meteor.crm.service.QuoteService;
import tr.com.meteor.crm.service.dto.QuoteSendDocumentDTO;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/quotes")
public class QuoteController extends GenericIdNameAuditingEntityController<Quote, UUID, QuoteRepository, QuoteService> {

    public QuoteController(QuoteService service) {
        super(service);
    }

    @PostMapping("documents")
    public Map<String, String> getDocumentList() {
        return service.getDocumentList();
    }

    @PutMapping("documents")
    public void sendDocuments(@RequestBody QuoteSendDocumentDTO dto) throws Exception {
        service.sendDocuments(dto, getCurrentUser());
    }

   /* @PutMapping("create-contract")
    public Contract createContract(@RequestParam UUID quoteId) throws Exception {
        return service.createContract(quoteId);
    }
*/
    /*@PutMapping("update-approval-status")
    public void updateApprovalStatus(@RequestParam UUID quoteId, @RequestParam boolean isApproved) throws Exception {
        service.updateApprovalStatus(quoteId, isApproved);
    }*/
    @GetMapping("/latest-quote-id")
   public ResponseEntity<UUID> getLatestQuoteId() {
       Optional<Quote> latestQuote = service.findLatestQuote();
       if (latestQuote.isPresent()) {
           UUID latestQuoteId = latestQuote.get().getId();
           return ResponseEntity.ok(latestQuoteId);
       } else {
           return ResponseEntity.notFound().build();
       }
   }
    @PostMapping("/report")
    public ResponseEntity report(@RequestParam Instant startDate, @RequestParam Instant endDate) throws Exception {
        return ResponseEntity.ok()
            .header("Content-Disposition", "attachment; filename=" + service.getEntityMetaData().getName() + ".xlsx")
            .contentType(MediaType.parseMediaType("application/vnd.ms-excel;charset=UTF-8"))
            .body(new ByteArrayResource(service.generateExcelQuoteReportForUser(getCurrentUser(), startDate, endDate)));
    }
}
