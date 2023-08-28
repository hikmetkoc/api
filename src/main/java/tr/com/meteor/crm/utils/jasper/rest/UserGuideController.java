package tr.com.meteor.crm.utils.jasper.rest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tr.com.meteor.crm.domain.Configuration;
import tr.com.meteor.crm.service.BaseConfigurationService;
import tr.com.meteor.crm.utils.configuration.Configurations;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/user-guides")
public class UserGuideController {
    private final BaseConfigurationService baseConfigurationService;

    public UserGuideController(BaseConfigurationService baseConfigurationService) {
        this.baseConfigurationService = baseConfigurationService;
    }

    @PostMapping("file-list")
    public List<String> getFileName() throws Exception {
        List<String> fileNames = new ArrayList<>();
        for (File file : Objects.requireNonNull(getUserGuidePath().listFiles())) {
            if(file.isFile())
            fileNames.add(file.getName());
        }
        return fileNames;
    }

    @PostMapping("file-download")
    public ResponseEntity<Resource> download(String fileName) throws Exception {
        File userGuideFolder = getUserGuidePath();
        File file = new File(userGuideFolder.getAbsolutePath() + "\\" + fileName + "");

        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName + "");

        InputStreamResource inputStreamResource = new InputStreamResource(new FileInputStream(file));

        return ResponseEntity.ok()
            .headers(header)
            .contentLength(file.length())
            .contentType(MediaType.parseMediaType("application/octet-stream"))
            .body(inputStreamResource);
    }

    private File getUserGuidePath() throws Exception {
        Configuration userGuideConfiguration = baseConfigurationService.getConfigurationById(Configurations.KULLANMA_KILAVUZU_YOLU.getId());

        if (userGuideConfiguration == null) {
            throw new Exception("Kullanma Kılavuzu Yolu Konfigürasyonu Bulunamadı");
        }

        if (StringUtils.isBlank(userGuideConfiguration.getStoredValue())) {
            throw new Exception("Kullanım Kılavuzu Yolu Konfigürasyonu Ayarlanmamış");
        }

        File userGuideFolder = new File(userGuideConfiguration.getStoredValue());

        if (!userGuideFolder.exists() || !userGuideFolder.isDirectory()) {
            throw new Exception("Kullanım Kılavuzu Yolu Yanlış");
        }

        return userGuideFolder;
    }
}

