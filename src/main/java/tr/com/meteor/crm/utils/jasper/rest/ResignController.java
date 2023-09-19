package tr.com.meteor.crm.utils.jasper.rest;


import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tr.com.meteor.crm.domain.Ikfile;
import tr.com.meteor.crm.domain.Resign;
import tr.com.meteor.crm.repository.IkfileRepository;
import tr.com.meteor.crm.repository.ResignRepository;
import tr.com.meteor.crm.service.IkfileService;
import tr.com.meteor.crm.service.ResignService;
import tr.com.meteor.crm.service.dto.CheckInOutDTO;
import tr.com.meteor.crm.service.dto.QuoteSendDocumentDTO;
import tr.com.meteor.crm.utils.attributevalues.ResignStatus;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/resigns")
public class ResignController extends GenericIdNameAuditingEntityController<Resign, UUID, ResignRepository, ResignService> {

    public ResignController(ResignService service) {
        super(service);
    }

    @PutMapping("/saveAnket")
    public ResponseEntity<String> getEttntByInvoiceNum(@RequestBody Resign resign, @RequestParam String sorumluluk,
                                                       @RequestParam String calismaSaat, @RequestParam String calismaOrtam,
                                                       @RequestParam String odeme, @RequestParam String takdir, @RequestParam String gelistirme,
                                                       @RequestParam String iliski, @RequestParam String kariyer, @RequestParam String iletisim) {
        try {
            service.saveAnket(resign, sorumluluk, calismaSaat, calismaOrtam, odeme, takdir, gelistirme, iliski, kariyer, iletisim);
        } catch (Exception e) {
            // Hata durumunda uygun bir hata yanıtı döndürebilirsiniz
            return ResponseEntity.badRequest().build();
        }
        return null;
    }
}
