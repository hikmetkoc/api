package tr.com.meteor.crm.trigger;

import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import tr.com.meteor.crm.domain.*;
import tr.com.meteor.crm.repository.*;
import tr.com.meteor.crm.service.*;
import tr.com.meteor.crm.utils.attributevalues.HolDayStatus;
import tr.com.meteor.crm.utils.attributevalues.HolidayStateStatus;
import tr.com.meteor.crm.utils.attributevalues.HolidayStatus;

import javax.validation.constraints.NotNull;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component(HolidayTrigger.QUALIFIER)
public class HolidayTrigger extends Trigger<Holiday, UUID, HolidayRepository> {
    final static String QUALIFIER = "HolidayTrigger";

    private final UserRepository userRepository;

    private final HolUserRepository holUserRepository;

    private final VocationDayRepository vocationDayRepository;

    private final HolManagerRepository limitRepository;

    private final RoleRepository roleRepository;

    private final UserService userService;
    private final MailService mailService;
    public HolidayTrigger(CacheManager cacheManager, HolidayRepository holidayRepository,
                          BaseUserService baseUserService, BaseConfigurationService baseConfigurationService,
                          UserRepository userRepository, MailService mailService,
                          UserService userService, HolUserRepository holUserRepository, VocationDayRepository vocationDayRepository, HolManagerRepository limitRepository, BaseRoleService baseRoleService1, RoleRepository roleRepository) {
        super(cacheManager, Holiday.class, HolidayTrigger.class, holidayRepository, baseUserService, baseConfigurationService);
        this.userRepository = userRepository;
        this.mailService = mailService;
        this.userService = userService;
        this.holUserRepository = holUserRepository;
        this.vocationDayRepository = vocationDayRepository;
        this.limitRepository = limitRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public Holiday beforeInsert(@NotNull Holiday newEntity) throws Exception {
        //newEntity.setLock(false);
        if (newEntity.getOwner() == null) {
            newEntity.setOwner(baseUserService.getUserFullFetched(getCurrentUserId()).get());
        } else {
            /*List<HolManager> limits = limitRepository.findByUserId(newEntity.getOwner().getId());
            for (HolManager HolManager : limits) {
                if (!newEntity.getUser().getId().equals(HolManager.getChief().getId())) {
                    throw new Exception("Sadece bir alt biriminizin çalışanlarına izin oluşturabilirsiniz!");
                }
            }*/
            /*if (!newEntity.getOwner().getId().equals(getCurrentUserId()) && !getCurrentUserId().equals(baseUserService.getUserFullFetched(2000L).get().getId())) {
                throw new Exception("Sadece kendi adınıza izin talebinde bulunabilirsiniz, boş bırakarakta kendinizi seçmiş olursunuz.");
            }*/
        }
        if (newEntity.getUser() == null){
            newEntity.setUser(baseUserService.getUserFullFetched(getCurrentUserId()).get());
        } else {
            if (!newEntity.getUser().getId().equals(getCurrentUserId())) {
                throw new Exception("Bu ekranda sadece kendi profilinizden talepte bulunabilirsiniz.");
            }
        }
        if (newEntity.getAssigner() == null) {
            List<HolManager> limits = limitRepository.findByUserId(newEntity.getOwner().getId());
            for (HolManager HolManager : limits) {
                if (HolManager.getUser().getId().equals(newEntity.getOwner().getId())) {
                    newEntity.setAssigner(HolManager.getChief());
                    if (newEntity.getUser() != null && !newEntity.getUser().getId().equals(newEntity.getOwner().getId()) && !getCurrentUser().equals(baseUserService.getUserFullFetched(2000L).get()) && !getCurrentUserId().equals(baseUserService.getUserFullFetched(2001L).get().getId())) {
                        if (!newEntity.getUser().getId().equals(HolManager.getChief().getId())) {
                            throw new Exception("Sadece kendi adınıza ve bir alt biriminizin çalışanlarına izin oluşturabilirsiniz!");
                        }
                    }
                }
            }

        }
        LocalDate startDate = newEntity.getStartDate().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate endDate = newEntity.getEndDate().atZone(ZoneId.systemDefault()).toLocalDate();

        for (VocationDay voca : vocationDayRepository.findAll()) {
            LocalDate vocationStartDate = voca.getHolStart().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate vocationEndDate = voca.getHolEnd().atZone(ZoneId.systemDefault()).toLocalDate();

            if ((startDate.isEqual(vocationStartDate) || startDate.isEqual(vocationEndDate) ||
                endDate.isEqual(vocationStartDate) || endDate.isEqual(vocationEndDate) ||
                (startDate.isAfter(vocationStartDate) && startDate.isBefore(vocationEndDate)) ||
                (endDate.isAfter(vocationStartDate) && endDate.isBefore(vocationEndDate)))) {
                throw new Exception("İzin başlangıç veya bitiş tarihi, " + voca.getDescription() + " tatil aralığındaki bir güne denk geliyor!");
            }

        }



        if (!newEntity.getType().getId().equals(HolidayStatus.MAZERET.getId())) {
            LocalDateTime localDateTime = newEntity.getEndDate().atZone(ZoneId.systemDefault()).toLocalDateTime();
            LocalDateTime newLocalDateTime = localDateTime.withHour(18);
            newEntity.setEndDate(newLocalDateTime.atZone(ZoneId.systemDefault()).toInstant());
        }


        User owner = baseUserService.getUserFullFetched(newEntity.getOwner().getId()).get();
        String roleName = owner.getRoles().toString();
        String cleanedRoleName = roleName.replaceAll("^\\[Role\\{id='(.+)'\\}\\]$", "$1");

        if ((startDate.getDayOfWeek() == DayOfWeek.SATURDAY || startDate.getDayOfWeek() == DayOfWeek.SUNDAY) && !(cleanedRoleName.equals("ROLE_MAVI") || cleanedRoleName.equals("ROLE_BEYAZ_MAVI"))) {
            throw new Exception("Ofis çalışanları haftasonu için izin alamaz!");
        }

        if ((endDate.getDayOfWeek() == DayOfWeek.SATURDAY || endDate.getDayOfWeek() == DayOfWeek.SUNDAY) && !(cleanedRoleName.equals("ROLE_MAVI") || cleanedRoleName.equals("ROLE_BEYAZ_MAVI")) ) {
            throw new Exception("Ofis çalışanları haftasonu için izin alamaz!");
        }

        if (newEntity.getStartDate().compareTo(newEntity.getEndDate())>0) {
            throw new Exception("İzin bitiş tarihi, başlangıç tarihinden sonra olmalıdır.");
        }
        if (newEntity.getStartDate().equals(newEntity.getEndDate())) {
            throw new Exception("İzin bitiş tarihi, başlangıç tarihinden sonra olmalıdır.");
        }


        double izingunDouble = 0.00;
        List<HolUser> holUser = holUserRepository.findByUserId(newEntity.getOwner().getId());
        for (HolUser holuser : holUser) {
            if (holuser.getUser().getId().equals(newEntity.getOwner().getId())) {
                Instant start = newEntity.getStartDate();
                Instant end = newEntity.getEndDate();
                Boolean haftalik = newEntity.getHaftalikizin();
                String izingun = newEntity.getHaftalikGun().getId();
                float leaveDays = calculateLeaveDays(start, end, cleanedRoleName, haftalik, izingun);
                izingunDouble = Math.round(leaveDays * 100.0) / 100.0;
            }
        }

        if (newEntity.getType().getId().equals(HolidayStatus.BABALIK.getId())) {
            if (izingunDouble != 5.00) {
                throw new Exception("Babalık izni 5 gün girilmelidir!");
            }
        }
        if (newEntity.getType().getId().equals(HolidayStatus.OLUM.getId())) {
            if (izingunDouble != 3.00) {
                throw new Exception("Ölüm izni 3 gün girilmelidir!");
            }
        }
        if (newEntity.getType().getId().equals(HolidayStatus.EVLILIK.getId())) {
            if (izingunDouble != 3.00) {
                throw new Exception("Evlilik izni 3 gün girilmelidir!");
            }
        }
        if(newEntity.getApprovalStatus().getId().equals(HolidayStateStatus.AKTIF.getId()) && !newEntity.getAssigner().getId().equals(getCurrentUserId())){
            throw new Exception("Sadece yöneticiniz onay verebilir!");
        }
        if(newEntity.getApprovalStatus().getId().equals(HolidayStateStatus.RED.getId()) && !newEntity.getAssigner().getId().equals(getCurrentUserId())){
            throw new Exception("Sadece yöneticiniz reddebilir!");
        }
        if(newEntity.getApprovalStatus().getId().equals(HolidayStateStatus.ISLENDI.getId()) && !getCurrentUserId().equals(baseUserService.getUserFullFetched(2000L).get().getId()) && !getCurrentUserId().equals(baseUserService.getUserFullFetched(2001L).get().getId())) {
            throw new Exception("Sadece İnsan Kaynakları Personeli İŞLENDİ durumuna çevirebilir!");
        }
        if (newEntity.getStartDate() == null) {
            throw new Exception("İzin başlangıç tarihi zorunludur!");
        }
        if (newEntity.getEndDate() == null) {
            throw new Exception("İzin bitiş tarihi zorunludur!");
        }
        if (newEntity.getType().getId().equals(HolidayStatus.MAZERET.getId())) {
            List<HolUser> holUser2 = holUserRepository.findByUserId(newEntity.getOwner().getId());
            for (HolUser holuser : holUser2) {
                if (holuser.getUser().getId().equals(newEntity.getOwner().getId())) {
                    Instant start1 = newEntity.getStartDate();
                    Instant end1 = newEntity.getEndDate();
                    Boolean haftalik = newEntity.getHaftalikizin();
                    String izingun = newEntity.getHaftalikGun().getId();
                    float leaveDays2 = calculateLeaveDays(start1, end1, cleanedRoleName, haftalik, izingun);
                    DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
                    decimalFormatSymbols.setDecimalSeparator(','); // Ondalık ayırıcıyı virgül olarak ayarlayın
                    decimalFormatSymbols.setGroupingSeparator('.'); // Grup ayırıcıyı nokta olarak ayarlayın

                    DecimalFormat decimalFormat = new DecimalFormat("#,##", decimalFormatSymbols);

                    String formattedValue = decimalFormat.format(leaveDays2);
                    double izingunDouble2 = Double.parseDouble(formattedValue);
                    if (holuser.getKalMaz() - izingunDouble2 < 0) {
                        throw new Exception("Kullanılabilir mazeret izniniz " + holuser.getKalMaz() + " gündür!");
                    }
                }
            }
        }
        if (newEntity.getApprovalStatus().getId().equals(HolidayStateStatus.IPTAL.getId())) {
            throw new Exception("Yeni oluşturulan talebin durumu İPTAL olamaz!");
        }
        return newEntity;
    }

    @Override
    public Holiday afterInsert(@NotNull Holiday newEntity) throws Exception {
        Instant start = newEntity.getStartDate();
        Instant end = newEntity.getEndDate();

        User owner = baseUserService.getUserFullFetched(newEntity.getOwner().getId()).get();
        String roleName = owner.getRoles().toString();
        String cleanedRoleName = roleName.replaceAll("^\\[Role\\{id='(.+)'\\}\\]$", "$1");

        //System.out.println(roleName); // ROLE_MAVI
        Boolean haftalik = newEntity.getHaftalikizin();
        String izingun = newEntity.getHaftalikGun().getId();
        float leaveDays = calculateLeaveDays(start, end, cleanedRoleName, haftalik, izingun);
        double izingunDouble = Math.round(leaveDays * 100.0) / 100.0;
        newEntity.setIzingun(izingunDouble);

        // VocationDay kontrolü için end tarihini LocalDate formatına çevirme
        LocalDate endDate = end.atZone(ZoneId.systemDefault()).toLocalDate();
        boolean isVocationDay = false;

        // VocationDay kontrolü
        for (VocationDay voca : vocationDayRepository.findAll()) {
            LocalDate vocationStartDate = voca.getHolStart().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate vocationEndDate = voca.getHolEnd().atZone(ZoneId.systemDefault()).toLocalDate();
            if (endDate.isEqual(vocationStartDate) || (endDate.isAfter(vocationStartDate) && endDate.isBefore(vocationEndDate))) {
                isVocationDay = true;
                newEntity.setComeDate(voca.getHolEnd()); // VocationDay holEnd değeriyle set ediliyor
                break;
            }
        }

        if (!newEntity.getType().getId().equals(HolidayStatus.MAZERET.getId())) {
            LocalDateTime localDateTime = end.atZone(ZoneId.systemDefault()).toLocalDateTime();
            LocalDateTime nextDayAt0830 = localDateTime.plusDays(1).withHour(8).withMinute(30).withSecond(0).withNano(0);
            Instant come = nextDayAt0830.atZone(ZoneId.systemDefault()).toInstant();
            if (!isVocationDay) {
                newEntity.setComeDate(come);
                if (endDate.getDayOfWeek() == DayOfWeek.FRIDAY && !(cleanedRoleName.equals("ROLE_MAVI") || cleanedRoleName.equals("ROLE_BEYAZ_MAVI"))) {
                    newEntity.setComeDate(nextDayAt0830.plusDays(2).atZone(ZoneId.systemDefault()).toInstant());
                }
            }
        } else {
            LocalDateTime localDateTime = end.atZone(ZoneId.systemDefault()).toLocalDateTime();
            LocalDateTime nextAddOneMinute = localDateTime.plusMinutes(1);
            Instant come = nextAddOneMinute.atZone(ZoneId.systemDefault()).toInstant();
            if (!isVocationDay) {
                newEntity.setComeDate(come);
            }
        }

        LocalDateTime olusturmatarihi = LocalDateTime.ofInstant(newEntity.getCreatedDate(), ZoneId.systemDefault());
        LocalDateTime izinbaslangictarihi = LocalDateTime.ofInstant(newEntity.getStartDate(), ZoneId.systemDefault());
        LocalDateTime izinbitistarihi = LocalDateTime.ofInstant(newEntity.getEndDate(), ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String olt = olusturmatarihi.format(formatter);
        String ibat = izinbaslangictarihi.format(formatter);
        String ibit = izinbitistarihi.format(formatter);

       mailService.sendEmail(newEntity.getAssigner().getEposta(),
            "MeteorPanel - Yeni İzin Talebi",newEntity.getOwner().getFullName() + ", " +
                olt + " tarihinde " + ibat + " - " +
                ibit + " tarihlerinde kullanılmak üzere " + newEntity.getType().getLabel() +
                " talebinde bulunmuştur.\nTalep edilen iznin süresi " + newEntity.getIzingun().toString() + " gündür.\n" +
                "İlgili talebin onaycısı sizsiniz.",
            false,false);
        return newEntity;
    }


    public float calculateLeaveDays(Instant start, Instant end, String role, Boolean haftalik, String izingun) {
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
                boolean isVocationDay = false; // VocationDay kaydı kontrolü için bir bayrak
                for (VocationDay voca : vocationDayRepository.findAll()) {
                    LocalDate vocationStartDate = voca.getHolStart().atZone(ZoneId.systemDefault()).toLocalDate();
                    LocalDate vocationEndDate = voca.getHolEnd().atZone(ZoneId.systemDefault()).toLocalDate();
                    if (currentDate.isEqual(vocationStartDate) || (currentDate.isAfter(vocationStartDate) && currentDate.isBefore(vocationEndDate))) {
                        isVocationDay = true;
                        break;
                    }
                }
                if (!isVocationDay && currentDate.getDayOfWeek() != DayOfWeek.SATURDAY && currentDate.getDayOfWeek() != DayOfWeek.SUNDAY) {
                    if (!(role.equals("ROLE_MAVI") || role.equals("ROLE_BEYAZ_MAVI"))) {
                        leaveDays += 1.0f;
                    }
                }
                DayOfWeek dayOfWeek = DayOfWeek.SUNDAY;

                if (!isVocationDay && (role.equals("ROLE_MAVI") || role.equals("ROLE_BEYAZ_MAVI"))) {
                    leaveDays += 1.0f;
                    if (izingun.equals(HolDayStatus.PAZARTESI.getId()))
                    {
                        dayOfWeek = DayOfWeek.MONDAY;
                    } else if (izingun.equals(HolDayStatus.SALI.getId())) {
                        dayOfWeek = DayOfWeek.TUESDAY;
                    } else if (izingun.equals(HolDayStatus.CARSAMBA.getId())) {
                        dayOfWeek = DayOfWeek.WEDNESDAY;
                    } else if (izingun.equals(HolDayStatus.PERSEMBE.getId())) {
                        dayOfWeek = DayOfWeek.THURSDAY;
                    } else if (izingun.equals(HolDayStatus.CUMA.getId())) {
                        dayOfWeek = DayOfWeek.FRIDAY;
                    } else if (izingun.equals(HolDayStatus.CUMARTESI.getId())) {
                        dayOfWeek = DayOfWeek.SATURDAY;
                    }
                    if (haftalik && currentDate.getDayOfWeek() == dayOfWeek) {
                        leaveDays -= 1.0f;
                    }
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
    public Holiday beforeUpdate(@NotNull Holiday oldEntity, @NotNull Holiday newEntity) throws Exception {
        if (newEntity.getOwner() == null) {
            newEntity.setOwner(getCurrentUser());
        }
        List <HolManager> holManagers = limitRepository.findByUserId(newEntity.getOwner().getId());
        if(newEntity.getApprovalStatus().getId().equals(HolidayStateStatus.ISLENDI.getId()) && !getCurrentUserId().equals(baseUserService.getUserFullFetched(2000L).get().getId()) && !getCurrentUserId().equals(baseUserService.getUserFullFetched(2001L).get().getId())) {
            throw new Exception("Sadece İnsan Kaynakları Personeli İŞLENDİ durumuna çevirebilir!");
        }
        else if(!newEntity.getAssigner().getId().equals(getCurrentUserId()) && !newEntity.getUser().getId().equals(getCurrentUserId()) && !getCurrentUserId().equals(baseUserService.getUserFullFetched(2000L).get().getId()) && !getCurrentUserId().equals(baseUserService.getUserFullFetched(2001L).get().getId()) && !getCurrentUserId().equals(holManagers.get(0).getManager().getId())){
            throw new Exception("Sadece talebi oluşturan kişi ve onaycı kişi düzenleme yapabilir!");
        }
        if(newEntity.getApprovalStatus().getId().equals(HolidayStateStatus.AKTIF.getId()) && !newEntity.getAssigner().getId().equals(getCurrentUserId()) && !getCurrentUserId().equals(holManagers.get(0).getManager().getId())){
            throw new Exception("Bu talebe sadece Onaycınız onay verebilir!");
        }
        /*if(newEntity.getApprovalStatus().getId().equals(HolidayStateStatus.AKTIF.getId()) && newEntity.getAssigner().getId().equals(getCurrentUserId()) && newEntity.getLock().equals(true)){
            throw new Exception("Onaylanan izinde değişiklik yapamazsınız!");
        }
        if(newEntity.getApprovalStatus().getId().equals(HolidayStateStatus.RED.getId()) && newEntity.getAssigner().getId().equals(getCurrentUserId()) && newEntity.getLock().equals(true)){
            throw new Exception("Reddedilen izinde değişiklik yapamazsınız!");
        }*/
        if(newEntity.getApprovalStatus().getId().equals(HolidayStateStatus.RED.getId()) && !newEntity.getAssigner().getId().equals(getCurrentUserId())){
            throw new Exception("Bu talebi sadece Onaycınız reddebilir!");
        }
        /*if(newEntity.getApprovalStatus().getId().equals(HolidayStateStatus.PASIF.getId()) && !newEntity.getAssigner().getId().equals(getCurrentUserId()) && newEntity.getLock().equals(true)){
            throw new Exception("Talep onaylandıktan veya reddedildikten sonra işlem yapamazsınız!");
        }
        if(newEntity.getApprovalStatus().getId().equals(HolidayStateStatus.IPTAL.getId()) && !newEntity.getAssigner().getId().equals(getCurrentUserId()) && newEntity.getLock().equals(true)){
            throw new Exception("Talep onaylandıktan veya reddedildikten sonra işlem yapamazsınız!");
        }*/

        if(!newEntity.getOwner().getId().equals(getCurrentUserId()) && newEntity.getApprovalStatus().getId().equals(HolidayStateStatus.IPTAL.getId())) {
            throw new Exception("Sadece talep eden kişi Onay Bekliyor ya da İptal durumuna çevirebilir!");
        }

        /*if(newEntity.getAssigner().getId().equals(getCurrentUserId()) && newEntity.getLock().equals(false)){
            newEntity.setLock(true);
        }*/
        /*if(newEntity.getOwner().getId().equals(getCurrentUserId()) && newEntity.getApprovalStatus().getId().equals(HolidayStateStatus.PASIF.getId())) {
            newEntity.setLock(false);
        }
        if(!newEntity.getOwner().getId().equals(getCurrentUserId()) && (newEntity.getApprovalStatus().getId().equals(HolidayStateStatus.PASIF.getId()) || newEntity.getApprovalStatus().getId().equals(HolidayStateStatus.IPTAL.getId()))) {
            throw new Exception("Sadece talep eden kişi Onay Bekliyor ya da İptal durumuna çevirebilir!");
        }*/
        return newEntity;
    }

    @Override
    public Holiday afterUpdate(@NotNull Holiday oldEntity, @NotNull Holiday newEntity) throws Exception {

        Instant start = newEntity.getStartDate();
        Instant end = newEntity.getEndDate();
        double kullanilan = 0.00;
        double kalan = 0.00;

        User owner = baseUserService.getUserFullFetched(newEntity.getOwner().getId()).get();
        String roleName = owner.getRoles().toString();
        String cleanedRoleName = roleName.replaceAll("^\\[Role\\{id='(.+)'\\}\\]$", "$1");

        LocalDate startDate = start.atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate endDate = end.atZone(ZoneId.systemDefault()).toLocalDate();

        if ((startDate.getDayOfWeek() == DayOfWeek.SATURDAY || startDate.getDayOfWeek() == DayOfWeek.SUNDAY) && !(cleanedRoleName.equals("ROLE_MAVI") || cleanedRoleName.equals("ROLE_BEYAZ_MAVI")) ) {
            throw new Exception("Ofis çalışanları haftasonu için izin alamaz!");
        }

        if ((endDate.getDayOfWeek() == DayOfWeek.SATURDAY || endDate.getDayOfWeek() == DayOfWeek.SUNDAY) && !(cleanedRoleName.equals("ROLE_MAVI") || cleanedRoleName.equals("ROLE_BEYAZ_MAVI"))) {
            throw new Exception("Ofis çalışanları haftasonu için izin alamaz!");
        }

        Boolean haftalik = false;
        String izingun = null;
        if (newEntity.getHaftalikizin() != null) {
            haftalik = newEntity.getHaftalikizin();
        }
        if (newEntity.getHaftalikGun() != null) {
            izingun = newEntity.getHaftalikGun().getId();
        }
        float leaveDays = calculateLeaveDays(start, end, cleanedRoleName, haftalik, izingun);
        double izingunDouble = Math.round(leaveDays * 100.0) / 100.0;
        newEntity.setIzingun(izingunDouble);

        if (newEntity.getApprovalStatus().getId().equals(HolidayStateStatus.AKTIF.getId())) {
            List<HolUser> hu = holUserRepository.findByUserId(newEntity.getOwner().getId());
            if (!hu.isEmpty()) {
                HolUser holuser = hu.get(0);
                if (newEntity.getType().getId().equals(HolidayStatus.YILLIK.getId())) {
                    kullanilan = (double) holuser.getKulYil();
                    kalan = (double) holuser.getKalYil();
                    holuser.setKulYil(kullanilan + izingunDouble);
                    holuser.setKalYil(kalan - izingunDouble);
                }
                if (newEntity.getType().getId().equals(HolidayStatus.MAZERET.getId())) {
                    kullanilan = (double) holuser.getKulMaz();
                    kalan = (double) Math.round(holuser.getKalMaz() * 100.0) / 100.0;;
                    holuser.setKulMaz(kullanilan + izingunDouble);
                    holuser.setTopKulMaz(holuser.getTopKulMaz() + holuser.getKulMaz());
                    holuser.setKalMaz(kalan - izingunDouble);
                }
                if (newEntity.getType().getId().equals(HolidayStatus.BABALIK.getId())) {
                    kullanilan = (double) holuser.getKulBaba();
                    holuser.setKulBaba(kullanilan + izingunDouble);
                }
                if (newEntity.getType().getId().equals(HolidayStatus.DOGUM.getId())) {
                    kullanilan = (double) holuser.getKulDog();
                    holuser.setKulDog(kullanilan + izingunDouble);
                }
                if (newEntity.getType().getId().equals(HolidayStatus.OLUM.getId())) {
                    kullanilan = (double) holuser.getKulOlum();
                    holuser.setKulOlum(kullanilan + izingunDouble);
                }
                if (newEntity.getType().getId().equals(HolidayStatus.RAPOR.getId())) {
                    kullanilan = (double) holuser.getKulRap();
                    holuser.setKulRap(kullanilan + izingunDouble);
                }
                if (newEntity.getType().getId().equals(HolidayStatus.IDARI.getId())) {
                    kullanilan = (double) holuser.getKulIdr();
                    holuser.setKulIdr(kullanilan + izingunDouble);
                }
                if (newEntity.getType().getId().equals(HolidayStatus.EVLILIK.getId())) {
                    kullanilan = (double) holuser.getKulEvl();
                    holuser.setKulEvl(kullanilan + izingunDouble);
                }
                if (newEntity.getType().getId().equals(HolidayStatus.UCRETSIZ.getId())) {
                    kullanilan = (double) holuser.getKulUcr();
                    holuser.setKulUcr(kullanilan + izingunDouble);
                }
                holUserRepository.save(holuser);
            }
        }

        LocalDateTime olusturmatarihi = LocalDateTime.ofInstant(newEntity.getCreatedDate(), ZoneId.systemDefault());
        LocalDateTime izinbaslangictarihi = LocalDateTime.ofInstant(newEntity.getStartDate(), ZoneId.systemDefault());
        LocalDateTime izinbitistarihi = LocalDateTime.ofInstant(newEntity.getEndDate(), ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String olt = olusturmatarihi.format(formatter);
        String ibat = izinbaslangictarihi.format(formatter);
        String ibit = izinbitistarihi.format(formatter);

        if (newEntity.getApprovalStatus().getId().equals(HolidayStateStatus.AKTIF.getId())) {
            mailService.sendEmail(newEntity.getOwner().getEposta(),
                "MeteorPanel - İzin Talebi",newEntity.getOwner().getFullName() + ", " +
                    olt + " tarihinde " + ibat + " - " +
                    ibit + " tarihlerinde kullanılmak üzere " + newEntity.getType().getLabel() +
                    " türünde yapmış olduğunuz talep " + newEntity.getAssigner().getFullName() + " tarafından ONAYLANMIŞTIR.\n" +
                    "Talep edilen iznin süresi " + newEntity.getIzingun().toString() + " gündür.\n" +
                    "İzni kullanabilmeniz için İZİN TALEP FORMU'nu yazdırın. Onaycınıza ve İnsan Kaynakları'na imzalattıktan sonra DOSYA YÖNETİCİSİ bölümünden taratıp yükleyin.\n" +
                    "Eğer İnsan Kaynakları personeline ulaşamazsanız Onaycınıza imzalattıktan sonra da DOSYA YÖNETİCİSİ'ne yükleme yapabilirsiniz.",
                false,false);
        }
        if (newEntity.getApprovalStatus().getId().equals(HolidayStateStatus.RED.getId())) {
            mailService.sendEmail(newEntity.getOwner().getEposta(),
                "MeteorPanel - Yeni İzin Talebi",newEntity.getOwner().getFullName() + ", " +
                    olt + " tarihinde " + ibat + " - " +
                    ibit + " tarihlerinde kullanılmak üzere " + newEntity.getType().getLabel() +
                    " türünde yapmış olduğunuz talep " + newEntity.getAssigner().getFullName() + " tarafından REDDEDİLMİŞTİR.\n",
                false,false);
        }

        return newEntity;
    }
}
