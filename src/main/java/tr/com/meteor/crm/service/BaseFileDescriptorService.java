package tr.com.meteor.crm.service;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tr.com.meteor.crm.domain.IdEntity;
import tr.com.meteor.crm.utils.attributevalues.FileDescriptorType;
import tr.com.meteor.crm.domain.FileDescriptor;
import tr.com.meteor.crm.repository.FileDescriptorRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class BaseFileDescriptorService {

    private final FileDescriptorRepository repository;
    private final FileStorageService fileStorageService;

    public BaseFileDescriptorService(FileDescriptorRepository fileDescriptorRepository, FileStorageService fileStorageService) {
        this.repository = fileDescriptorRepository;
        this.fileStorageService = fileStorageService;
    }

    public FileDescriptor uploadFile(MultipartFile file, String entityId, String description, Class<? extends IdEntity> entityClass) throws Exception {
        FileDescriptor fileDescriptor = new FileDescriptor();

        fileDescriptor.setType(FileDescriptorType.FILE.getAttributeValue());
        fileDescriptor.setContentType(file.getContentType());
        fileDescriptor.setFileSize(file.getSize());
        fileDescriptor.setFileName(file.getOriginalFilename());
        fileDescriptor.setDescription(description);
        fileDescriptor.setEntityId(entityId);
        fileDescriptor.setEntityName(entityClass == null ? "" : entityClass.getSimpleName());

        repository.save(fileDescriptor);

        fileStorageService.storeFile(file, fileDescriptor);

        return fileDescriptor;
    }

    public FileDescriptor uploadFile(MultipartFile file, String entityId, String description) throws Exception {
        return uploadFile(file, entityId, description, null);
    }

    public Optional<FileDescriptor> getFileDescriptorById(UUID id) {
        return repository.findById(id);
    }

    public Optional<FileDescriptor> getFileDescriptorViewById(UUID id) {
        return repository.findById(id);
    }

    public ResponseEntity<Resource> download(FileDescriptor fileDescriptor) throws Exception {
        Resource resource = fileStorageService.loadFileAsResource(fileDescriptor.getId().toString());
        String contentType = fileDescriptor.getContentType();

        if (contentType == null || contentType.isEmpty()) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(contentType))
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDescriptor.getFileName() + "\"")
            .body(resource);
    }

    public void delete(FileDescriptor fileDescriptor) throws Exception {
        fileStorageService.deleteFile(fileDescriptor.getId().toString());
        repository.deleteSoft(fileDescriptor);
    }

    public <TIdType> List<FileDescriptor> getFileDescriptorsByEntityId(TIdType entityId) {
        return repository.findAllByEntityId(entityId.toString());
    }
}
