package tr.com.meteor.crm.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.meteor.crm.domain.HolUser;
import tr.com.meteor.crm.domain.Holiday;
import tr.com.meteor.crm.domain.User;
import tr.com.meteor.crm.repository.HolUserRepository;
import tr.com.meteor.crm.repository.HolidayRepository;
import tr.com.meteor.crm.utils.filter.Filter;
import tr.com.meteor.crm.utils.filter.FilterItem;
import tr.com.meteor.crm.utils.request.Request;

import java.io.ByteArrayOutputStream;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class HolUserService extends GenericIdNameAuditingEntityService<HolUser, UUID, HolUserRepository> {

    public HolUserService(BaseUserService baseUserService, BaseRoleService baseRoleService,
                          BasePermissionService basePermissionService, BaseFileDescriptorService baseFileDescriptorService,
                          BaseConfigurationService baseConfigurationService,
                          HolUserRepository repository) {
        super(baseUserService, baseRoleService, basePermissionService, baseFileDescriptorService, baseConfigurationService,
            HolUser.class, repository);
    }

    public byte[] generateExcelHolUserReport(User currentUser, Instant startDate, Instant endDate) throws Exception {
        List<User> hierarchicalUsers = baseUserService.getHierarchicalUsersOnlyDownwards(currentUser);
        List<Long> hierarchicalUserIds = hierarchicalUsers.stream().map(User::getId).collect(Collectors.toList());
        startDate = startDate.atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant();
        endDate = endDate.atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay(ZoneId.systemDefault()).plusDays(1).toInstant();

        Request request = Request.build().page(0).size(Integer.MAX_VALUE).filter(
            Filter.And(
                Filter.FilterItem("createdDate", FilterItem.Operator.GREATER_OR_EQUAL_THAN, startDate),
                Filter.FilterItem("createdDate", FilterItem.Operator.LESS_THAN, endDate),
                Filter.Or(
                    Filter.FilterItem("user.id", FilterItem.Operator.IN, hierarchicalUserIds)
                )
            )
        );

        List<HolUser> HolUsers = getData(null, request, false).getBody();

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

        XSSFCell headerUserCell = headerRow.createCell(columnIndex++);
        XSSFCell headerDogTarCell = headerRow.createCell(columnIndex++);
        XSSFCell headerIsBasCell = headerRow.createCell(columnIndex++);
        XSSFCell headerYilHakCell = headerRow.createCell(columnIndex++);
        XSSFCell headerTopHakCell = headerRow.createCell(columnIndex++);
        XSSFCell headerTopKulCell = headerRow.createCell(columnIndex++);
        XSSFCell headerYilDevirCell = headerRow.createCell(columnIndex++);
        XSSFCell headerYilGunCell = headerRow.createCell(columnIndex++);
        XSSFCell headerTopYilCell = headerRow.createCell(columnIndex++);
        XSSFCell headerKulYilCell = headerRow.createCell(columnIndex++);
        XSSFCell headerKalYilCell = headerRow.createCell(columnIndex++);
        XSSFCell headerKulMazCell = headerRow.createCell(columnIndex++);
        XSSFCell headerTopKulMazCell = headerRow.createCell(columnIndex++);
        XSSFCell headerKalMazCell = headerRow.createCell(columnIndex++);
        XSSFCell headerKulBabaCell = headerRow.createCell(columnIndex++);
        XSSFCell headerKulOlumCell = headerRow.createCell(columnIndex++);
        XSSFCell headerKulEvlCell = headerRow.createCell(columnIndex++);
        XSSFCell headerKulDogCell = headerRow.createCell(columnIndex++);
        XSSFCell headerKulRapCell = headerRow.createCell(columnIndex++);
        XSSFCell headerKulIdrCell = headerRow.createCell(columnIndex++);
        XSSFCell headerKulUcrCell = headerRow.createCell(columnIndex++);

        headerUserCell.setCellStyle(headerCellStyle);
        headerDogTarCell.setCellStyle(headerCellStyle);
        headerIsBasCell.setCellStyle(headerCellStyle);
        headerYilHakCell.setCellStyle(headerCellStyle);
        headerTopHakCell.setCellStyle(headerCellStyle);
        headerTopKulCell.setCellStyle(headerCellStyle);
        headerYilDevirCell.setCellStyle(headerCellStyle);
        headerYilGunCell.setCellStyle(headerCellStyle);
        headerTopYilCell.setCellStyle(headerCellStyle);
        headerKulYilCell.setCellStyle(headerCellStyle);
        headerKalYilCell.setCellStyle(headerCellStyle);
        headerKulMazCell.setCellStyle(headerCellStyle);
        headerTopKulMazCell.setCellStyle(headerCellStyle);
        headerKalMazCell.setCellStyle(headerCellStyle);
        headerKulBabaCell.setCellStyle(headerCellStyle);
        headerKulOlumCell.setCellStyle(headerCellStyle);
        headerKulEvlCell.setCellStyle(headerCellStyle);
        headerKulDogCell.setCellStyle(headerCellStyle);
        headerKulRapCell.setCellStyle(headerCellStyle);
        headerKulIdrCell.setCellStyle(headerCellStyle);
        headerKulUcrCell.setCellStyle(headerCellStyle);

        headerUserCell.setCellValue("Personel");
        headerDogTarCell.setCellValue("Doğum Tarihi");
        headerIsBasCell.setCellValue("İşe Başlangıç Tarihi");
        headerYilHakCell.setCellValue("Yıllık İzin Eklenme Tarihi");
        headerTopHakCell.setCellValue("Toplam Hakedilen İzin Gün Sayısı");
        headerTopKulCell.setCellValue("Toplam Kullanılan İzin Gün Sayısı");
        headerYilDevirCell.setCellValue("Devreden İzin Gün Sayısı");
        headerYilGunCell.setCellValue("Yıllık Hakedilen İzin Gün Sayısı");
        headerTopYilCell.setCellValue("Toplam Kullanılabilir İzin Gün Sayısı");
        headerKulYilCell.setCellValue("Dönem içi Kullanılan İzin Gün Sayısı");
        headerKalYilCell.setCellValue("Kalan Yıllık İzin Gün Sayısı");
        headerKulMazCell.setCellValue("Kullanılan Mazeret İzni");
        headerTopKulMazCell.setCellValue("Toplam Kullanılan Mazeret İzni");
        headerKalMazCell.setCellValue("Kalan Mazeret İzni");
        headerKulBabaCell.setCellValue("Kullanılan Babalık İzni");
        headerKulOlumCell.setCellValue("Kullanılan Ölüm İzni");
        headerKulEvlCell.setCellValue("Kullanılan Evlilik İzni");
        headerKulDogCell.setCellValue("Kullanılan Doğum İzni");
        headerKulRapCell.setCellValue("Kullanılan Rapor");
        headerKulIdrCell.setCellValue("Kullanılan İdari İzin");
        headerKulUcrCell.setCellValue("Kullanılan İdari İzin");

        for (HolUser HolUser : HolUsers) {
            columnIndex = 1;

            XSSFRow Row = sheet.createRow(rowIndex++);

            XSSFCell UserCell = Row.createCell(columnIndex++);
            XSSFCell DogTarCell = Row.createCell(columnIndex++);
            XSSFCell IsBasCell = Row.createCell(columnIndex++);
            XSSFCell YilHakCell = Row.createCell(columnIndex++);
            XSSFCell TopHakCell = Row.createCell(columnIndex++);
            XSSFCell TopKulCell = Row.createCell(columnIndex++);
            XSSFCell YilDevirCell = Row.createCell(columnIndex++);
            XSSFCell YilGunCell = Row.createCell(columnIndex++);
            XSSFCell TopYilCell = Row.createCell(columnIndex++);
            XSSFCell KulYilCell = Row.createCell(columnIndex++);
            XSSFCell KalYilCell = Row.createCell(columnIndex++);
            XSSFCell KulMazCell = Row.createCell(columnIndex++);
            XSSFCell TopKulMazCell = Row.createCell(columnIndex++);
            XSSFCell KalMazCell = Row.createCell(columnIndex++);
            XSSFCell KulBabaCell = Row.createCell(columnIndex++);
            XSSFCell KulOlumCell = Row.createCell(columnIndex++);
            XSSFCell KulEvlCell = Row.createCell(columnIndex++);
            XSSFCell KulDogCell = Row.createCell(columnIndex++);
            XSSFCell KulRapCell = Row.createCell(columnIndex++);
            XSSFCell KulIdrCell = Row.createCell(columnIndex++);
            XSSFCell KulUcrCell = Row.createCell(columnIndex++);

            if (HolUser.getUser() != null) {
                UserCell.setCellValue(HolUser.getUser().getFullName());
            }

            if (HolUser.getDogTar() != null) {
                DogTarCell.setCellValue(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm").withZone(ZoneId.systemDefault()).format(HolUser.getDogTar()));
            }

            if (HolUser.getIsBas() != null) {
                IsBasCell.setCellValue(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm").withZone(ZoneId.systemDefault()).format(HolUser.getIsBas()));
            }

            if (HolUser.getYilHak() != null) {
                YilHakCell.setCellValue(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm").withZone(ZoneId.systemDefault()).format(HolUser.getYilHak()));
            }
            TopHakCell.setCellValue(HolUser.getTopHak().floatValue());
            TopKulCell.setCellValue(HolUser.getTopKul().floatValue());
            YilDevirCell.setCellValue(HolUser.getYilDevir().floatValue());
            YilGunCell.setCellValue(HolUser.getYilGun().floatValue());
            TopYilCell.setCellValue(HolUser.getTopYil().floatValue());
            KulYilCell.setCellValue(HolUser.getKulYil().floatValue());
            KalYilCell.setCellValue(HolUser.getKalYil().floatValue());
            KulMazCell.setCellValue(HolUser.getKulMaz().floatValue());
            TopKulMazCell.setCellValue(HolUser.getTopKulMaz().floatValue());
            KalMazCell.setCellValue(HolUser.getKalMaz().floatValue());
            KulBabaCell.setCellValue(HolUser.getKulBaba().floatValue());
            KulOlumCell.setCellValue(HolUser.getKulOlum().floatValue());
            KulEvlCell.setCellValue(HolUser.getKulEvl().floatValue());
            KulDogCell.setCellValue(HolUser.getKulDog().floatValue());
            KulRapCell.setCellValue(HolUser.getKulRap().floatValue());
            KulIdrCell.setCellValue(HolUser.getKulIdr().floatValue());
            KulUcrCell.setCellValue(HolUser.getKulUcr().floatValue());
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
