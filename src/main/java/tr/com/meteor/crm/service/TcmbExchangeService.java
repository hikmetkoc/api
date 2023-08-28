package tr.com.meteor.crm.service;

import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import tr.com.meteor.crm.domain.*;
import tr.com.meteor.crm.repository.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class TcmbExchangeService extends GenericIdNameAuditingEntityService<BuyLimit, UUID, BuyLimitRepository> {
    private final MailService mailService;

    public TcmbExchangeService(BaseUserService baseUserService, BaseRoleService baseRoleService,
                               BasePermissionService basePermissionService, BaseFileDescriptorService baseFileDescriptorService,
                               BaseConfigurationService baseConfigurationService, BuyLimitRepository repository,
                               MailService mailService) {
        super(baseUserService, baseRoleService, basePermissionService, baseFileDescriptorService, baseConfigurationService,
            BuyLimit.class, repository);
        this.mailService = mailService;
    }

    @Scheduled(cron = "0 0 2 * * *")
    public void TcmbService() throws Exception {
        try {
            System.out.println("TCMB Exchange Service Başlatılıyor, Kullanıcı Döviz Limitleri Güncellenecektir...");
            String urlString = "https://www.tcmb.gov.tr/kurlar/today.xml"; // XML dosyasının URL'sini belirtin
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            InputStream inputStream = connection.getInputStream();

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputStream);

            doc.getDocumentElement().normalize();

            NodeList currencyList = doc.getElementsByTagName("Currency");
            String forexBuying = "0";
            for (int i = 0; i < currencyList.getLength(); i++) {
                Element currencyElement = (Element) currencyList.item(i);
                String currencyCode = currencyElement.getAttribute("CurrencyCode");
                if ("USD".equals(currencyCode)) {
                    forexBuying = currencyElement.getElementsByTagName("BanknoteBuying").item(0).getTextContent();
                }
            }
            BigDecimal guncelkur = new BigDecimal(forexBuying);
            List<BuyLimit> buyLimits = repository.findAll();
            for (BuyLimit buyLimit : buyLimits) {
                buyLimit.setUserDl(buyLimit.getUserTl().divide(guncelkur, RoundingMode.HALF_UP));
                buyLimit.setChiefDl(buyLimit.getChiefTl().divide(guncelkur, RoundingMode.HALF_UP));
                buyLimit.setManagerDl(buyLimit.getManagerTl().divide(guncelkur, RoundingMode.HALF_UP));
                buyLimit.setDirectorDl(buyLimit.getDirectorTl().divide(guncelkur, RoundingMode.HALF_UP));
            }
            System.out.println("Efektif Alış Kuru: 1 USD = " + forexBuying + " TL , Limit güncellemeleri yapıldı.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
