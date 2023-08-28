package tr.com.meteor.crm.trigger;

import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import tr.com.meteor.crm.domain.BuyLimit;
import tr.com.meteor.crm.domain.ExuseHoliday;
import tr.com.meteor.crm.domain.HolUser;
import tr.com.meteor.crm.domain.Holiday;
import tr.com.meteor.crm.repository.*;
import tr.com.meteor.crm.service.BaseConfigurationService;
import tr.com.meteor.crm.service.BaseUserService;
import tr.com.meteor.crm.service.MailService;
import tr.com.meteor.crm.service.UserService;
import tr.com.meteor.crm.utils.attributevalues.HolidayStateStatus;
import tr.com.meteor.crm.utils.attributevalues.HolidayStatus;

import javax.validation.constraints.NotNull;
import java.time.*;
import java.util.List;
import java.util.UUID;

@Component(ExuseHolidayTrigger.QUALIFIER)
public class ExuseHolidayTrigger extends Trigger<ExuseHoliday, UUID, ExuseHolidayRepository> {
    final static String QUALIFIER = "ExuseHolidayTrigger";

    private final UserRepository userRepository;

    private final HolUserRepository holUserRepository;

    private final BuyLimitRepository limitRepository;

    private final UserService userService;
    private final MailService mailService;

    public ExuseHolidayTrigger(CacheManager cacheManager, ExuseHolidayRepository holidayRepository,
                               BaseUserService baseUserService, BaseConfigurationService baseConfigurationService,
                               UserRepository userRepository, MailService mailService,
                               UserService userService, HolUserRepository holUserRepository, BuyLimitRepository limitRepository) {
        super(cacheManager, ExuseHoliday.class, ExuseHolidayTrigger.class, holidayRepository, baseUserService, baseConfigurationService);
        this.userRepository = userRepository;
        this.mailService = mailService;
        this.userService = userService;
        this.holUserRepository = holUserRepository;
        this.limitRepository = limitRepository;
    }

    @Override
    public ExuseHoliday beforeInsert(@NotNull ExuseHoliday newEntity) throws Exception {

        if (newEntity.getOwner() == null) {
            newEntity.setOwner(baseUserService.getUserFullFetched(getCurrentUserId()).get());
        }
        if (newEntity.getUser() == null) {
            newEntity.setUser(baseUserService.getUserFullFetched(getCurrentUserId()).get());
        }
        if (newEntity.getAssigner() == null) {
            /*List<BuyLimit> limits = limitRepository.findByUserId(newEntity.getOwner().getId());
            for (BuyLimit buyLimit : limits) {
                if (buyLimit.getUser().getId().equals(newEntity.getOwner().getId())) {
                    newEntity.setAssigner(buyLimit.getChief());
                }
            }*/
        }

        if (newEntity.getApprovalStatus().getId().equals(HolidayStateStatus.AKTIF.getId()) && !newEntity.getAssigner().getId().equals(getCurrentUserId())) {
            throw new Exception("Sadece yöneticiniz onay verebilir!");
        }
        if (newEntity.getApprovalStatus().getId().equals(HolidayStateStatus.RED.getId()) && !newEntity.getAssigner().getId().equals(getCurrentUserId())) {
            throw new Exception("Sadece yöneticiniz reddebilir!");
        }
        if (newEntity.getApprovalStatus().getId().equals(HolidayStateStatus.ISLENDI.getId()) && !getCurrentUserId().equals(baseUserService.getUserFullFetched(15L).get().getId())) {
            throw new Exception("Sadece İnsan Kaynakları Personeli İŞLENDİ durumuna çevirebilir!");
        }
        if (newEntity.getStartDate() == null) {
            throw new Exception("İzin başlangıç tarihi zorunludur!");
        }
        if (newEntity.getEndDate() == null) {
            throw new Exception("İzin bitiş tarihi zorunludur!");
        }

        List<HolUser> holUser = holUserRepository.findByUserId(newEntity.getOwner().getId());
        for (HolUser holuser : holUser) {
            if (holuser.getUser().getId().equals(newEntity.getOwner().getId())) {
                Instant start = newEntity.getStartDate();
                Instant end = newEntity.getEndDate();
                float leaveDays = calculateLeaveDays(start, end);
                double izingunDouble = Math.round(leaveDays * 100.0) / 100.0;
                if (holuser.getKalMaz() - izingunDouble < 0) {
                    throw new Exception("Kullanılabilir mazeret izniniz " + holuser.getKalMaz() + " gündür!");
                }
            }
        }
        return newEntity;
    }

    @Override
    public ExuseHoliday afterInsert(@NotNull ExuseHoliday newEntity) throws Exception {
        Instant start = newEntity.getStartDate();
        Instant end = newEntity.getEndDate();
        float leaveDays = calculateLeaveDays(start, end);
        double izingunDouble = Math.round(leaveDays * 100.0) / 100.0;
        newEntity.setIzingun(izingunDouble);
        return newEntity;
    }


    public float calculateLeaveDays(Instant start, Instant end) {
        float leaveDays = 0;
        LocalDate startDate = start.atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate endDate = end.atZone(ZoneId.systemDefault()).toLocalDate();

        if (startDate.isEqual(endDate)) {
            // İzin aynı gün içinde başlayıp bitiyorsa
            LocalTime startHour = start.atZone(ZoneId.systemDefault()).toLocalTime();
            LocalTime endHour = end.atZone(ZoneId.systemDefault()).toLocalTime();
            float leaveHours = (endHour.toSecondOfDay() - startHour.toSecondOfDay()) / 36000.0f; // İzin süresi saat cinsinden
            leaveDays = leaveHours; // İzin süresi gün cinsinden
        } else {
            // İzin birden fazla gün içinde ise
            // İlk gün için
            LocalTime startHour = start.atZone(ZoneId.systemDefault()).toLocalTime();
            if (startHour.isBefore(LocalTime.of(8, 30))) {
                startHour = LocalTime.of(8, 30);
            }
            if (startHour.isAfter(LocalTime.of(18, 30))) {
                startHour = LocalTime.of(18, 30);
            }
            float leaveHours = (LocalTime.of(18, 30).toSecondOfDay() - startHour.toSecondOfDay()) / 36000.0f;
            leaveDays += leaveHours;

            // Aradaki günler için
            LocalDate currentDate = startDate.plusDays(1);
            while (currentDate.isBefore(endDate)) {
                if (currentDate.getDayOfWeek() != DayOfWeek.SATURDAY && currentDate.getDayOfWeek() != DayOfWeek.SUNDAY) {
                    leaveDays += 1.0f;
                }
                currentDate = currentDate.plusDays(1);
            }

            // Son gün için
            LocalTime endHour = end.atZone(ZoneId.systemDefault()).toLocalTime();
            if (endHour.isBefore(LocalTime.of(8, 30))) {
                endHour = LocalTime.of(8, 30);
            }
            if (endHour.isAfter(LocalTime.of(18, 30))) {
                endHour = LocalTime.of(18, 30);
            }
            leaveHours = (endHour.toSecondOfDay() - LocalTime.of(8, 30).toSecondOfDay()) / 36000.0f;
            leaveDays += leaveHours;
        }

        return leaveDays;
    }


    @Override
    public ExuseHoliday beforeUpdate(@NotNull ExuseHoliday oldEntity, @NotNull ExuseHoliday newEntity) throws Exception {
        if (newEntity.getApprovalStatus().getId().equals(HolidayStateStatus.ISLENDI.getId()) && !getCurrentUserId().equals(baseUserService.getUserFullFetched(15L).get().getId())) {
            throw new Exception("Sadece İnsan Kaynakları Personeli İŞLENDİ durumuna çevirebilir!");
        } else if (!newEntity.getAssigner().getId().equals(getCurrentUserId()) && !newEntity.getUser().getId().equals(getCurrentUserId())) {
            throw new Exception("Sadece talebi oluşturan kişi ve onaycı kişi düzenleme yapabilir!");
        }
        if (newEntity.getApprovalStatus().getId().equals(HolidayStateStatus.AKTIF.getId()) && !newEntity.getAssigner().getId().equals(getCurrentUserId())) {
            throw new Exception("Bu talebe sadece Onaycınız onay verebilir!");
        }
        if (newEntity.getApprovalStatus().getId().equals(HolidayStateStatus.AKTIF.getId()) && newEntity.getAssigner().getId().equals(getCurrentUserId())) {
            throw new Exception("Onaylanan izinde değişiklik yapamazsınız!");
        }
        if (newEntity.getApprovalStatus().getId().equals(HolidayStateStatus.RED.getId()) && !newEntity.getAssigner().getId().equals(getCurrentUserId())) {
            throw new Exception("Bu talebe sadece Onaycınız reddebilir!");
        }
        return newEntity;
    }

    @Override
    public ExuseHoliday afterUpdate(@NotNull ExuseHoliday oldEntity, @NotNull ExuseHoliday newEntity) throws Exception {

        Instant start = newEntity.getStartDate();
        Instant end = newEntity.getEndDate();
        double kullanilan = 0.00;
        double kalan = 0.00;
        float leaveDays = calculateLeaveDays(start, end);
        double izingunDouble = Math.round(leaveDays * 100.0) / 100.0;
        newEntity.setIzingun(izingunDouble);

        List<HolUser> hu = holUserRepository.findByUserId(newEntity.getUser().getId());
        if (!hu.isEmpty()) {
            HolUser holuser = hu.get(0);
            double kullanilanmazeret = (double) holuser.getKulMaz();
            double kalanmazeret = (double) holuser.getKalMaz();
            double toplammazeret = (double) holuser.getTopKulMaz();
            holuser.setTopKulMaz(toplammazeret + izingunDouble);
            holuser.setKulMaz(kullanilanmazeret + izingunDouble);
            holuser.setKalMaz(kalanmazeret - izingunDouble);
            holUserRepository.save(holuser);
        }

        return newEntity;
    }
}
