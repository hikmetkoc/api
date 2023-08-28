package tr.com.meteor.crm.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tr.com.meteor.crm.domain.*;
import tr.com.meteor.crm.service.dto.CrmNotification;
import tr.com.meteor.crm.utils.configuration.Configurations;

@Service
public class MobileNotificationService {

    private static final Logger log = LoggerFactory.getLogger(MobileNotificationService.class);
    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.registerModule(new JavaTimeModule());
    }

    private final BaseConfigurationService baseConfigurationService;
    private final BaseUserService baseUserService;

    public MobileNotificationService(BaseConfigurationService baseConfigurationService, BaseUserService baseUserService) {
        this.baseConfigurationService = baseConfigurationService;
        this.baseUserService = baseUserService;
    }

    @Async
    public void sendAnnouncementNotification(Announcement announcement) throws Exception {
        if (announcement == null) return;

        CrmNotification crmNotification = new CrmNotification();
        crmNotification.setType(CrmNotification.Type.ANNOUNCEMENT);
        crmNotification.setTitle("Duyuru");
        crmNotification.setBody("Yeni bir duyuru yayınlandı. Görüntülemek için tıklayın.");
        crmNotification.setEntityName(Announcement.class.getSimpleName());
        crmNotification.setEntityId(announcement.getId().toString());
        crmNotification.setTo("/topics/all");

        sendNotification(crmNotification);
    }

    @Async
    public void sendNotification(CrmNotification crmNotification) throws Exception {
        Configuration firebaseConfiguration = baseConfigurationService.getConfigurationById(Configurations.FIREBASE_AUTHORIZATION_KEY.getId());

        if (firebaseConfiguration == null || StringUtils.isBlank(firebaseConfiguration.getStoredValue())) {
            log.error("Firebase Authorization key bulunamadı.");
            return;
        }
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Authorization", firebaseConfiguration.getStoredValue());
            httpHeaders.setContentType(MediaType.parseMediaType("application/json"));
            HttpEntity<String> httpEntity = new HttpEntity<>(objectMapper.writeValueAsString(crmNotification), httpHeaders);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> responseEntity = restTemplate.exchange(
                "https://fcm.googleapis.com/fcm/send", HttpMethod.POST, httpEntity, String.class);

    }

    @Async
    public void sendTaskNotification(Task task) throws Exception {
        if (task == null || task.getOwner() == null) return;

        /*User owner = baseUserService.getUserFullFetched(task.getOwner().getId()).get();
        if (owner.getFcmTokens() == null || owner.getFcmTokens().isEmpty()) return;*/

        CrmNotification crmNotification = new CrmNotification();
        crmNotification.setType(CrmNotification.Type.TASK_ASSIGNED);
        crmNotification.setTitle("Görev");

        if (task.getAssigner() != null) {
            crmNotification.setBody(task.getAssigner().getFullName() + " tarafından size bir görev atandı. Görüntülemek için tıklayın.");
            crmNotification.addData("assigner", task.getAssigner().getFullName());
        } else {
            crmNotification.setBody("Size bir görev atandı. Görüntülemek için tıklayın.");
        }

        crmNotification.setEntityName(Task.class.getSimpleName());
        crmNotification.setEntityId(task.getId().toString());

        /*for (String token : owner.getFcmTokens()) {
            crmNotification.setTo(token);
            sendNotification(crmNotification);
        }*/
    }
}
