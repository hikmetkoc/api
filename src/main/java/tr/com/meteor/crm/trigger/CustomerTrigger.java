package tr.com.meteor.crm.trigger;

import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import tr.com.meteor.crm.domain.*;
import tr.com.meteor.crm.repository.CustomerRepository;
import tr.com.meteor.crm.repository.OpetSaleRepository;
import tr.com.meteor.crm.repository.RoleRepository;
import tr.com.meteor.crm.repository.SummaryOpetSaleRepository;
import tr.com.meteor.crm.service.BaseConfigurationService;
import tr.com.meteor.crm.service.BaseUserService;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component(CustomerTrigger.QUALIFIER)
public class CustomerTrigger extends Trigger<Customer, UUID, CustomerRepository> {
    final static String QUALIFIER = "CustomerTrigger";

    private final SummaryOpetSaleRepository summaryOpetSaleRepository;
    private final OpetSaleRepository opetSaleRepository;

    private final RoleRepository roleRepository;
    private final CustomerRepository customerRepository;
    public CustomerTrigger(CacheManager cacheManager, CustomerRepository customerRepository,
                           BaseUserService baseUserService, BaseConfigurationService baseConfigurationService,
                           SummaryOpetSaleRepository summaryOpetSaleRepository, OpetSaleRepository opetSaleRepository,
                           RoleRepository roleRepository) {
        super(cacheManager, Customer.class, CustomerTrigger.class, customerRepository, baseUserService, baseConfigurationService);
        this.summaryOpetSaleRepository = summaryOpetSaleRepository;
        this.opetSaleRepository = opetSaleRepository;
        this.roleRepository = roleRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer beforeInsert(@NotNull Customer newEntity) throws Exception {
        if (newEntity.getOwner() == null) {
            newEntity.setOwner(getCurrentUser());
        }

        if (customerRepository.existsByTaxNumber(newEntity.getTaxNumber())) {
            throw new Exception("Bu vergi numarasına kayıtlı bir tedarikçi zaten kayıtlı!");
        }

        if(newEntity.getTaxNumber().length()!=11){
            throw new Exception("Lütfen 11 haneli vergili numarası giriniz. Eğer 10 haneli ise başına 0 koyarak girebilirsiniz.");
        }


        return newEntity;
    }

    @Override
    public Customer afterInsert(@NotNull Customer newEntity) throws Exception {
        newEntity.setCommercialTitle(newEntity.getName());
        return newEntity;
    }

    @Override
    public Customer beforeUpdate(@NotNull Customer oldEntity, @NotNull Customer newEntity) throws Exception {
        if (customerRepository.existsByTaxNumberAndIdNot(newEntity.getTaxNumber(), newEntity.getId())) {
            throw new Exception("Bu vergi numarasına kayıtlı bir tedarikçi zaten kayıtlı!");
        } // todo: Buraya bak.
        if(newEntity.getTaxNumber().length()!=11){
            throw new Exception("Lütfen 11 haneli vergili numarası giriniz. Eğer 10 haneli ise başına 0 koyarak girebilirsiniz.");
        }
        newEntity.setCommercialTitle(newEntity.getName());
        return newEntity;
    }

    @Override
    public Customer afterUpdate(@NotNull Customer oldEntity, @NotNull Customer newEntity) {
        return newEntity;
    }

    /*private void checkFieldChanges(@NotNull Customer oldEntity, @NotNull Customer newEntity, boolean isInsert) throws Exception {
        if (isInsert) {
            boolean haveBackOfficeOperation = isUserHaveOperation(getCurrentUserId(), Operations.BACK_OFFICE.getId());
            if (!haveBackOfficeOperation
                && (newEntity.getStatus() == null || !newEntity.getStatus().getId().equals(CustomerStatus.YENI.getId()))) {
                throw new Exception("Müşteri oluşturulurken müşteri durumu Yeni seçilmelidir.");
            }

            if (!haveBackOfficeOperation && newEntity.getFleetCode() != null) {
                throw new Exception("Filo Kodu alanı sadece BackOffice operasyonu ile doldurulabilir.");
            }

            if (!haveBackOfficeOperation && newEntity.getOwner() != null && !newEntity.getOwner().getId().equals(getCurrentUserId())) {
                throw new Exception("Müşteri sahibi olarak sadece kendi kullanıcınızı seebilirsiniz.");
            }
        } else {
            boolean haveBackOfficeOperation = isUserHaveOperation(getCurrentUserId(), Operations.BACK_OFFICE.getId());

            if (!haveBackOfficeOperation && Utils.isChanged(oldEntity.getStatus(), newEntity.getStatus())) {
                throw new Exception("Müşteri durumu sadece BackOffice operasyonu ile değiştirilebilir.");
            }

            if (!haveBackOfficeOperation && Utils.isChanged(oldEntity.getFleetCode(), newEntity.getFleetCode())) {
                throw new Exception("Filo Kodu sadece BackOffice operasyonu ile değiştirilebilir.");
            }

            if (!haveBackOfficeOperation && Utils.isChanged(oldEntity.getOwner(), newEntity.getOwner())) {
                throw new Exception("Sahip Bilgisi sadece BackOffice operasyonu ile değiştirilebilir.");
            }
        }
    }*/

    public boolean isUserHaveOperation(Long userId, String operationId) {
        List<String> userRoleIds = baseUserService.getUserFullFetched(userId).get()
            .getRoles().stream().map(Role::getId).collect(Collectors.toList());

        return roleRepository.findAll().stream()
            .filter(x -> userRoleIds.contains(x.getId()))
            .anyMatch(role -> role.getOperations().stream().anyMatch(x -> x.getId().equals(operationId)));
    }

    private void checkFleetCodeDuplication(Customer newEntity) throws Exception {
        /*if (newEntity.getFleetCode() == null) return;

        List<Customer> customers = repository.findAllByFleetCodeIn(Collections.singleton(newEntity.getFleetCode()));
*/
        /*if (!customers.isEmpty() && customers.stream().anyMatch(x -> !x.getId().equals(newEntity.getId()))) {
            throw new Exception("Bu müşteri numarası ile kayıtlı müşteri var.");
        }*/
    }

    /*@Async
    public void updateSummaries(Customer customer, Integer oldFleetCode, Integer newFleetCode) {
        if (oldFleetCode != null) {
            unlinkSummariesAndSales(oldFleetCode);
        }

        if (newFleetCode != null) {
            unlinkSummariesAndSales(newFleetCode);
            linkSummariesAndSalesToCustomer(customer.getId());
        }
    }*/

    /*private void unlinkSummariesAndSales(Integer fleetCode) {
        List<SummaryOpetSale> summaryOpetSales = summaryOpetSaleRepository.findAllByFleetCodeEquals(fleetCode);
        List<OpetSale> opetSales = opetSaleRepository.findAllByFleetCodeEquals(fleetCode);

        if (!summaryOpetSales.isEmpty()) {
            for (SummaryOpetSale summaryOpetSale : summaryOpetSales) {
                summaryOpetSale.setCustomer(null);
                summaryOpetSale.setUser(null);
            }

            summaryOpetSaleRepository.saveAll(summaryOpetSales);
        }

        if (!opetSales.isEmpty()) {
            for (OpetSale opetSale : opetSales) {
                opetSale.setCustomer(null);
                opetSale.setUser(null);
            }

            opetSaleRepository.saveAll(opetSales);
        }
    }
*/
    /*private void linkSummariesAndSalesToCustomer(UUID customerId) {
        Customer customer = repository.findById(customerId).get();
        //List<SummaryOpetSale> summaryOpetSales = summaryOpetSaleRepository.findAllByFleetCodeEquals(customer.getFleetCode());
        //List<OpetSale> opetSales = opetSaleRepository.findAllByFleetCodeEquals(customer.getFleetCode());

        if (!summaryOpetSales.isEmpty()) {
            for (SummaryOpetSale summaryOpetSale : summaryOpetSales) {
                summaryOpetSale.setCustomer(customer);
                if (customer.getOwner() != null) {
                    summaryOpetSale.setUser(customer.getOwner());
                }
            }

            summaryOpetSaleRepository.saveAll(summaryOpetSales);
        }

        if (!opetSales.isEmpty()) {
            for (OpetSale opetSale : opetSales) {
                opetSale.setCustomer(customer);
                if (customer.getOwner() != null) {
                    opetSale.setUser(customer.getOwner());
                }
            }

            opetSaleRepository.saveAll(opetSales);
        }
    }*/
}
