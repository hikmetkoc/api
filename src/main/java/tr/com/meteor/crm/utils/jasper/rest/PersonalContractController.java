package tr.com.meteor.crm.utils.jasper.rest;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tr.com.meteor.crm.domain.PersonalContract;
import tr.com.meteor.crm.domain.Task;
import tr.com.meteor.crm.repository.PersonalContractRepository;
import tr.com.meteor.crm.repository.TaskRepository;
import tr.com.meteor.crm.service.PersonalContractService;
import tr.com.meteor.crm.service.TaskService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/personal_contracts")
public class PersonalContractController extends GenericIdNameAuditingEntityController<PersonalContract, UUID, PersonalContractRepository, PersonalContractService> {

    public PersonalContractController(PersonalContractService service) {
        super(service);
    }

    @GetMapping("/download-degistirilmis-belge")
    public ResponseEntity<ByteArrayResource> download(@RequestParam String adsoyad) throws Exception {
        File file = service.ChangeWord(adsoyad);
        byte[] fileContent = Files.readAllBytes(file.toPath());
        ByteArrayResource resource = new ByteArrayResource(fileContent);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=degistirilmis_belge.docx");

        return ResponseEntity.ok()
            .headers(headers)
            .contentLength(file.length())
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(resource);
    }

}
