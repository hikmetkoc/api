package tr.com.meteor.crm.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.meteor.crm.domain.*;
import tr.com.meteor.crm.service.dto.Table;
import tr.com.meteor.crm.utils.attributevalues.CustomerStatus;
import tr.com.meteor.crm.utils.filter.CbUtils;
import tr.com.meteor.crm.utils.filter.Filter;
import tr.com.meteor.crm.utils.filter.FilterItem;
import tr.com.meteor.crm.utils.request.Column;
import tr.com.meteor.crm.utils.request.ColumnType;
import tr.com.meteor.crm.utils.request.Request;
import tr.com.meteor.crm.repository.CityRepository;
import tr.com.meteor.crm.repository.TargetRepository;
import tr.com.meteor.crm.security.RolesConstants;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.persistence.criteria.*;
import java.math.BigDecimal;
import java.time.*;
import java.time.format.TextStyle;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class ManagerReportService {

    private final BaseUserService baseUserService;
    private final BaseRoleService baseRoleService;
    private final TargetRepository targetRepository;
    private final SummaryOpetSaleService summaryOpetSaleService;
    private final ActivityService activityService;
    private final OpetSaleService opetSaleService;
    private final CustomerService customerService;
    private final ContractService contractService;
    private final CityRepository cityRepository;

    @PersistenceContext
    EntityManager entityManager;

    public ManagerReportService(BaseUserService baseUserService, BaseRoleService baseRoleService,
                                TargetRepository targetRepository, SummaryOpetSaleService summaryOpetSaleService,
                                ActivityService activityService, OpetSaleService opetSaleService,
                                CustomerService customerService, ContractService contractService,
                                CityRepository cityRepository) {
        this.baseUserService = baseUserService;
        this.baseRoleService = baseRoleService;
        this.targetRepository = targetRepository;
        this.summaryOpetSaleService = summaryOpetSaleService;
        this.activityService = activityService;
        this.opetSaleService = opetSaleService;
        this.customerService = customerService;
        this.contractService = contractService;
        this.cityRepository = cityRepository;
    }

    public List<Table> post() throws Exception {
        List<User> regions = baseUserService.getAllUsers().stream()
            .filter(x -> x.getRoles().stream().map(Role::getId).collect(Collectors.toList()).contains(RolesConstants.BOLGE))
            .collect(Collectors.toList());

        List<User> users = new ArrayList<>();
        for (User user : baseUserService.getAllUsers()) {
            boolean ok = true;
            for (Role role : user.getRoles()) {
                if (StringUtils.equalsAny(role.getId(), RolesConstants.ROLE_BACK_OFFICE, RolesConstants.ADMIN,
                    RolesConstants.BOLGE, RolesConstants.YONETIM) && !role.getId().equals(RolesConstants.USER)) {
                    ok = false;
                }
            }
            if (user.getId() < 5) ok = false;
            if (ok) users.add(user);
        }

        Instant todayStart = LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        Instant todayEnd = LocalDate.now().plusDays(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();

        Instant yesterdayStart = LocalDate.now().minusDays(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        Instant yesterdayEnd = LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();

        Instant monthStart = YearMonth.now().atDay(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        Instant monthEnd = YearMonth.now().atEndOfMonth().plusDays(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();

        Instant yearStart = Year.now().atDay(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        Instant yearEnd = Year.now().plusYears(1).atDay(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();

        List<Table> tables = new ArrayList<>();

        tables.add(report1(regions, monthStart, monthEnd));
        tables.add(report2(regions, yesterdayStart, yesterdayEnd));
        tables.add(report3(regions));
        tables.add(report6(regions));
        tables.add(report7(regions, yearStart, yearEnd));
        tables.add(report8(users, monthStart, monthEnd));
        tables.add(report9(monthStart));
        tables.add(report12(regions, monthStart, yearStart));

        return tables;
    }

    private Table report1(List<User> regions, Instant monthStart, Instant monthEnd) {
        Table table = new Table("Bölge Hedef Raporu (R1)");

        table.addRow(new Table.Row(
            new Table.Cell("Bölge"),
            new Table.Cell("Hedef"),
            new Table.Cell("Real"),
            new Table.Cell("Kalan"),
            new Table.Cell("%")
        ));

        Map<Long, Target> userIdTargetMap = new HashMap<>();

        for (Target target : targetRepository.findAllByTermStartGreaterThanEqualAndTermStartLessThanAndOwnerIdIn(monthStart, monthEnd,
            regions.stream().map(User::getId).collect(Collectors.toList()))) {
            if (target.getOwner() == null) continue;
            userIdTargetMap.put(target.getOwner().getId(), target);
        }

        Double targetTotal = 0.0;
        Double realTotal = 0.0;
        for (User region : regions) {
            Table.Row row = new Table.Row();
            row.addCell(new Table.Cell(region.getFullName()));

            if (userIdTargetMap.containsKey(region.getId())) {
                Target target = userIdTargetMap.get(region.getId());
                row.addCell(new Table.Cell(target.getAmount()));
                targetTotal += target.getAmount();
                row.addCell(new Table.Cell(target.getRealizedAmount()));
                realTotal += target.getRealizedAmount();
                if (target.getRealizedAmount() != null && target.getAmount() != null) {
                    row.addCell(new Table.Cell(target.getAmount() - target.getRealizedAmount()));
                    if (target.getAmount() == 0) {
                        row.addCell(new Table.Cell());
                    } else {
                        row.addCell(new Table.Cell(target.getRealizedAmount() / target.getAmount(), Table.Cell.Type.PERCENTAGE));
                    }
                } else {
                    row.addCell(new Table.Cell());
                    row.addCell(new Table.Cell());
                }
            } else {
                row.addCell(new Table.Cell());
                row.addCell(new Table.Cell());
                row.addCell(new Table.Cell());
                row.addCell(new Table.Cell());
            }

            table.addRow(row);
        }
        Table.Row row = new Table.Row();
        row.addCell(new Table.Cell("Meteor"));
        row.addCell(new Table.Cell(targetTotal));
        row.addCell(new Table.Cell(realTotal));
        row.addCell(new Table.Cell(targetTotal - realTotal));
        row.addCell(new Table.Cell(realTotal / targetTotal, Table.Cell.Type.PERCENTAGE));
        table.addRow(row);

        return table;
    }

    private Table report2(List<User> regions, Instant yesterdayStart, Instant yesterdayEnd) throws Exception {

        Map<Long, BigDecimal> regionVolumeMap = new HashMap<>();

        Table table = new Table("Günlük Satış ve Ziyaret Raporu (R2)");

        Table.Row headerRow = new Table.Row();
        headerRow.addCell(new Table.Cell("Bölge"));
        headerRow.addCell(new Table.Cell("MeteorCard Satış"));
        headerRow.addCell(new Table.Cell("Toplam Satış"));
        headerRow.addCell(new Table.Cell("Ziyaret Sayısı"));
        table.addRow(headerRow);

//        BigDecimal col1Total = BigDecimal.ZERO;
        BigDecimal col2Total = BigDecimal.ZERO;
        Long col3Total = 0L;
        for (User region : regions) {
            Table.Row row = new Table.Row();
            row.addCell(new Table.Cell(region.getFullName()));
            row.addCell(new Table.Cell());

            List<Map<String, Object>> salesData = getSalesVolumeForUserFromOpetSale(region, yesterdayStart, yesterdayEnd);
            BigDecimal volume = (BigDecimal) salesData.get(0).getOrDefault("volume", BigDecimal.ZERO);
            if (volume == null) volume = BigDecimal.ZERO;
            row.addCell(new Table.Cell(volume));
            col2Total.add(volume);

            List<Map<String, Object>> activityCount = getActivityCountForUser(region, yesterdayStart, yesterdayEnd);
            row.addCell(new Table.Cell(activityCount.get(0).getOrDefault("count", 0L)));
            col3Total += (Long) activityCount.get(0).getOrDefault("count", 0L);

            table.addRow(row);
        }

        Table.Row row = new Table.Row();
        row.addCell(new Table.Cell("Meteor"));
        row.addCell(new Table.Cell());
        row.addCell(new Table.Cell(col2Total));
        row.addCell(new Table.Cell(col3Total));
        table.addRow(row);

        return table;
    }

    private Table report3(List<User> regions) throws Exception {
        Table table = new Table("Müşteri Durum Raporu (R3)");

        Table.Row headerRow = new Table.Row();
        headerRow.addCell(new Table.Cell("Bölge"));
        headerRow.addCell(new Table.Cell("Aktif"));
        headerRow.addCell(new Table.Cell("Pasif"));
        headerRow.addCell(new Table.Cell("İptal"));
        headerRow.addCell(new Table.Cell("MeteorCard"));
        table.addRow(headerRow);

        int col1Total = 0;
        int col2Total = 0;
        int col3Total = 0;

        for (User region : regions) {
            Table.Row row = new Table.Row();
            row.addCell(new Table.Cell(region.getFullName()));

            List<Map<String, Object>> customerCount = getCustomerCountForUser(region);

            Table.Cell activeCustomerCell = new Table.Cell();
            Table.Cell passiveCustomerCell = new Table.Cell();
            Table.Cell deActiveCustomerCell = new Table.Cell();
            Table.Cell meteorCardCustomerCell = new Table.Cell();

            for (Map<String, Object> record : customerCount) {
                String statusId = (String) record.get("statusId");
                int count = ((Long) record.get("count")).intValue();

                if (statusId.equals(CustomerStatus.MEVCUT_AKTIF.getId())) {
                    activeCustomerCell.setValue(count);
                    col1Total += count;
                } else if (statusId.equals(CustomerStatus.MEVCUT.getId())) {
                    passiveCustomerCell.setValue(count);
                    col2Total += count;
                } else if (statusId.equals(CustomerStatus.MEVCUT_PASIF.getId())) {
                    deActiveCustomerCell.setValue(count);
                    col3Total += count;
                }
            }

            row.addCell(activeCustomerCell);
            row.addCell(passiveCustomerCell);
            row.addCell(deActiveCustomerCell);
            row.addCell(meteorCardCustomerCell);

            table.addRow(row);
        }

        Table.Row row = new Table.Row();
        row.addCell(new Table.Cell("Meteor"));
        row.addCell(new Table.Cell(col1Total));
        row.addCell(new Table.Cell(col2Total));
        row.addCell(new Table.Cell(col3Total));
        row.addCell(new Table.Cell());
        table.addRow(row);

        return table;
    }

    private Table report6(List<User> regions) throws Exception {
        Table table = new Table("Teminatı Olup Yakıt Almayanlar Raporu (R6)");

        Table.Row headerRow = new Table.Row();
        headerRow.addCell(new Table.Cell("Bölge"));
        headerRow.addCell(new Table.Cell("Aktif Sözleşme Sayısı"));
        headerRow.addCell(new Table.Cell("Yakıt Almayan"));
        table.addRow(headerRow);

        Instant start = YearMonth.now().minusMonths(3).atDay(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        Instant end = YearMonth.now().atDay(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();

        int col1Total = 0;
        int col2Total = 0;

        for (User region : regions) {
            Table.Row row = new Table.Row();
            row.addCell(new Table.Cell(region.getFullName()));

            List<Long> hierarchicalUserIds = baseUserService.getHierarchicalUsersOnlyDownwards(region).stream()
                .map(User::getId).collect(Collectors.toList());

            Set<UUID> activeContractCustomerIds = getCustomerIdsHaveActiveContract(hierarchicalUserIds).stream()
                .map(x -> (UUID) x.get("customerId")).collect(Collectors.toSet());
            row.addCell(new Table.Cell(activeContractCustomerIds.size()));
            col1Total += activeContractCustomerIds.size();

            int activeCustomerCount = 0;
            if (!activeContractCustomerIds.isEmpty()) {
                activeCustomerCount = getSalesVolumeForCustomers(new ArrayList<>(activeContractCustomerIds), start, end).size();
            }
            row.addCell(new Table.Cell(activeContractCustomerIds.size() - activeCustomerCount));
            col2Total += activeContractCustomerIds.size() - activeCustomerCount;

            table.addRow(row);
        }

        Table.Row row = new Table.Row();
        row.addCell(new Table.Cell("Meteor"));
        row.addCell(new Table.Cell(col1Total));
        row.addCell(new Table.Cell(col2Total));
        table.addRow(row);

        return table;
    }

    private Table report7(List<User> regions, Instant yearStart, Instant yearEnd) throws Exception {
        Table table = new Table("Yıllık Satış Raporu (R7) (G: Gerçekleşen)");

        Table.Row headerRow = new Table.Row();
        headerRow.addCell(new Table.Cell("Ay"));
        for (User region : regions) {
            headerRow.addCell(new Table.Cell(region.getFullName()));
        }
        headerRow.addCell(new Table.Cell("Meteor"));
        table.addRow(headerRow);


        Map<Long, Map<Instant, Target>> userIdTargetsMap = new HashMap<>();

        for (Target target : targetRepository.findAllByTermStartGreaterThanEqualAndTermStartLessThanAndOwnerIdIn(yearStart, yearEnd,
            regions.stream().map(User::getId).collect(Collectors.toList()))) {
            if (target.getOwner() == null) continue;
            Map<Instant, Target> userTargets = userIdTargetsMap.getOrDefault(target.getOwner().getId(), new HashMap<>());
            userTargets.put(target.getTermStart(), target);
            userIdTargetsMap.put(target.getOwner().getId(), userTargets);
        }

        Instant tempMonthStart = yearStart;

        for (Month month : Month.values()) {
            Table.Row row1 = new Table.Row();
            Table.Row row2 = new Table.Row();
            row1.addCell(new Table.Cell(month.getDisplayName(TextStyle.FULL, new Locale("tr", "TR"))));
            row2.addCell(new Table.Cell(month.getDisplayName(TextStyle.FULL, new Locale("tr", "TR")) + " G"));

            Double row1total = 0.0;
            BigDecimal row2total = BigDecimal.ZERO;
            for (User region : regions) {

                List<Map<String, Object>> salesData = getSalesVolumeForUser(region, yearStart, yearEnd, true);

                Map<Object, Object> sales = salesData.stream().collect(Collectors.toMap(x -> x.get("saleEnd"), x -> x.get("volume")));

                Map<Instant, Target> userTargets = userIdTargetsMap.getOrDefault(region.getId(), new HashMap<>());

                BigDecimal volume = BigDecimal.ZERO;

                if (sales.containsKey(tempMonthStart)) {
                    volume = (BigDecimal) sales.get(tempMonthStart);
                    volume = volume.multiply(BigDecimal.valueOf(0.001d));
                }

                row2.addCell(new Table.Cell(volume, Table.Cell.Type.INTEGER));
                row2total = row2total.add(volume);

                if (userTargets.containsKey(tempMonthStart) && userTargets.get(tempMonthStart).getAmount() != null) {
                    row1.addCell(new Table.Cell(userTargets.get(tempMonthStart).getAmount() * 0.001d, Table.Cell.Type.INTEGER));
                    row1total += userTargets.get(tempMonthStart).getAmount() * 0.001d;
                } else {
                    row1.addCell(new Table.Cell("-"));
                }

            }
            row1.addCell(new Table.Cell(row1total, Table.Cell.Type.INTEGER));
            row2.addCell(new Table.Cell(row2total, Table.Cell.Type.INTEGER));

            tempMonthStart = LocalDateTime.ofInstant(tempMonthStart, ZoneId.systemDefault()).plusMonths(1).atZone(ZoneId.systemDefault()).toInstant();

            table.addRow(row1);
            table.addRow(row2);
        }

        return table;
    }

    private Table report8(List<User> users, Instant monthStart, Instant monthEnd) throws Exception {
        Table table = new Table("Kullanıcı Bazlı Satış Raporu (R8)");

        Table.Row headerRow = new Table.Row();
        headerRow.addCell(new Table.Cell("Bölge"));
        headerRow.addCell(new Table.Cell("Toplam Litre"));
        table.addRow(headerRow);

        Map<User, BigDecimal> userVolumeMap = new HashMap<>();

        for (User region : users.stream().sorted(Comparator.comparing(User::getFullName, String::compareTo).reversed())
            .collect(Collectors.toList())) {

            BigDecimal volume = (BigDecimal) getSalesVolumeForUser(region, monthStart, monthEnd, false)
                .get(0).get("volume");

            if (volume == null) {
                volume = BigDecimal.ZERO;
            }
            userVolumeMap.put(region, volume);
        }

        List<Map.Entry<User, BigDecimal>> entries = userVolumeMap.entrySet().stream()
            .sorted((t1, t2) -> t2.getValue().compareTo(t1.getValue())).collect(Collectors.toList());

        for (Map.Entry<User, BigDecimal> entry : entries) {
            Table.Row row = new Table.Row();
            row.addCell(new Table.Cell(entry.getKey().getFullName()));
            row.addCell(new Table.Cell(entry.getValue()));

            table.addRow(row);
        }

        return table;
    }

    private Table report9(Instant monthStart) throws Exception {
        Table table = new Table("Şehir Bazlı Satış Raporu (R9)");

        Table.Row headerRow = new Table.Row();
        headerRow.addCell(new Table.Cell("Sıra"));
        headerRow.addCell(new Table.Cell("Şehir"));
        headerRow.addCell(new Table.Cell("Müşteri Sayısı"));
        headerRow.addCell(new Table.Cell("Toplam Litre"));
        table.addRow(headerRow);

        Map<String, City> cities = cityRepository.findAll().stream()
            .collect(Collectors.toMap(x -> x.getId().toString(), Function.identity()));

        Map<String, Object[]> result = getConsumptionsByCity(monthStart);

        int number = 1;

        for (String cityId : result.keySet()) {
            Table.Row row = new Table.Row();
            row.addCell(new Table.Cell(number++));

            if (cityId == null || cityId.equals("null")) {
                row.addCell(new Table.Cell("Adresi Olmayan"));
                row.addCell(new Table.Cell(result.get(cityId)[2]));
                row.addCell(new Table.Cell(result.get(cityId)[3]));
            } else {
                row.addCell(new Table.Cell(cities.get(cityId).getName()));
                row.addCell(new Table.Cell(result.get(cityId)[2]));
                row.addCell(new Table.Cell(result.get(cityId)[3]));
            }

            table.addRow(row);
        }

        return table;
    }

    private Table report12(List<User> regions, Instant monthStart, Instant yearStart) {
        Table table = new Table("Ürün Bazlı Satış Raporu (R12)");

        Table.Row headerRow = new Table.Row();
        headerRow.addCell(new Table.Cell("Ürün"));
        for (User region : regions) {
            headerRow.addCell(new Table.Cell(region.getFullName()));
        }
        headerRow.addCell(new Table.Cell("Meteor"));
        table.addRow(headerRow);

        Set<String> productNames = new HashSet<>();
        for (User region : regions) {
            for (Map<String, Object> product : getRegionConsumptionGroupByProduct(region, monthStart, yearStart)) {
                productNames.add((String) product.get("productName"));
            }
        }

        productNames.forEach(x -> {
            Table.Row row1 = new Table.Row();
            Table.Row row2 = new Table.Row();
            row1.addCell(new Table.Cell(x + " Aylık"));
            row2.addCell(new Table.Cell(x + " Yıllık"));

            boolean regionFlag;

            BigDecimal row1Total = BigDecimal.ZERO;
            BigDecimal row2Total = BigDecimal.ZERO;

            for (User region : regions) {
                regionFlag = false;
                for (Map<String, Object> product : getRegionConsumptionGroupByProduct(region, monthStart, yearStart)) {
                    if(x.equals(product.get("productName").toString())) {
                        row1.addCell(new Table.Cell(product.get("monthSum")));
                        row1Total = row1Total.add((BigDecimal) product.get("monthSum"));
                        row2.addCell(new Table.Cell(product.get("yearSum")));
                        row2Total = row2Total.add((BigDecimal) product.get("yearSum"));
                        regionFlag = true;
                    }
                }
                if(!regionFlag) {
                    row1.addCell(new Table.Cell("-"));
                    row2.addCell(new Table.Cell("-"));
                }
            }
            row1.addCell(new Table.Cell(row1Total));
            row2.addCell(new Table.Cell(row2Total));
            table.addRow(row1);
            table.addRow(row2);
        });

        return table;
    }

    private List<Map<String, Object>> getRegionConsumptionGroupByProduct(User region, Instant monthStart, Instant yearStart) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<Tuple> query = cb.createTupleQuery();

        Root<SummaryOpetSale> root = query.from(SummaryOpetSale.class);
        Path<Instant> saleEnd = root.get("saleEnd");
        Path<BigDecimal> volume = root.get("volume");
        Path<String> productName = root.get("productName");
        Path<UUID> userId = root.get("user");

        Expression monthSum = cb.selectCase().when(cb.greaterThanOrEqualTo(saleEnd, monthStart), volume).otherwise(BigDecimal.ZERO);

        query.multiselect(productName.alias("productName"), cb.sum(monthSum).alias("monthSum"), cb.sum(volume).alias("yearSum"));

        List<Long> hierarchicalUserIds = baseUserService.getHierarchicalUsersOnlyDownwards(region).stream()
            .map(User::getId).collect(Collectors.toList());

        query.where(cb.and(cb.greaterThanOrEqualTo(saleEnd, yearStart), userId.in(hierarchicalUserIds)));

        query.groupBy(productName);

        return CbUtils.tupleListToMap(entityManager.createQuery(query).getResultList());
    }

    private List<Map<String, Object>> getSalesVolumeForUser(User user, Instant start, Instant end, boolean groupBySaleEnd) throws Exception {
        Filter filter = Filter.And(
            Filter.FilterItem("saleEnd", FilterItem.Operator.GREATER_OR_EQUAL_THAN, start),
            Filter.FilterItem("saleEnd", FilterItem.Operator.LESS_THAN, end)
        );

        if (user != null) {
            List<Long> hierarchicalUserIds = baseUserService.getHierarchicalUsersOnlyDownwards(user).stream()
                .map(User::getId).collect(Collectors.toList());
            filter.getFilterList().add(Filter.FilterItem("user.id", FilterItem.Operator.IN, hierarchicalUserIds));
        }

        List<Column> columns = new ArrayList<>();

        if (groupBySaleEnd) {
            columns.add(new Column().name("saleEnd").title("saleEnd"));
        }

        columns.add(new Column().name("volume").title("volume").columnType(ColumnType.SUM));

        return summaryOpetSaleService
            .getDataWithColumnSelection(
                user,
                Request.build().page(0).size(Integer.MAX_VALUE)
                    .filter(filter)
                    .columns(columns),
                false)
            .getBody();
    }

    private List<Map<String, Object>> getSalesVolumeForUserFromOpetSale(User user, Instant start, Instant end) throws Exception {
        Filter filter = Filter.And(
            Filter.FilterItem("saleEnd", FilterItem.Operator.GREATER_OR_EQUAL_THAN, start),
            Filter.FilterItem("saleEnd", FilterItem.Operator.LESS_THAN, end)
        );

        if (user != null) {
            List<Long> hierarchicalUserIds = baseUserService.getHierarchicalUsersOnlyDownwards(user).stream()
                .map(User::getId).collect(Collectors.toList());
            filter.getFilterList().add(Filter.FilterItem("user.id", FilterItem.Operator.IN, hierarchicalUserIds));
        }

        return opetSaleService
            .getDataWithColumnSelection(
                user,
                Request.build().page(0).size(Integer.MAX_VALUE)
                    .filter(filter)
                    .addColumn(new Column().name("volume").title("volume").columnType(ColumnType.SUM)),
                false)
            .getBody();
    }

    private List<Map<String, Object>> getActivityCountForUser(User user, Instant start, Instant end) throws Exception {
        Filter filter = Filter.And(
            Filter.FilterItem("createdDate", FilterItem.Operator.GREATER_OR_EQUAL_THAN, start),
            Filter.FilterItem("createdDate", FilterItem.Operator.LESS_THAN, end)
        );

        if (user != null) {
            List<Long> hierarchicalUserIds = baseUserService.getHierarchicalUsersOnlyDownwards(user).stream()
                .map(User::getId).collect(Collectors.toList());
            filter.getFilterList().add(Filter.FilterItem("createdBy.id", FilterItem.Operator.IN, hierarchicalUserIds));
        }

        return activityService
            .getDataWithColumnSelection(
                user,
                Request.build().page(0).size(Integer.MAX_VALUE)
                    .filter(filter)
                    .addColumn(new Column().name("id").title("count").columnType(ColumnType.COUNT)),
                false)
            .getBody();
    }

    private List<Map<String, Object>> getCustomerCountForUser(User user) throws Exception {
        Filter filter = null;

        if (user != null) {
            List<Long> hierarchicalUserIds = baseUserService.getHierarchicalUsersOnlyDownwards(user).stream()
                .map(User::getId).collect(Collectors.toList());
            filter = Filter.FilterItem("owner.id", FilterItem.Operator.IN, hierarchicalUserIds);
        }

        return customerService
            .getDataWithColumnSelection(
                user,
                Request.build().page(0).size(Integer.MAX_VALUE)
                    .filter(filter)
                    .addColumn(new Column().name("status.id").title("statusId"))
                    .addColumn(new Column().name("id").title("count").columnType(ColumnType.COUNT)),
                false)
            .getBody();
    }

    private List<Map<String, Object>> getCustomerIdsForUser(User user) throws Exception {
        Filter filter = null;

        if (user != null) {
            List<Long> hierarchicalUserIds = baseUserService.getHierarchicalUsersOnlyDownwards(user).stream()
                .map(User::getId).collect(Collectors.toList());
            filter = Filter.FilterItem("owner.id", FilterItem.Operator.IN, hierarchicalUserIds);
        }

        return customerService
            .getDataWithColumnSelection(
                user,
                Request.build().page(0).size(Integer.MAX_VALUE)
                    .filter(filter)
                    .addColumn(new Column().name("id").title("id")),
                false)
            .getBody();
    }

    private List<Map<String, Object>> getCustomerIdsHaveActiveContract(List<Long> hierarchicalUserIds) throws Exception {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> query = cb.createTupleQuery();

        Root root = query.from(Contract.class);

        Path endDate = root.get("endDate");

        Join customerJoin = root.join("customer");
        Path customerId = customerJoin.get("id");

        Join customerOwnerJoin = customerJoin.join("owner");
        Path customerOwnerId = customerOwnerJoin.get("id");

        query.where(cb.and(cb.greaterThanOrEqualTo(endDate, Instant.now()),
            customerOwnerId.in(hierarchicalUserIds)));

        query.multiselect(customerId.alias("customerId"));

        return CbUtils.tupleListToMap(entityManager.createQuery(query).getResultList());
    }

    private List<Map<String, Object>> getSalesVolumeForCustomers(List<UUID> customerIds, Instant start, Instant end) throws Exception {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> query = cb.createTupleQuery();

        Root root = query.from(OpetSale.class);

        Path saleEnd = root.get("saleEnd");
        Path volume = root.get("volume");

        Join customerJoin = root.join("customer", JoinType.LEFT);
        Path customerId = customerJoin.get("id");

        Expression volumeSum = cb.sum(volume);

        query.where(
            cb.and(
                cb.greaterThanOrEqualTo(saleEnd, start),
                cb.lessThan(saleEnd, end),
                customerId.in(customerIds)
            )
        );

        query.groupBy(customerId);

        query.multiselect(customerId.alias("customerId"), volumeSum.alias("volume"));

        return CbUtils.tupleListToMap(entityManager.createQuery(query).getResultList());
    }

    private Map<String, Object[]> getConsumptionsByCity(Instant monthStart) {
        String queryString =
            "select c2.name cityName, cast(c2.id as varchar), count(distinct os.fleet_code) customerCount, sum(os.volume) volume from customer c " +
                "left outer join address a on c.id = a.customer_id " +
                "left outer join city c2 on a.city_id = c2.id " +
                "left outer join opet_sale os on c.fleet_code = os.fleet_code " +
                "where c.fleet_code IS NOT NULL and os.sale_end >= :saleEndGreaterOrEqual " +
                "group by c2.name, c2.id " +
                "order by volume desc ";

        Query query = entityManager.createNativeQuery(queryString);
        query.setParameter("saleEndGreaterOrEqual", monthStart);

        List<Object[]> result = query.getResultList();

        Map<String, Object[]> linkedHashMap = new LinkedHashMap<>();
        for (Object[] o : result) {
            linkedHashMap.put(String.valueOf(o[1]), o);
        }

        return linkedHashMap;
    }
}
