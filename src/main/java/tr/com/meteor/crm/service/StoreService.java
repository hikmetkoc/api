package tr.com.meteor.crm.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.meteor.crm.domain.Buy;
import tr.com.meteor.crm.domain.Store;
import tr.com.meteor.crm.domain.User;
import tr.com.meteor.crm.repository.BuyRepository;
import tr.com.meteor.crm.repository.ContProductRepository;
import tr.com.meteor.crm.repository.StoreRepository;
import tr.com.meteor.crm.utils.filter.Filter;
import tr.com.meteor.crm.utils.filter.FilterItem;
import tr.com.meteor.crm.utils.request.Request;

import java.io.ByteArrayOutputStream;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static tr.com.meteor.crm.utils.ExcelUtils.addBorderToCell;

@Service
@Transactional(rollbackFor = Exception.class)
public class StoreService extends GenericIdNameAuditingEntityService<Store, UUID, StoreRepository> {

    private BuyRepository buyRepository;
    public StoreService(BaseUserService baseUserService, BaseRoleService baseRoleService,
                        BasePermissionService basePermissionService, BaseFileDescriptorService baseFileDescriptorService,
                        BaseConfigurationService baseConfigurationService,
                        StoreRepository repository) {
        super(baseUserService, baseRoleService, basePermissionService, baseFileDescriptorService, baseConfigurationService,
            Store.class, repository);
        this.buyRepository = buyRepository;
    }

    public List<Store> saveWeekly(List<Store> Stores) throws Exception {
        List<Store> BuyList = new ArrayList<>();
        for (Store Store : Stores) {
            if (!StringUtils.isBlank(Store.getDescription()) && Store.getId() != null) {
                BuyList.add(update(getCurrentUser(), Store));
            } else if (Store.getId() == null) {
                /*Buy.setType(BuyType.KOLAY_AJANDA.getAttributeValue());
                Buy.setStatus(BuyStatus.YENI.getAttributeValue());*/
                BuyList.add(add(getCurrentUser(), Store));
            } else if (StringUtils.isBlank(Store.getDescription())) {
                delete(getCurrentUser(), Store.getId());
            }
        }

        return BuyList;
    }
    public byte[] generateExcelBuyReport(User currentUser, Instant startDate, Instant endDate) throws Exception {
        List<User> hierarchicalUsers = baseUserService.getHierarchicalUsersOnlyDownwards(currentUser);
        List<Long> hierarchicalUserIds = hierarchicalUsers.stream().map(User::getId).collect(Collectors.toList());
        startDate = startDate.atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant();
        endDate = endDate.atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay(ZoneId.systemDefault()).plusDays(1).toInstant();

        Request request = Request.build().page(0).size(Integer.MAX_VALUE).filter(
            Filter.And(
                Filter.FilterItem("createdDate", FilterItem.Operator.GREATER_OR_EQUAL_THAN, startDate),
                Filter.FilterItem("createdDate", FilterItem.Operator.LESS_THAN, endDate),
                Filter.Or(
                    Filter.FilterItem("assigner.id", FilterItem.Operator.IN, hierarchicalUserIds),
                    Filter.FilterItem("owner.id", FilterItem.Operator.IN, hierarchicalUserIds),
                    Filter.FilterItem("buyowner.id", FilterItem.Operator.IN, hierarchicalUserIds)
                )
            )
        );

        List<Store> Stores = getData(null, request, false).getBody();

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet();

        sheet.setColumnWidth(0, 200);
        sheet.createRow(0).setHeight((short) 200);

        XSSFFont boldFont = workbook.createFont();
        boldFont.setBold(true);

        int rowIndex = 1;
        int columnIndex = 1;

        XSSFRow headerRow = sheet.createRow(rowIndex++);

        XSSFCellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(boldFont);
        headerCellStyle.setFillForegroundColor(new XSSFColor(new byte[]{(byte) 189, (byte) 215, (byte) 238}, null));
        headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        XSSFCell headerCodeCell = headerRow.createCell(columnIndex++);     //Satın Alma Kodu
        XSSFCell headerAssignerCell = headerRow.createCell(columnIndex++);     //Talep Eden
        XSSFCell headerOwnerCell = headerRow.createCell(columnIndex++);  //Onaycı
        XSSFCell headerBuyOwnerCell = headerRow.createCell(columnIndex++);  //Satın Alma Sorumlusu
        XSSFCell headerCompanyCell = headerRow.createCell(columnIndex++);     //Şirket
        XSSFCell headerDepartmentCell = headerRow.createCell(columnIndex++);     //Birim
        XSSFCell headerLastDateCell = headerRow.createCell(columnIndex++);      //Son Tarih
        XSSFCell headerProductCell = headerRow.createCell(columnIndex++);      //Talep Edilen Ürün
        XSSFCell headerProdDescCell = headerRow.createCell(columnIndex++);    //Ürün Gerekçesi
        XSSFCell headerStoreOkeyCell = headerRow.createCell(columnIndex++);    //Talep Onay Durumu
        XSSFCell headerBuyOkeyCell = headerRow.createCell(columnIndex++);   //Satın Alma Onay Durumu
        XSSFCell headerCreatedDateCell = headerRow.createCell(columnIndex++); //Oluşturma Tarihi

        headerCodeCell.setCellStyle(headerCellStyle);
        headerAssignerCell.setCellStyle(headerCellStyle);
        headerOwnerCell.setCellStyle(headerCellStyle);
        headerBuyOwnerCell.setCellStyle(headerCellStyle);
        headerCompanyCell.setCellStyle(headerCellStyle);
        headerDepartmentCell.setCellStyle(headerCellStyle);
        headerLastDateCell.setCellStyle(headerCellStyle);
        headerProductCell.setCellStyle(headerCellStyle);
        headerProdDescCell.setCellStyle(headerCellStyle);
        headerStoreOkeyCell.setCellStyle(headerCellStyle);
        headerBuyOkeyCell.setCellStyle(headerCellStyle);
        headerCreatedDateCell.setCellStyle(headerCellStyle);

        headerCodeCell.setCellValue("Satın Alma Kodu");
        headerAssignerCell.setCellValue("Talep Eden");
        headerOwnerCell.setCellValue("Onaycı");
        headerBuyOwnerCell.setCellValue("Satın Alma Sorumlusu");
        headerCompanyCell.setCellValue("Şirket");
        headerDepartmentCell.setCellValue("Birim");
        headerLastDateCell.setCellValue("Son Talep Tarihi");
        headerProductCell.setCellValue("Talep Edilen Ürün");
        headerProdDescCell.setCellValue("Ürün Gerekçesi");
        headerStoreOkeyCell.setCellValue("Talep Onay Durumu");
        headerBuyOkeyCell.setCellValue("Satın Alma Onay Durumu");
        headerCreatedDateCell.setCellValue("Oluşturma Tarihi");

        for (Store store : Stores) {
            columnIndex = 1;

            XSSFRow row = sheet.createRow(rowIndex++);

            XSSFCell codeCell = row.createCell(columnIndex++);
            XSSFCell assignerCell = row.createCell(columnIndex++);
            XSSFCell ownerCell = row.createCell(columnIndex++);
            XSSFCell buyOwnerCell = row.createCell(columnIndex++);
            XSSFCell companyCell = row.createCell(columnIndex++);
            XSSFCell departmentCell = row.createCell(columnIndex++);
            XSSFCell lastDateCell = row.createCell(columnIndex++);
            XSSFCell productCell = row.createCell(columnIndex++);
            XSSFCell prodDescCell = row.createCell(columnIndex++);
            XSSFCell storeOkeyCell = row.createCell(columnIndex++);
            XSSFCell buyOkeyCell = row.createCell(columnIndex++);
            XSSFCell createdDateCell = row.createCell(columnIndex++);


            codeCell.setCellValue(store.getStcode());
            if (store.getAssigner() != null) {
                assignerCell.setCellValue(store.getAssigner().getFullName());
            }
            if (store.getOwner() != null) {
                ownerCell.setCellValue(store.getOwner().getFullName());
            }
            if (store.getBuyowner() != null) {
                buyOwnerCell.setCellValue(store.getBuyowner().getFullName());
            }
            /*if (store.getSirket() != null) {
                companyCell.setCellValue(store.getSirket().getLabel());
            }
            if (store.getBirim() != null) {
                departmentCell.setCellValue(store.getBirim().getLabel());
            }*/
            if (store.getEndDate()!= null) {
                lastDateCell.setCellValue(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm").withZone(ZoneId.systemDefault()).format(store.getEndDate()));
            }
            productCell.setCellValue(store.getRequest());
            prodDescCell.setCellValue(store.getDescription());

            if (store.getStatus() != null) {
                storeOkeyCell.setCellValue(store.getStatus().getLabel());
            }
            if (store.getBuyStatus() != null) {
                buyOkeyCell.setCellValue(store.getBuyStatus().getLabel());
            }
            if (store.getCreatedDate() != null) {
                createdDateCell.setCellValue(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm").withZone(ZoneId.systemDefault()).format(store.getCreatedDate()));
            }
        }

        for (int i = 0; i < 7; i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();

        return out.toByteArray();
    }
}
