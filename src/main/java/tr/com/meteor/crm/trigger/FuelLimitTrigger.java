package tr.com.meteor.crm.trigger;

import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import tr.com.meteor.crm.domain.FuelLimit;
import tr.com.meteor.crm.domain.BuyLimit;
import tr.com.meteor.crm.repository.*;
import tr.com.meteor.crm.service.*;
import tr.com.meteor.crm.utils.attributevalues.CostPlace;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.text.Normalizer;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Component(FuelLimitTrigger.QUALIFIER)
public class FuelLimitTrigger extends Trigger<FuelLimit, UUID, FuelLimitRepository> {
    final static String QUALIFIER = "FuelLimitTrigger";

    private final CustomerRepository customerRepository;

    private final FuelLimitService fuelLimitService;
    private final StoreRepository storeRepository;

    private final BuyLimitRepository limitRepository;

    private final MailService mailService;

    private final PostaGuverciniService postaGuverciniService;

    public FuelLimitTrigger(CacheManager cacheManager, FuelLimitRepository fuellimitRepository, BaseUserService baseUserService,
                            BaseConfigurationService baseConfigurationService, CustomerRepository customerRepository,
                            FuelLimitService service, StoreRepository storeRepository, BuyLimitRepository limitRepository, MailService mailService, PostaGuverciniService postaGuverciniService) {
        super(cacheManager, FuelLimit.class, FuelLimitTrigger.class, fuellimitRepository, baseUserService, baseConfigurationService);
        this.customerRepository = customerRepository;
        this.fuelLimitService = service;
        this.storeRepository = storeRepository;
        this.limitRepository = limitRepository;
        this.mailService = mailService;
        this.postaGuverciniService = postaGuverciniService;
    }

    @Override
    public FuelLimit beforeInsert(@NotNull FuelLimit newEntity) throws Exception {
        //1. VE 2.ONAYCIYI PARA BİRİMİ VE LİMİTE GÖRE AYARLA
        if (newEntity.getOwner() == null) {
            newEntity.setOwner(getCurrentUser());
        }
        if (newEntity.getFuelTl() == null) {
            newEntity.setFuelTl(BigDecimal.ZERO);
        }
        BigDecimal newTotal = new BigDecimal(newEntity.getTotal().getLabel());
        newEntity.setFuelTl(newTotal);
        if(fuelLimitService.limitControlService(newEntity.getCurcode(), newEntity.getFuelTl())) {
            BigDecimal limitTl = new BigDecimal("20000");
            BigDecimal onayciLimit = new BigDecimal("50000");
            if (newEntity.getFuelTl().compareTo(limitTl) > 0 && !newEntity.getOwner().equals(baseUserService.getUserFullFetched(93L).get())) {
                throw new Exception("EN FAZLA 20.000 TL TALEP EDİLEBİLİR!");
            }
            if (newEntity.getFuelTl().compareTo(onayciLimit) > 0 && newEntity.getOwner().equals(baseUserService.getUserFullFetched(93L).get())) {
                throw new Exception("EN FAZLA 50.000 TL TALEP EDİLEBİLİR!");
            }
            if (newEntity.getStartDate() == null) {
                newEntity.setStartDate(Instant.now());
            }
            LocalDate startDate = newEntity.getStartDate().atZone(ZoneId.systemDefault()).toLocalDate();
            Duration addDays = Duration.ofDays(3);
        /*if (startDate.getDayOfWeek() == DayOfWeek.FRIDAY) {
            addDays = Duration.ofDays(3);
        }*/
            newEntity.setEndDate(Instant.now().plus(addDays));

            if(newEntity.getAssigner() == null) {
                List<BuyLimit> limit = limitRepository.findByUserIdAndMaliyet(newEntity.getOwner().getId(), CostPlace.OTOBIL.getAttributeValue());
                for (BuyLimit limits : limit) {
                    if (newEntity.getFuelTl().compareTo(limits.getUserTl()) < 0) {
                        newEntity.setAssigner(baseUserService.getUserFullFetched(getCurrentUserId()).get());
                    } else if (newEntity.getFuelTl().compareTo(limits.getUserTl()) > 0 && newEntity.getFuelTl().compareTo(limits.getChiefTl()) < 0) {
                        newEntity.setAssigner(limits.getChief());
                    } else if (newEntity.getFuelTl().compareTo(limits.getChiefTl()) > 0 && newEntity.getFuelTl().compareTo(limits.getManagerTl()) < 0) {
                        newEntity.setAssigner(limits.getManager());
                    } else if (newEntity.getFuelTl().compareTo(limits.getManagerTl()) > 0) {
                        newEntity.setAssigner(limits.getDirector());
                    }
                }
            }


            String original = newEntity.getOwner().getFullName();
            String normalized = Normalizer.normalize(original, Normalizer.Form.NFD);
            String result = normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toLowerCase(Locale.ENGLISH);
            String input2 = result;
            String[] words = input2.split(" ");
            StringBuilder result2 = new StringBuilder();

            for (String word : words) {
                if (!word.isEmpty()) {
                    result2.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1)).append(" ");
                }
            }

            String finalResult = result2.toString().trim();
        postaGuverciniService.SendSmsService("5442088890",finalResult +
            ", " + newEntity.getCurcode() + " cari kodlu musterisine " + DateTimeFormatter.ofPattern("dd-MM-yyyy").withZone(ZoneId.systemDefault()).format(newEntity.getStartDate()) +
            " - " + DateTimeFormatter.ofPattern("dd-MM-yyyy").withZone(ZoneId.systemDefault()).format(newEntity.getEndDate()) +
            " tarihleri icin " + newEntity.getFuelTl().toString() + " TL lik ek limit talep ediyor. Meteor Panel'de onayiniz bekleniyor.");
        mailService.sendEmail(newEntity.getAssigner().getEposta(),
            "MeteorPanel - Ek Limit Talebi Hk.",newEntity.getOwner().getFullName() + ", " +
                newEntity.getCurcode() + " cari kodlu müşterisine " + newEntity.getStartDate().toString() + "  -  " + newEntity.getEndDate() + " tarihleri için " +
                newEntity.getFuelTl().toString() + " TL lik ek limit talep ediyor.",
            false,false);
        } else {
            throw new Exception("Toplam Dbs limitinin en fazla %10'u kadar ek limit talebinde bulunabilirsiniz!");
        }
        return newEntity;
    }

    @Override
    public FuelLimit beforeUpdate(FuelLimit oldEntity, FuelLimit newEntity) throws Exception {
        return newEntity;
    }
}
