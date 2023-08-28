package tr.com.meteor.crm.service;

import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.meteor.crm.service.dto.NearestCustomersInputDTO;
import tr.com.meteor.crm.service.dto.NearestCustomersOutputDTO;
import tr.com.meteor.crm.utils.export.ExportUtils;
import tr.com.meteor.crm.utils.filter.Filter;
import tr.com.meteor.crm.utils.filter.FilterItem;
import tr.com.meteor.crm.utils.request.Request;
import tr.com.meteor.crm.domain.Customer;
import tr.com.meteor.crm.repository.CustomerRepository;
import tr.com.meteor.crm.repository.SummaryOpetSaleRepository;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class CustomerService extends GenericIdNameAuditingEntityService<Customer, UUID, CustomerRepository> {

    private final AddressService addressService;
    private final SummaryOpetSaleRepository summaryOpetSaleRepository;

    public CustomerService(BaseUserService baseUserService, BaseRoleService baseRoleService,
                           BasePermissionService basePermissionService, BaseFileDescriptorService baseFileDescriptorService,
                           BaseConfigurationService baseConfigurationService,
                           CustomerRepository repository, AddressService addressService,
                           SummaryOpetSaleRepository summaryOpetSaleRepository) {
        super(baseUserService, baseRoleService, basePermissionService, baseFileDescriptorService, baseConfigurationService,
            Customer.class, repository);
        this.addressService = addressService;
        this.summaryOpetSaleRepository = summaryOpetSaleRepository;
    }

    public ResponseEntity<Resource> getImportTemplate() throws Exception {
        return ResponseEntity.ok()
            .header("Content-Disposition", "attachment; filename=" + entityClass.getSimpleName() + ".xlsx")
            .contentType(MediaType.parseMediaType("application/vnd.ms-excel;charset=UTF-8"))
            .body(ExportUtils.generateImportTemplate(entityClass));
    }

    public List<NearestCustomersOutputDTO> getNearestCustomers(NearestCustomersInputDTO nearestCustomersInputDTO) throws Exception {
        Filter filter = Filter.And(
            Filter.FilterItem("latitude", FilterItem.Operator.GREATER_OR_EQUAL_THAN, nearestCustomersInputDTO.getSouthWest().getLat()),
            Filter.FilterItem("latitude", FilterItem.Operator.LESS_OR_EQUAL_THAN, nearestCustomersInputDTO.getNorthEast().getLat()),
            Filter.FilterItem("longitude", FilterItem.Operator.GREATER_OR_EQUAL_THAN, nearestCustomersInputDTO.getSouthWest().getLng()),
            Filter.FilterItem("longitude", FilterItem.Operator.LESS_OR_EQUAL_THAN, nearestCustomersInputDTO.getNorthEast().getLng())
        );

        return Objects.requireNonNull(addressService.getData(getCurrentUser(), Request.build().filter(filter))
            .getBody()).stream().filter(x -> x.getCustomer() != null)
            .map(x -> new NearestCustomersOutputDTO()
                .setId(x.getCustomer().getId())
                .setName(x.getCustomer().getCommercialTitle())
                .setEmail(x.getCustomer().getEmail())
                .setPhone(x.getCustomer().getPhone())
                .setLatitude(x.getLatitude())
                .setLongitude(x.getLongitude())
                .setOwner(x.getCustomer().getOwner())
            ).collect(Collectors.toList());
    }
}
