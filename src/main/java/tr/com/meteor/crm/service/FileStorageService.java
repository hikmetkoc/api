package tr.com.meteor.crm.service;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tr.com.meteor.crm.config.FileStorageConfiguration;
import tr.com.meteor.crm.domain.FileDescriptor;
import tr.com.meteor.crm.repository.FileDescriptorRepository;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class FileStorageService {

    private final FileStorageConfiguration fileStorageConfiguration;

    private final FileDescriptorRepository fileDescriptorRepository;
    private final Path fileStoragePath;

    public FileStorageService(FileStorageConfiguration fileStorageConfiguration, FileDescriptorRepository fileDescriptorRepository) throws Exception {
        this.fileStorageConfiguration = fileStorageConfiguration;

        this.fileStoragePath = Paths.get(fileStorageConfiguration.getUploadDir())
            .toAbsolutePath().normalize();
        this.fileDescriptorRepository = fileDescriptorRepository;

        try {
            Files.createDirectories(this.fileStoragePath);
        } catch (Exception ex) {
            throw new Exception("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public FileDescriptor getFileDescriptorById(String fileId) throws Exception {
        // Dosya bilgilerini almak için gerekli işlemleri gerçekleştirin
        FileDescriptor fileDescriptor = (FileDescriptor) fileDescriptorRepository.findAllByEntityId(fileId);
        if (fileDescriptor != null) {
            return fileDescriptor;
        } else {
            // Dosya bulunamadı durumunda uygun bir hata işleme yöntemi uygulayın
            throw new Exception("File not found with ID: " + fileId);
        }
    }
    public String storeFile(MultipartFile file, FileDescriptor fileDescriptor) throws Exception {
        String fileName = fileDescriptor.getId().toString();

        try {
            if (fileName.contains("..")) {
                throw new Exception("Sorry! Filename contains invalid path sequence " + fileName);
            }

            Path targetLocation = this.fileStoragePath.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException ex) {
            throw new Exception("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public Resource loadFileAsResource(String fileName) throws Exception {
        try {
            Path filePath = this.fileStoragePath.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new Exception("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new Exception("File not found " + fileName, ex);
        }
    }

    public void deleteFile(String fileName) throws Exception {
        try {
            Path filePath = this.fileStoragePath.resolve(fileName).normalize();
            File file = filePath.toFile();
            if (file.exists()) {
                file.delete();
            } else {
                throw new Exception("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new Exception("File not found " + fileName, ex);
        }
    }
}
