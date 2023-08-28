package tr.com.meteor.crm.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.meteor.crm.utils.attributevalues.ContractStatus;
import tr.com.meteor.crm.utils.export.ExportUtils;
import tr.com.meteor.crm.utils.filter.CbUtils;
import tr.com.meteor.crm.utils.operations.Operations;
import tr.com.meteor.crm.config.postgresql.timestamp.PostgreSqlTimeStampDiffFuncUnit;
import tr.com.meteor.crm.config.postgresql.timestamp.PostgreSqlTimestampDiffFunction;
import tr.com.meteor.crm.domain.Contract;
import tr.com.meteor.crm.domain.User;
import tr.com.meteor.crm.repository.ContractRepository;

import javax.persistence.Tuple;
import javax.persistence.criteria.*;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class ContractService extends GenericIdNameAuditingEntityService<Contract, UUID, ContractRepository> {

    private final MailService mailService;

    public ContractService(BaseUserService baseUserService, BaseRoleService baseRoleService,
                           BasePermissionService basePermissionService, BaseFileDescriptorService baseFileDescriptorService,
                           BaseConfigurationService baseConfigurationService,
                           ContractRepository repository, MailService mailService) {
        super(baseUserService, baseRoleService, basePermissionService, baseFileDescriptorService, baseConfigurationService,
            Contract.class, repository);
        this.mailService = mailService;
    }

    //@Scheduled(cron = "* * * * * ?")
    public void contractReport() throws Exception {
        /*CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> query = criteriaBuilder.createTupleQuery();

        Root root = query.from(entityClass);

        Path startDate = root.get("startDate");

        Join customerJoin = root.join("customer", JoinType.LEFT);
        Join customerOwnerJoin = customerJoin.join("owner", JoinType.LEFT);
        Join statusJoin = root.join("status", JoinType.LEFT);

        Path statusId = statusJoin.get("id");

        Selection customerName = customerJoin.get("name").alias("Müşteri Adı");
        Selection customerId = customerJoin.get("id").alias("Müşteri Id");
        Path customerOwnerId = customerOwnerJoin.get("id");
        Path ownerFirstName = customerOwnerJoin.get("firstName");
        Path ownerLastName = customerOwnerJoin.get("lastName");
        Selection fullName = criteriaBuilder.concat(ownerFirstName, criteriaBuilder.concat(" ", ownerLastName));

        Expression diff = PostgreSqlTimestampDiffFunction.TimestampDiff(criteriaBuilder,
            PostgreSqlTimeStampDiffFuncUnit.Unit.DAY, criteriaBuilder.currentTimestamp(), startDate);

        final String diffHeader = "Kaç Gündür Teminat Sürecinde";
        final String customerOwnerIdHeader = "Müşteri Sahibi Id";

        query.multiselect(customerName, fullName.alias("Müşteri Sahibi"), root.get("name").alias("Sözleşme"),
            startDate.alias("Sözleşme Başlangıç Tarihi"), diff.alias(diffHeader), customerOwnerId.alias(customerOwnerIdHeader));

        Instant filter = Instant.now().minus(10, ChronoUnit.DAYS);

        Predicate dateFilter = criteriaBuilder.lessThanOrEqualTo(startDate, filter);
        //Predicate statusFilter = criteriaBuilder.equal(statusId, ContractStatus.TEMINAT_SURECINDE);

        //query.where(criteriaBuilder.and(dateFilter, statusFilter));

        List<Tuple> list = entityManager.createQuery(query).getResultList();

        List<Map<String, Object>> s = CbUtils.tupleListToMap(list);

        Map<Long, ArrayList<Map<String, Object>>> ownerIdContractMap = new LinkedHashMap<>();

        s.forEach(x -> {
            ArrayList<Map<String, Object>> old = new ArrayList<>();

            if (ownerIdContractMap.containsKey(x.get(customerOwnerIdHeader))) {
                old = ownerIdContractMap.get(x.get(customerOwnerIdHeader));
            }

            old.add(x);
            ownerIdContractMap.put((Long) x.get(customerOwnerIdHeader), old);
        });

        Map<User, ArrayList<Map<String, Object>>> reportOwnerContractMap = new LinkedHashMap<>();

        for (Long ownerId : ownerIdContractMap.keySet()) {
            User user = findManager(ownerId);
            ArrayList<Map<String, Object>> contractList = new ArrayList<>();

            if (reportOwnerContractMap.containsKey(user)) {
                contractList = reportOwnerContractMap.get(user);
            }

            contractList.addAll(ownerIdContractMap.get(ownerId));

            reportOwnerContractMap.put(user, contractList);
        }

        for (User user : reportOwnerContractMap.keySet()) {
            if (user == null) continue;

            List<Map<String, Object>> uList = reportOwnerContractMap.get(user).stream()
                .peek(x -> x.remove(customerOwnerIdHeader))
                .sorted((t0, t1) -> {
                    if (t0.containsKey(diffHeader) && t1.containsKey(diffHeader)) {
                        Integer i0 = (Integer) t0.get(diffHeader);
                        Integer i1 = (Integer) t1.get(diffHeader);

                        return i0 < i1 ? 1 : -1;
                    }

                    return 0;
                }).collect(Collectors.toList());

            mailService.sendWithAttachment(Collections.singleton(user.getEmail()), null, null,
                "10 Günden Fazla Teminat Sürecinde Bekleyen Sözleşmeler",
                "İlgili rapor ektedir.", true, false,
                "Teminat Surecindekiler.xlsx", ExportUtils.toExcel(uList));
        }*/
    }

    private User findManager(Long ownerId) {
        User currentUser = baseUserService.getUserFullFetched(ownerId).get();
        List<User> hierarchicalUsers = baseUserService.getHierarchicalUsers(currentUser);

        for (User user : hierarchicalUsers) {
            if (isUserHaveOperation(user.getId(), Operations.RAPOR_AlICISI.getId())) return user;
        }

        return null;
    }
}
