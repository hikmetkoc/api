package tr.com.meteor.crm.utils.jasper.rest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tr.com.meteor.crm.domain.FileContainer;
import tr.com.meteor.crm.repository.FileContainerRepository;
import tr.com.meteor.crm.service.FileContainerService;

import java.util.Base64;
import java.util.UUID;

@RestController
@RequestMapping("/api/file_containers")
public class FileContainerController extends GenericIdNameAuditingEntityController<FileContainer, UUID, FileContainerRepository, FileContainerService> {

    public FileContainerController(FileContainerService service) {
        super(service);
    }

    @PostMapping("/uploadFile")
    public String uploadBase64(@RequestParam String location, @RequestParam String locName, @RequestParam String name, @RequestParam String subject, @RequestBody byte[] binaryValue) throws Exception{
        try {
            String code = Base64.getEncoder().encodeToString(binaryValue);
            return service.uploadBase64File(location, locName, code, name, subject);
        } catch (IllegalArgumentException e) {
            return "Yükleme hatası!";
        } catch (Exception e) {
            return "Yükleme hatası!";
        }
    }

    @GetMapping("/showFile")
    public ResponseEntity<String> getCode(@RequestParam String location, @RequestParam String locName, @RequestParam String subject) {
        try {
            String base64 = service.getBase64FileCode(locName,location,subject);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=Document.pdf");
            return ResponseEntity.ok()
                .headers(headers)
                .body(base64);
        } catch (Exception e) {
            // Hata durumunda uygun bir hata yanıtı döndürebilirsiniz
            return ResponseEntity.badRequest().build();
        }
    }
}
