package tr.com.meteor.crm.service;

import org.apache.poi.xwpf.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.meteor.crm.domain.PersonalContract;
import tr.com.meteor.crm.domain.User;
import tr.com.meteor.crm.repository.PersonalContractRepository;
import tr.com.meteor.crm.repository.UserRepository;
import tr.com.meteor.crm.utils.attributevalues.FaturaSirketleri;
import tr.com.meteor.crm.utils.attributevalues.Sirketler;

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

            if (user.getSgksirket() != null) {
                if (user.getSgksirket().getId().equals(Sirketler.METEOR.getId())) {
                    sskSicil = "247300101104582907701-07/000";
                    sirket = "METEOR PETROL ÜRN.SAN. VE TİC.A.Ş.";
                    sirketAdres = "ISMETPASA MAH.ATATÜRK BULVARI N:156 Dıs kapı no:156 Iç kapı no:0 YALOVA/MERKEZ";
                } else if (user.getSgksirket().getId().equals(Sirketler.TERMINAL.getId())) {
                    sskSicil = "247300101108390807701-62/000";
                    sirket = "METEOR PETROL ÜRN.SAN. VE TİC.A.Ş.";
                    sirketAdres = "BAYRAKTEPE MAH.BURSA YOLU CAD. N:138 Dıs kapı no:138 Iç kapı no:0 YALOVA/MERKEZ";
                } else if (user.getSgksirket().getId().equals(Sirketler.GORUKLE.getId())) {
                    sskSicil = "441200101140791601612-13/000";
                    sirket = "METEOR PETROL ÜRN.SAN. VE TİC.A.Ş.";
                    sirketAdres = "DUMLUPINAR MAHISIMSIZ Dıs kapı no:4 Iç kapı no:A BURSA/NİLÜFER";
                } else if (user.getSgksirket().getId().equals(Sirketler.ISTANBUL.getId())) {
                    sskSicil = "247300808124933803432-40/000";
                    sirket = "METEOR PETROL ÜRN.SAN. VE TİC.A.Ş.";
                    sirketAdres = "ORHANLI MAHFETTAH BASARAN CAD. Dıs kapı no:85 Iç kapı no:A-1 İSTANBUL/TUZLA";
                } else if (user.getSgksirket().getId().equals(Sirketler.BURSA.getId())) {
                    sskSicil = "247300101123361601612-22/000";
                    sirket = "METEOR PETROL ÜRN.SAN. VE TİC.A.Ş.";
                    sirketAdres = "AHMET YESEVI MAH HÜRRIYET CADBALAT 16 EVLERI A BLOK Dıs kapı no:13 Iç kapı no:9 BURSA/NİLÜFER";
                } else if (user.getSgksirket().getId().equals(Sirketler.IGDIR.getId())) {
                    sskSicil = "228220101101616007601-41/000";
                    sirket = "METEOR PETROL ÜRN.SAN. VE TİC.A.Ş.";
                    sirketAdres = "ÇALPALA KÖYÜ ÇALPALA OSB MAH.1.SOK. Dıs kapı no:1 Iç kapı no: IGDIR/MERKEZ";
                } else if (user.getSgksirket().getId().equals(Sirketler.TEPE.getId())) {
                    sskSicil = "441200101144143601612-68/000";
                    sirket = "METEOR PETROL ÜRN.SAN. VE TİC.A.Ş.";
                    sirketAdres = "BALAT SELIMIYE H21B24C4B PAFTA 4177 ADA 6 PARSEL Dıs kapı no:24A Iç kapı no: BURSA/NİLÜFER\n";
                } else if (user.getSgksirket().getId().equals(Sirketler.IZMIR.getId())) {
                    sskSicil = "228220101182179903513-83/000";
                    sirket = "METEOR PETROL ÜRN.SAN. VE TİC.A.Ş.";
                    sirketAdres = "MALTEPE SB MAH. SEDİR SK. LÖHER ASANSÖR BLOK NO: 26 MENEMEN / İZMİR";
                } else if (user.getSgksirket().getId().equals(Sirketler.NCC.getId())) {
                    sskSicil = "247300101109906907701-91/000";
                    sirket = "NCC PETROL ÜRN.SAN. VE TİC.A.Ş.";
                    sirketAdres = "ISMETPASA MAH.ATATÜRK BULVARI N:156 Dıs kapı no:156 Iç kapı no:0 YALOVA/MERKEZ";
                } else if (user.getSgksirket().getId().equals(Sirketler.CEMCAN.getId())) {
                    sskSicil = "247300101127913101612-44/000";
                    sirket = "CEMCAN PETROL ÜRN.İNŞ.NAK.RENT A CAR A.Ş.";
                    sirketAdres = "FETHIYE MAH. SANAYI CAD. NO: 299 IÇ KAPI NO: 299 NİLÜFER/BURSA";
                } else if (user.getSgksirket().getId().equals(Sirketler.SIMYA.getId())) {
                    sskSicil = "256100101141911401612-56/000";
                    sirket = "SİMYA KAFE";
                    sirketAdres = "Balat Mah. Bozkır Sk. Meteor Satış Ofısı Blok No: 6 İç Kapı No: 1 NİLÜFER/BURSA";
                } else if (user.getSgksirket().getId().equals(Sirketler.BIRCE.getId())) {
                    sskSicil = "247300808120511703432-51/000";
                    sirket = "BİRCE PETROL İNŞAAT A.Ş.";
                    sirketAdres = "ORHANLI MAHFETTAH BASARAN CAD. Dıs kapı no:85 Iç kapı no:A-1 İSTANBUL/TUZLA";
                } else if (user.getSgksirket().getId().equals(Sirketler.MUDANYA.getId())) {
                    sskSicil = "247300101127638601614-15/000";
                    sirket = "MUDANYA PETROL ÜRN.İNŞ.SAN. VE TİC.LTD.ŞTİ.";
                    sirketAdres = "DAVUTDEDE KURTULUŞ No:149/B YILDIRIM/BURSA";
                } else if (user.getSgksirket().getId().equals(Sirketler.STAR.getId())) {
                    sskSicil = "246690202133645003421-46/000";
                    sirket = "STAR ŞARZ ENERJİ A.Ş.";
                    sirketAdres = "YESILCE MAH. DIKEN SK. NO: 2 IÇ KAPI NO: 5 İSTANBUL/KAĞITHANE";
                } else if (user.getSgksirket().getId().equals(Sirketler.CHARGE.getId())) {
                    sskSicil = "246690202133645003421-46/000";
                    sirket = "STAR CHARGE PRODUCTION A.Ş.";
                    sirketAdres = "YESILCE MAH. DIKEN SK. NO: 2 IÇ KAPI NO: 5 İSTANBUL/KAĞITHANE";
                } else if (user.getSgksirket().getId().equals(Sirketler.AVELICE.getId())) {
                    sskSicil = "228220101100921007601-75/000";
                    sirket = "AVELICE MAKİNA SANAYİ VE TİCARET ANONİM ŞİRKETİ";
                    sirketAdres = "ÇALPALA ORGANİZE SANAYİ BÖLGESİ. Dış Kapı No: 1 İç Kapı No:1 Merkez";
                } else {
                    sskSicil = "ŞİRKET HATALI!";
                }
            }
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
