package tr.com.meteor.crm.utils.jasper.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tr.com.meteor.crm.domain.FileDescriptor;
import tr.com.meteor.crm.repository.FileDescriptorRepository;
import tr.com.meteor.crm.service.FileDescriptorService;

import java.util.UUID;

@RestController
@RequestMapping("/api/file-descriptors")
public class FileDescriptorController extends GenericIdNameEntityController<FileDescriptor, UUID, FileDescriptorRepository, FileDescriptorService> {

    public FileDescriptorController(FileDescriptorService service) {
        super(service);
    }
}
