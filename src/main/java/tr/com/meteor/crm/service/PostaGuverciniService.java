package tr.com.meteor.crm.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import sun.net.www.http.HttpClient;
import tr.com.meteor.crm.domain.User;
import tr.com.meteor.crm.repository.UserRepository;

@Service
@Transactional(rollbackFor = Exception.class)
public class PostaGuverciniService extends GenericIdNameAuditingEntityService<User, Long, UserRepository> {
    private final MailService mailService;

    public PostaGuverciniService(BaseUserService baseUserService, BaseRoleService baseRoleService,
                                 BasePermissionService basePermissionService, BaseFileDescriptorService baseFileDescriptorService,
                                 BaseConfigurationService baseConfigurationService, UserRepository repository,
                                 MailService mailService) {
        super(baseUserService, baseRoleService, basePermissionService, baseFileDescriptorService, baseConfigurationService,
            User.class, repository);
        this.mailService = mailService;
    }

    //@Scheduled(cron = "0 0 2 * * *")
    public void SendSmsService(String gsm, String text) throws Exception {
        // API URL ve diğer parametreleri ayarlayın
        String apiUrl = "http://www.postaguvercini.com/api_http/sendsms.asp";
        String user = "meteorpetrol";
        String password = "meteorpetrol1";
        //String gsm = "5442458391";
        //String text = "DENEME";

        RestTemplate restTemplate = new RestTemplate();

        // HTTP isteği için başlık ayarlarını yapın
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // POST verilerini hazırlayın
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("user", user);
        map.add("password", password);
        map.add("gsm", gsm);
        map.add("text", text);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        // HTTP POST isteğini gönderin
        ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, request, String.class);

        // Yanıtı işleyin
        if (response.getStatusCode().is2xxSuccessful()) {
            String responseBody = response.getBody();
            System.out.println("POSTA GUVERCINI SERVICE Yanıtı: " + responseBody);
        } else {
            System.err.println("POSTA GUVERCINI SERVICE Yanıtı alınamadı. HTTP Kodu: " + response.getStatusCodeValue());
            throw new Exception("HATA VAR !");
        }
        //String queryCredit = "http://www.postaguvercini.com/api_http/querycredit.asp?user=XXXX&password=XXXX";
    }
}
