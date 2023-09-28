package tr.com.meteor.crm.service;

import org.apache.poi.xwpf.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.meteor.crm.domain.PersonalContract;
import tr.com.meteor.crm.domain.User;
import tr.com.meteor.crm.repository.PersonalContractRepository;
import tr.com.meteor.crm.repository.UserRepository;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Locale;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class PersonalContractService extends GenericIdNameAuditingEntityService<PersonalContract, UUID, PersonalContractRepository> {
    private final MailService mailService;

    private final UserRepository userRepository;

    public PersonalContractService(BaseUserService baseUserService, BaseRoleService baseRoleService,
                                   BasePermissionService basePermissionService, BaseFileDescriptorService baseFileDescriptorService,
                                   BaseConfigurationService baseConfigurationService, PersonalContractRepository repository,
                                   MailService mailService, UserRepository userRepository) {
        super(baseUserService, baseRoleService, basePermissionService, baseFileDescriptorService, baseConfigurationService,
            PersonalContract.class, repository);
        this.mailService = mailService;
        this.userRepository = userRepository;
    }

    public File ChangeWord(User user, String sozlesme) throws Exception {

        String base64 = repository.findBySozlesme(sozlesme).getBelge();
        try {
            // Base64 verisini byte dizisine çevirin
            byte[] decodedBytes = Base64.getDecoder().decode(base64);

            // Byte dizisini bir .docx dosyasına yazın
            FileOutputStream fos = new FileOutputStream("input.docx");
            fos.write(decodedBytes);
            fos.close();

            // .docx dosyasını işleyebilirsiniz
            FileInputStream fis = new FileInputStream("input.docx");
            XWPFDocument document = new XWPFDocument(fis);
            // ... belgeyi kullanarak yapmak istediğiniz işlemleri gerçekleştirin
            Instant sgkStartDate = user.getSgkStartDate();
            Instant endDate = user.getEndDate();
            Instant birthDate = user.getBirthDate();
            Instant bugun = Instant.now();
            String adsoyad = "";
            String sgkUnvan = "";
            String adres = "";
            String acilAdSoyad = "";
            String acilYakinlik = "";
            String acilTel = "";
            String dogumYer = "";
            String tck = "";
            String ogrenim = "";
            String tel = "";
            String sirket = "";
            String sskSicil = "ŞİRKET SİCİL NUMARASI";
            String sirketAdres = "ŞİRKET ADRESİ";
            String myb = "YOK";
            String yonetici = getCurrentUser().getFullName();
            String sgkNo = ""; // todo:SORULACAK
            String birim = "";
            String yoneticiUnvan = "";
            String yoneticiTel = "";
            String yoneticiAdres = "";

            if(endDate == null) {
                endDate = Instant.now();
            }
            LocalDate sgkStartDate1 = sgkStartDate.atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate endDate1 = endDate.atZone(ZoneId.systemDefault()).toLocalDate();

            // Period sınıfını kullanarak iki tarih arasındaki farkı hesaplayın
            Period period = Period.between(sgkStartDate1, endDate1);

            // Sonucu ekrana yazdırın
            int years = period.getYears();
            int months = period.getMonths();
            int days = period.getDays();

            String sure = years + " yıl, " + months + " ay, " + days + " gün";
            if (user.getFirstName() != null && user.getLastName() != null) {
                adsoyad = user.getFirstName() + " " + user.getLastName();
            }
            if (user.getSgkunvan() != null) {
                sgkUnvan = user.getSgkunvan().getLabel();
            }
            if (user.getBirim() != null) {
                birim = getCurrentUser().getBirim().getLabel();
            }
            if (getCurrentUser().getSgkunvan() != null) {
                yoneticiUnvan = getCurrentUser().getSgkunvan().getLabel();
            }
            if (getCurrentUser().getPhone2() != null) {
                yoneticiTel = getCurrentUser().getPhone2();
            }
            if (getCurrentUser().getAdres() != null) {
                yoneticiAdres = getCurrentUser().getAdres();
            }
            if (user.getAdres() != null) {
                adres = user.getAdres();
            }
            if (user.getAciladsoyad() != null) {
                acilAdSoyad = user.getAciladsoyad();
            }
            if (user.getAcilyakinlik() != null) {
                acilYakinlik = user.getAcilyakinlik();
            }
            if (user.getAcilno() != null) {
                acilTel = user.getAcilno();
            }
            if (user.getCity() != null && user.getDistrict() != null) {
                dogumYer = user.getCity() + " / " + user.getDistrict();
            }
            if (user.getTck() != null) {
                tck = user.getTck();
            }
            if (user.getEgitim() != null) {
                ogrenim = user.getEgitim().getLabel();
            }
            if (user.getPhone2() != null) {
                tel = user.getPhone2();
            }
            if (user.getSgksirket() != null) {
                sirket = user.getSgksirket().getLabel();
            }
            if (user.getMyb().equals(true)) {
                myb = "VAR";
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                .withLocale(new Locale("tr", "TR")) // Türkçe ve Türkiye bölge ayarını kullanın.
                .withZone(ZoneId.of("Europe/Istanbul")); // Türkiye'nin zaman dilimini kullanın.

            for (XWPFParagraph paragraph : document.getParagraphs()) {
                for (XWPFRun run : paragraph.getRuns()) {
                    String text = run.getText(0);
                    if (text != null && text.contains("&ADSOYAD&")) {
                        text = text.replace("&ADSOYAD&", adsoyad);
                        run.setText(text, 0);
                    }
                    if (text != null && text.contains("&UNVAN&")) {
                        text = text.replace("&UNVAN&", sgkUnvan);
                        run.setText(text, 0);
                    }
                    if (text != null && text.contains("&BIRIM&")) {
                        text = text.replace("&BIRIM&", birim);
                        run.setText(text, 0);
                    }
                    if (text != null && text.contains("&ADRES&")) {
                        text = text.replace("&ADRES&", adres);
                        run.setText(text, 0);
                    }
                    if (text != null && text.contains("&YONETICI&")) {
                        text = text.replace("&YONETICI&", yonetici);
                        run.setText(text, 0);
                    }
                    if (text != null && text.contains("&YONETICITEL&")) {
                        text = text.replace("&YONETICITEL&", yoneticiTel);
                        run.setText(text, 0);
                    }
                    if (text != null && text.contains("&YONETICIUNVAN&")) {
                        text = text.replace("&YONETICIUNVAN&", yoneticiUnvan);
                        run.setText(text, 0);
                    }
                    if (text != null && text.contains("&YONETICIADRES&")) {
                        text = text.replace("&YONETICIADRES&", yoneticiAdres);
                        run.setText(text, 0);
                    }
                    if (text != null && text.contains("&ACILADSOYAD&")) {
                        text = text.replace("&ACILADSOYAD&", acilAdSoyad);
                        run.setText(text, 0);
                    }
                    if (text != null && text.contains("&ACILYAKINLIK&")) {
                        text = text.replace("&ACILYAKINLIK&", acilYakinlik);
                        run.setText(text, 0);
                    }
                    if (text != null && text.contains("&ACILTEL&")) {
                        text = text.replace("&ACILTEL&", acilTel);
                        run.setText(text, 0);
                    }
                    if (text != null && text.contains("&ISBASLANGIC&")) {
                        text = text.replace("&ISBASLANGIC&", formatter.format(sgkStartDate));
                        run.setText(text, 0);
                    }
                    if (text != null && text.contains("&ISBITIS&")) {
                        text = text.replace("&ISBITIS&", formatter.format(endDate));
                        run.setText(text, 0);
                    }
                    if (text != null && text.contains("&SURE&")) {
                        text = text.replace("&SURE&", sure);
                        run.setText(text, 0);
                    }
                    if (text != null && text.contains("&SGKNO&")) {
                        text = text.replace("&SGKNO&", sgkNo);
                        run.setText(text, 0);
                    }
                    if (text != null && text.contains("&DOGUM&")) {
                        text = text.replace("&DOGUM&", formatter.format(birthDate));
                        run.setText(text, 0);
                    }
                    if (text != null && text.contains("&DOGUMYER&")) {
                        text = text.replace("&DOGUMYER&", dogumYer);
                        run.setText(text, 0);
                    }
                    if (text != null && text.contains("&TC&")) {
                        text = text.replace("&TC&", tck);
                        run.setText(text, 0);
                    }
                    if (text != null && text.contains("&OGRENIM&")) {
                        text = text.replace("&OGRENIM&", ogrenim);
                        run.setText(text, 0);
                    }
                    if (text != null && text.contains("&MAAS&")) {
                        text = text.replace("&MAAS&", "111111111111111111111");
                        run.setText(text, 0);
                    }
                    if (text != null && text.contains("&TEL&")) {
                        text = text.replace("&TEL&", tel);
                        run.setText(text, 0);
                    }
                    if (text != null && text.contains("&MYB&")) {
                        text = text.replace("&MYB&", myb);
                        run.setText(text, 0);
                    }
                    if (text != null && text.contains("&SIRKET&")) {
                        text = text.replace("&SIRKET&", sirket);
                        run.setText(text, 0);
                    }
                    if (text != null && text.contains("&SSKSICIL&")) {
                        text = text.replace("&SSKSICIL&", sskSicil);
                        run.setText(text, 0);
                    }
                    if (text != null && text.contains("&SIRKETADRES&")) {
                        text = text.replace("&SIRKETADRES&", sirketAdres);
                        run.setText(text, 0);
                    }
                    if (text != null && text.contains("&BUGUN&")) {
                        text = text.replace("&BUGUN&", formatter.format(bugun));
                        run.setText(text, 0);
                    }
                }
            }

            if (document.getTables().size() >= 1) {
                XWPFTable table = document.getTables().get(0);
                if (sozlesme.equals("isSozlesme")) {
                    table = document.getTables().get(1);
                }
                if (sozlesme.equals("fesihBildirimi")) {
                    table = document.getTables().get(0);
                }

                for (XWPFTableRow row : table.getRows()) {
                    for (XWPFTableCell cell : row.getTableCells()) {
                        for (XWPFParagraph paragraph : cell.getParagraphs()) {
                            for (XWPFRun run : paragraph.getRuns()) {
                                String text = run.getText(0);
                                if (text != null && text.contains("&ADSOYAD&")) {
                                    text = text.replace("&ADSOYAD&", adsoyad);
                                    run.setText(text, 0);
                                }
                                if (text != null && text.contains("&YONETICI&")) {
                                    text = text.replace("&YONETICI&", yonetici);
                                    run.setText(text, 0);
                                }
                                if (text != null && text.contains("&UNVAN&")) {
                                    text = text.replace("&UNVAN&", sgkUnvan);
                                    run.setText(text, 0);
                                }
                                if (text != null && text.contains("&ADRES&")) {
                                    text = text.replace("&ADRES&", adres);
                                    run.setText(text, 0);
                                }
                                if (text != null && text.contains("&ACILADSOYAD&")) {
                                    text = text.replace("&ACILADSOYAD&", acilAdSoyad);
                                    run.setText(text, 0);
                                }
                                if (text != null && text.contains("&ACILYAKINLIK&")) {
                                    text = text.replace("&ACILYAKINLIK&", acilYakinlik);
                                    run.setText(text, 0);
                                }
                                if (text != null && text.contains("&ACILTEL&")) {
                                    text = text.replace("&ACILTEL&", acilTel);
                                    run.setText(text, 0);
                                }
                                if (text != null && text.contains("&ISBASLANGIC&")) {
                                    text = text.replace("&ISBASLANGIC&", formatter.format(sgkStartDate));
                                    run.setText(text, 0);
                                }
                                if (text != null && text.contains("&DOGUM&")) {
                                    text = text.replace("&DOGUM&", formatter.format(birthDate));
                                    run.setText(text, 0);
                                }
                                if (text != null && text.contains("&DOGUMYER&")) {
                                    text = text.replace("&DOGUMYER&", dogumYer);
                                    run.setText(text, 0);
                                }
                                if (text != null && text.contains("&TC&")) {
                                    text = text.replace("&TC&", tck);
                                    run.setText(text, 0);
                                }
                                if (text != null && text.contains("&OGRENIM&")) {
                                    text = text.replace("&OGRENIM&", ogrenim);
                                    run.setText(text, 0);
                                }
                                if (text != null && text.contains("&MAAS&")) {
                                    text = text.replace("&MAAS&", "0.000");
                                    run.setText(text, 0);
                                }
                                if (text != null && text.contains("&TEL&")) {
                                    text = text.replace("&TEL&", tel);
                                    run.setText(text, 0);
                                }
                                if (text != null && text.contains("&MYB&")) {
                                    text = text.replace("&MYB&", myb);
                                    run.setText(text, 0);
                                }
                                if (text != null && text.contains("&SIRKET&")) {
                                    text = text.replace("&SIRKET&", sirket);
                                    run.setText(text, 0);
                                }
                                if (text != null && text.contains("&SSKSICIL&")) {
                                    text = text.replace("&SSKSICIL&", sskSicil);
                                    run.setText(text, 0);
                                }
                                if (text != null && text.contains("&SIRKETADRES&")) {
                                    text = text.replace("&SIRKETADRES&", sirketAdres);
                                    run.setText(text, 0);
                                }
                                if (text != null && text.contains("&BUGUN&")) {
                                    text = text.replace("&BUGUN&", formatter.format(bugun));
                                    run.setText(text, 0);
                                }
                            }
                        }
                    }
                }
            }

            FileOutputStream fos2 = new FileOutputStream("degistirilmis_belge.docx");
            document.write(fos2);
            File changedFile = new File("degistirilmis_belge.docx");
            fis.close();
            fos.close();
            fos2.close();
            return changedFile;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
