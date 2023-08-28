package tr.com.meteor.crm.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Node;
import tr.com.meteor.crm.domain.AttributeValue;
import tr.com.meteor.crm.domain.Customer;
import tr.com.meteor.crm.domain.InvoiceList;
import tr.com.meteor.crm.domain.SapSoap;
import tr.com.meteor.crm.repository.AttributeValueRepository;
import tr.com.meteor.crm.repository.CustomerRepository;
import tr.com.meteor.crm.repository.InvoiceListRepository;

import java.io.*;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import tr.com.meteor.crm.repository.SapSoapRepository;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

@Service
@Transactional(rollbackFor = Exception.class)
public class InvoiceListenService extends GenericIdNameAuditingEntityService<InvoiceList, UUID, InvoiceListRepository> {
    private final MailService mailService;

    private final CustomerRepository customerRepository;

    private final AttributeValueRepository attributeValueRepository;

    private final InvoiceListRepository invoiceListRepository;

    private final SapSoapRepository sapSoapRepository;

    public InvoiceListenService(BaseUserService baseUserService, BaseRoleService baseRoleService,
                                BasePermissionService basePermissionService, BaseFileDescriptorService baseFileDescriptorService,
                                BaseConfigurationService baseConfigurationService, InvoiceListRepository repository,
                                MailService mailService, CustomerRepository customerRepository, AttributeValueRepository attributeValueRepository, InvoiceListRepository invoiceListRepository, SapSoapRepository sapSoapRepository) {
        super(baseUserService, baseRoleService, basePermissionService, baseFileDescriptorService, baseConfigurationService,
            InvoiceList.class, repository);
        this.mailService = mailService;
        this.customerRepository = customerRepository;
        this.attributeValueRepository = attributeValueRepository;
        this.invoiceListRepository = invoiceListRepository;
        this.sapSoapRepository = sapSoapRepository;
    }

    //@Scheduled(cron = "59 * * * * *")
    public void SoapService() throws Exception {
        String DBSirket ="";
        for (int a=1; a<=8; a++) {
            switch (a) {
                case 1:
                    DBSirket = "NCC2021";
                    break;
                case 2:
                    DBSirket = "Meteor2021";
                    break;
                case 3:
                    DBSirket = "Cemcan2021";
                    break;
                case 4:
                    DBSirket = "BircePetrol2021";
                    break;
                case 5:
                    DBSirket = "Mudanya2021";
                    break;
                case 6:
                    DBSirket = "SIMYA23";
                    break;
                case 7:
                    DBSirket = "Star2021";
                    break;
                case 8:
                    DBSirket = "StarCharge2021";
                    break;
            }

            System.out.println("FATURA LİSTESİ SERVİSİ BAŞLATILDI. " + DBSirket + " veritabanından faturalar çekiliyor...");

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            Instant ensontarih = Instant.parse("2000-01-01T00:00:00.000Z");
            List<InvoiceList> invoiceLists = invoiceListRepository.findAll();
            for (InvoiceList invoiceList : invoiceLists) {
                if (ensontarih.compareTo(invoiceList.getSendDate())<0) {
                    ensontarih = invoiceList.getSendDate();
                }
            }
            ZonedDateTime strbaslangic = ensontarih.atZone(ZoneId.systemDefault());
            ZonedDateTime strbitis = Instant.now().atZone(ZoneId.systemDefault());
            String gonderimbaslangic = formatter.format(strbaslangic);
            String gonderimbitis = formatter.format(strbitis);
            String soapEndpointUrl = "http://176.235.80.130:39802/LarinTransfer.asmx";
            String soapRequest = "<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" " +
                "xmlns:tem=\"http://tempuri.org/\"><soap:Header/><soap:Body><tem:OzkanFaturaDetay>" +
                "<tem:MuhatapKodu>12345</tem:MuhatapKodu><tem:BaslangıcTarihi>" + gonderimbaslangic + "</tem:BaslangıcTarihi>" +
                "<tem:BitisTarihi>" + gonderimbitis + "</tem:BitisTarihi><tem:Ccompany>" + DBSirket + "</tem:Ccompany></tem:OzkanFaturaDetay></soap:Body></soap:Envelope>";

            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                // SOAP isteği gönder
                URL url = new URL(soapEndpointUrl);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/soap+xml; charset=utf-8");
                connection.setDoOutput(true);

                OutputStream outputStream = connection.getOutputStream();
                outputStream.write(soapRequest.getBytes(StandardCharsets.UTF_8));
                outputStream.close();

                // SOAP yanıtını al
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
                } else {
                    reader = new BufferedReader(new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8));
                }

                StringBuilder responseBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    responseBuilder.append(line);
                }
                String soapResponse = responseBuilder.toString();

                // SOAP yanıtını XML olarak ayrıştır
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document document = builder.parse(new ByteArrayInputStream(soapResponse.getBytes(StandardCharsets.UTF_8)));
                document.getDocumentElement().normalize();

                List<SapSoap> sapSoapList = printTagNames(document.getDocumentElement(), "", DBSirket);

                for (SapSoap sapSoap : sapSoapList) {
                    if (isCustomerExists(sapSoap.getVkn())) {
                        System.out.println(sapSoap.getCardname() + " cari kaydı mevcuttur.");
                    } else {
                        addCustomerRecord(sapSoap.getVkn(), sapSoap.getCardname(), sapSoap.getWeb(), sapSoap.getTelefon(), sapSoap.getMail(), sapSoap.getDaire());
                    }

                    if (isInvoiceExists(sapSoap.getFaturano())) {
                        System.out.println(sapSoap.getFaturano() + " numaralı faturanın kaydı sistemde bulunmaktadır.");
                    } else {
                        addInvoiceRecord(sapSoap.getGonderimtarihi(), sapSoap.getFaturano(), sapSoap.getVkn(), sapSoap.getToplamtutar(), sapSoap.getFaturatarihi(), sapSoap.getParabirimi(), sapSoap.getCardname(), DBSirket);
                    }
                }

            } finally {
                // Bağlantıyı ve reader'ı kapat
                if (reader != null) {
                    reader.close();
                }
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }
    }
    private List<SapSoap> printTagNames(Element element, String indent, String dbname) throws Exception {
        NodeList childNodes = element.getElementsByTagName("BE1_OPCHBusinessCards");
        List<SapSoap> sapSoapList = new ArrayList<>();

        for (int i = 0; i < childNodes.getLength(); i++) {
            Node node = childNodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element childElement = (Element) node;
                String tagName = childElement.getTagName();

                SapSoap sapSoap = new SapSoap();
                sapSoap.setDbname(dbname);
                sapSoap.setSira(getTagContent(childElement, "Sıra"));
                sapSoap.setCardcode(getTagContent(childElement, "CardCode"));
                sapSoap.setCardname(getTagContent(childElement, "CardName"));
                if (getTagContent(childElement, "Vkn").length() == 10) {
                    sapSoap.setVkn("0" + getTagContent(childElement, "Vkn"));
                } else {
                    sapSoap.setVkn(getTagContent(childElement, "Vkn"));
                }
                sapSoap.setFaturano(getTagContent(childElement, "FaturaNo"));
                sapSoap.setFaturatarihi(getTagContent(childElement, "FaturaTarihi"));
                sapSoap.setGonderimtarihi(getTagContent(childElement, "GonderimTarihi"));
                sapSoap.setToplamtutar(getTagContent(childElement, "ToplamTutar"));
                sapSoap.setParabirimi(getTagContent(childElement, "ParaBirimi"));
                sapSoap.setAciklama(getTagContent(childElement, "FaturaAciklama"));
                sapSoap.setWeb(getTagContent(childElement, "Web"));
                sapSoap.setTelefon(getTagContent(childElement, "Telefon"));
                sapSoap.setMail(getTagContent(childElement, "Mail"));
                sapSoap.setAdres(getTagContent(childElement, "Adres"));
                sapSoap.setDaire(getTagContent(childElement, "VargiDairesi"));
                sapSoap.setMilce(getTagContent(childElement, "MIlce"));
                sapSoap.setMsehir(getTagContent(childElement, "MSehir"));
                sapSoap.setMulke(getTagContent(childElement, "MUlke"));
                sapSoap.setFil(getTagContent(childElement, "FIL"));
                sapSoap.setFilce(getTagContent(childElement, "FILCE"));
                sapSoap.setFdurum(getTagContent(childElement, "FDurum"));
                sapSoap.setFsenaryo(getTagContent(childElement, "FSenaryo"));
                sapSoap.setFtipi(getTagContent(childElement, "FTipi"));
                sapSoap.setFpdf(getTagContent(childElement, "PDFs"));
                sapSoap.setEttn(getTagContent(childElement, "ETTN"));

                sapSoapList.add(sapSoap);
                if (!sapSoapRepository.existsByFaturano(sapSoap.getFaturano())) {
                    sapSoapRepository.save(sapSoap);
                }

                printTagNames(childElement, indent + "  ", dbname);
            }
        }

        return sapSoapList;
    }

    private String getTagContent(Element element, String tagName) {
        NodeList nodes = element.getElementsByTagName(tagName);
        if (nodes.getLength() > 0) {
            return nodes.item(0).getTextContent().trim();
        }
        return null;
    }
    private boolean isCustomerExists(String taxNumber) {
        // Customer tablosunda kontrolü gerçekleştir
        // Eşleşme durumunda true, aksi halde false dön
        // İlgili iş mantığını buraya ekleyin
        return customerRepository.existsByTaxNumber(taxNumber);
    }

    private void addCustomerRecord(String taxNumber, String commercialTitle, String website, String phone, String email, String taxOffice) {
        // Customer tablosuna yeni bir veri kaydı yap
        // İlgili iş mantığını buraya ekleyin
        Customer customer = new Customer();
        customer.setTaxNumber(taxNumber);
        if (!website.isEmpty()) {
            customer.setWebsite(website);
        }
        if (!phone.isEmpty()) {
            customer.setPhone(phone);
        }
        if (!email.isEmpty()) {
            customer.setEmail(email);
        }
        if (!commercialTitle.isEmpty()) {
            customer.setCommercialTitle(commercialTitle);
            customer.setName(commercialTitle);
        }
        customer.setTaxOffice(taxOffice);
        customerRepository.save(customer);
        System.out.println(taxNumber + " Vkn li yeni cari kaydı eklenmiştir.");
    }

    private boolean isInvoiceExists(String invoiceNum) {
        // InvoiceList tablosunda kontrolü gerçekleştir
        // Eşleşme durumunda true, aksi halde false dön
        // İlgili iş mantığını buraya ekleyin
        return invoiceListRepository.existsByInvoiceNum(invoiceNum);
    }

    private void addInvoiceRecord(String sendDate, String invoiceNum, String customer, String amount, String invoiceDate, String parabirimi, String sirket, String dbsirket) {
        // InvoiceList tablosuna yeni bir veri kaydı yap
        // İlgili iş mantığını buraya ekleyin
        List<AttributeValue> attributeValues = attributeValueRepository.findAll();

        InvoiceList invoiceList = new InvoiceList();
        invoiceList.setInvoiceNum(invoiceNum);

        BigDecimal bigDecimal = BigDecimal.ZERO;
        if (!amount.isEmpty()) {
            bigDecimal = new BigDecimal(amount);
            invoiceList.setAmount(bigDecimal);
        }

        if (parabirimi.equals("TRY")) {
            invoiceList.setMoneyType(getAttributeValueById(attributeValues, "Par_Bir_Tl"));
        } else {
            invoiceList.setMoneyType(getAttributeValueById(attributeValues, "Par_Bir_Dl"));
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        if (!invoiceDate.isEmpty()) {
            try {
                LocalDate localDate = LocalDate.parse(invoiceDate, formatter);
                Instant instant = localDate.atStartOfDay().toInstant(ZoneOffset.UTC);
                invoiceList.setInvoiceDate(instant);
            } catch (DateTimeParseException e) {
                // Hata durumunda yapılacak işlemler
            }
        }

        if (!sendDate.isEmpty()) {
            try {
                LocalDate localDate = LocalDate.parse(sendDate, formatter);
                Instant instant = localDate.atStartOfDay().toInstant(ZoneOffset.UTC);
                invoiceList.setSendDate(instant);
            } catch (DateTimeParseException e) {
                // Hata durumunda yapılacak işlemler
            }
        }

        switch (dbsirket) {
            case "NCC2021" :
                invoiceList.setSirket(getAttributeValueById(attributeValues, "Fatura_Sirketleri_Ncc"));
                break;
            case "Meteor2021" :
                invoiceList.setSirket(getAttributeValueById(attributeValues, "Fatura_Sirketleri_Meteor"));
                break;
            case "Cemcan2021" :
                invoiceList.setSirket(getAttributeValueById(attributeValues, "Fatura_Sirketleri_Cemcan"));
                break;
            case "BircePetrol2021" :
                invoiceList.setSirket(getAttributeValueById(attributeValues, "Fatura_Sirketleri_Birce"));
                break;
            case "Mudanya2021" :
                invoiceList.setSirket(getAttributeValueById(attributeValues, "Fatura_Sirketleri_Mudanya"));
                break;
            case "SIMYA23" :
                invoiceList.setSirket(getAttributeValueById(attributeValues, "Fatura_Sirketleri_Simya"));
                break;
            case "Star2021" :
                invoiceList.setSirket(getAttributeValueById(attributeValues, "Fatura_Sirketleri_Star"));
                break;
            case "StarCharge2021" :
                invoiceList.setSirket(getAttributeValueById(attributeValues, "Fatura_Sirketleri_Charge"));
                break;
        }
        invoiceList.setInvoiceStatus(getAttributeValueById(attributeValues, "Fatura_Durumlari_Sahipsiz"));

        List<Customer> customers = customerRepository.findByTaxNumber(customer);
        invoiceList.setCustomer(customers.get(0));


        invoiceListRepository.save(invoiceList);
        /*List<InvoiceList> invoiceList1 = invoiceListRepository.findByCreatedById(baseUserService.getUserFullFetched(1L).get().getId());

        for (InvoiceList invoiceList2 : invoiceList1) {
            invoiceList2.setCreatedBy(baseUserService.getUserFullFetched(2L).get());
        }
        invoiceListRepository.saveAll(invoiceList1);*/
        System.out.println(invoiceNum + " numaralı fatura, Fatura Listesine eklenmiştir.");
    }

    private AttributeValue getAttributeValueById(List<AttributeValue> attributeValues, String id) {
        for (AttributeValue attributeValue : attributeValues) {
            if (attributeValue.getId().equals(id)) {
                return attributeValue;
            }
        }
        return null;
    }
}
