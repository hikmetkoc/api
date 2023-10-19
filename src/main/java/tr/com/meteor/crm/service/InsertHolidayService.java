package tr.com.meteor.crm.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.meteor.crm.domain.Activity;
import tr.com.meteor.crm.domain.HolUser;
import tr.com.meteor.crm.domain.Role;
import tr.com.meteor.crm.domain.User;
import tr.com.meteor.crm.repository.ActivityRepository;
import tr.com.meteor.crm.repository.ContractRepository;
import tr.com.meteor.crm.repository.HolUserRepository;
import tr.com.meteor.crm.repository.QuoteRepository;
import tr.com.meteor.crm.service.dto.CheckInOutDTO;
import tr.com.meteor.crm.utils.filter.Filter;
import tr.com.meteor.crm.utils.filter.FilterItem;
import tr.com.meteor.crm.utils.jasper.rest.errors.BadRequestAlertException;
import tr.com.meteor.crm.utils.jasper.rest.errors.RecordNotFoundException;
import tr.com.meteor.crm.utils.operations.Operations;
import tr.com.meteor.crm.utils.request.Request;

import java.io.ByteArrayOutputStream;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class InsertHolidayService extends GenericIdNameAuditingEntityService<HolUser, UUID, HolUserRepository> {
    private final MailService mailService;

    public InsertHolidayService(BaseUserService baseUserService, BaseRoleService baseRoleService,
                                BasePermissionService basePermissionService, BaseFileDescriptorService baseFileDescriptorService,
                                BaseConfigurationService baseConfigurationService, HolUserRepository repository,
                                MailService mailService) {
        super(baseUserService, baseRoleService, basePermissionService, baseFileDescriptorService, baseConfigurationService,
            HolUser.class, repository);
        this.mailService = mailService;
    }
    @Scheduled(cron = "0 0 1 * * *")
    public void sendFridayTargetReport() throws Exception {
        System.out.println("İZİN EKLEME SERVİSİ BAŞLATILDI");
        ekleIzinler();
    }

    public void ekleIzinler() throws Exception {
        List<HolUser> holUser = repository.findAll();

        try {
            for (HolUser holuser : holUser) {
                if (holuser.getIsBas() != null && holuser.getDogTar() != null) {
                    LocalDate isBaslangic = holuser.getIsBas().atZone(ZoneId.systemDefault()).toLocalDate();
                    LocalDate dogumTarihi = holuser.getDogTar().atZone(ZoneId.systemDefault()).toLocalDate();
                    Double toplamhakedis = holuser.getTopHak();
                    Double toplamkullanilan = holuser.getTopKul();
                    Double toplamyillik = holuser.getTopYil();
                    Double kalanyillik = holuser.getKalYil();

                    LocalDate bugun = LocalDate.now();
                    Period period = Period.between(isBaslangic, bugun);
                    Period period2 = Period.between(dogumTarihi, bugun);
                    int yilFarki = period.getYears();
                    int yilFarki2 = period2.getYears();
                    LocalDate isBasYilDonduguGun = isBaslangic.plusYears(yilFarki);
                    boolean tamYilDoldu = isBasYilDonduguGun.isEqual(bugun);
                    Instant yeniHakedisTarihi = holuser.getYilHak().plus(Period.ofYears(1));

                    if(tamYilDoldu) {
                        System.out.println("Yıl Farkı : " + yilFarki);
                        System.out.println("Yıl Farkı2 : " + yilFarki2);
                        System.out.println("isBasGun : " + isBasYilDonduguGun);
                        System.out.println("Tam Yıl? : " + tamYilDoldu);
                    }
                    if (yilFarki2 < 18 && yilFarki >= 1 && tamYilDoldu) {
                        // 18 yaş altı ve en az 1 yıldır çalışan personele 20 gün eklenir.
                        holuser.setTopHak(toplamhakedis + holuser.getYilGun());
                        holuser.setTopKul(toplamkullanilan + holuser.getKulYil());
                        holuser.setYilDevir(kalanyillik);
                        holuser.setYilGun(20.00);

                        holuser.setTopYil(holuser.getYilDevir() + holuser.getYilGun());
                        holuser.setKulYil(0.00);
                        holuser.setKalYil(holuser.getTopYil());
                        holuser.setKalMaz(3.00);
                        holuser.setKulMaz(0.00);
                        holuser.setYilHak(yeniHakedisTarihi);

                        System.out.println(holuser.getUser().getFullName() + " adlı personele 20 gün Yıllık izin ve 3 gün mazeret izni eklenmiştir.");
                    } else if (yilFarki2 >= 50 && yilFarki >= 1 && yilFarki < 5 && tamYilDoldu) {
                        // 50 yaş üzeri ve en az 1 yıldır çalışan personele 20 gün eklenir
                        holuser.setTopHak(toplamhakedis + holuser.getYilGun());
                        holuser.setTopKul(toplamkullanilan + holuser.getKulYil());
                        holuser.setYilDevir(kalanyillik);
                        holuser.setYilGun(20.00);

                        holuser.setTopYil(holuser.getYilDevir() + holuser.getYilGun());
                        holuser.setKulYil(0.00);
                        holuser.setKalYil(holuser.getTopYil());
                        holuser.setKalMaz(3.00);
                        holuser.setKulMaz(0.00);
                        holuser.setYilHak(yeniHakedisTarihi);

                        System.out.println(holuser.getUser().getFullName() + " adlı personele 20 gün Yıllık izin ve 3 gün mazeret izni eklenmiştir.");
                    } else if (yilFarki >= 1 && yilFarki < 5 && tamYilDoldu) {
                        // 1 yıl sonra 14 gün ekleme
                        holuser.setTopHak(toplamhakedis + holuser.getYilGun());
                        holuser.setTopKul(toplamkullanilan + holuser.getKulYil());
                        holuser.setYilDevir(kalanyillik);
                        holuser.setYilGun(14.00);

                        holuser.setTopYil(holuser.getYilDevir() + holuser.getYilGun());
                        holuser.setKulYil(0.00);
                        holuser.setKalYil(holuser.getTopYil());
                        holuser.setKalMaz(3.00);
                        holuser.setKulMaz(0.00);
                        holuser.setYilHak(yeniHakedisTarihi);

                        System.out.println(holuser.getUser().getFullName() + " adlı personele 1 yıl sonrası Yıllık izin ve Mazeret izinleri eklenmiştir.");
                    } else if (yilFarki >= 5 && yilFarki < 15 && tamYilDoldu) {
                        // 5-14 yıl arasında 20 gün ekleme
                        holuser.setTopHak(toplamhakedis + holuser.getYilGun());
                        holuser.setTopKul(toplamkullanilan + holuser.getKulYil());
                        holuser.setYilDevir(kalanyillik);
                        holuser.setYilGun(20.00);

                        holuser.setTopYil(holuser.getYilDevir() + holuser.getYilGun());
                        holuser.setKulYil(0.00);
                        holuser.setKalYil(holuser.getTopYil());
                        holuser.setKalMaz(3.00);
                        holuser.setKulMaz(0.00);
                        holuser.setYilHak(yeniHakedisTarihi);

                        System.out.println(holuser.getUser().getFullName() + " adlı personele 5-14 yıl arası Yıllık izin ve Mazeret izinleri eklenmiştir.");
                    } else if (yilFarki >= 15 && tamYilDoldu) {
                        // 15 yıl ve üzeri 26 gün ekleme
                        holuser.setTopHak(toplamhakedis + holuser.getYilGun());
                        holuser.setTopKul(toplamkullanilan + holuser.getKulYil());
                        holuser.setYilDevir(kalanyillik);
                        holuser.setYilGun(26.00);

                        holuser.setTopYil(holuser.getYilDevir() + holuser.getYilGun());
                        holuser.setKulYil(0.00);
                        holuser.setKalYil(holuser.getTopYil());
                        holuser.setKalMaz(3.00);
                        holuser.setKulMaz(0.00);
                        holuser.setYilHak(yeniHakedisTarihi);
                        System.out.println(holuser.getUser().getFullName() + " adlı personele 15 yıl ve üzeri Yıllık izin ve Mazeret izinleri eklenmiştir.");
                    }
                }
            }
            repository.saveAll(holUser);

            System.out.println("İZİN EKLEME SERVİSİ TAMAMLANDI");
        } catch (Exception e) {
            System.out.println("İZİN EKLENİRKEN BİR HATA OLDU! BU TARİHTE İZİN EKLENMESİ GEREKEN KİŞİYİ KONTROL ET!");
            try {
                mailService.sendEmail("hikmet@meteorpetrol.com","meteorpanel-InsertHolidayService Hatası!",
                    "Servis çalışırken bir hata oluştu, bugün izin eklenmesi gereken biri varsa girip kontrol et!",false,false);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }

    }
}
