package tr.com.meteor.crm.utils.export;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import tr.com.meteor.crm.utils.metadata.MetadataReader;
import tr.com.meteor.crm.domain.IdEntity;
import tr.com.meteor.crm.domain.IdNameEntity;
import tr.com.meteor.crm.utils.metadata.EntityMetadataFull;
import tr.com.meteor.crm.utils.metadata.FieldMetadataFull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public final class ExportUtils {
    public static <TEntity extends IdEntity> Resource toExcel(List<TEntity> data, Class<TEntity> entityClass) throws Exception {
        return toExcel(data, entityClass, new ExcelColumnCallback<TEntity>() {
        });
    }

    public static <TEntity extends IdEntity> Resource toExcel(List<TEntity> data, Class<TEntity> entityClass,
                                                              ExcelColumnCallback<TEntity> excelColumnCallback) throws Exception {
        if (!MetadataReader.getClassMetadataList().containsKey(entityClass.getName())) {
            throw new Exception("Entity Metadata Not found");
        }

        EntityMetadataFull entityMetadataFull = MetadataReader.getClassMetadataList().get(entityClass.getName());
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(entityClass.getSimpleName());

        int rowIndex = 0;
        int cellIndex = 0;

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        Row headerRow = sheet.createRow(rowIndex++);
        for (FieldMetadataFull fieldMetadataFull : entityMetadataFull.getFieldMetadataMap().values()) {
            if (!excelColumnCallback.willAddField(fieldMetadataFull.getName())) continue;

            Cell cell = headerRow.createCell(cellIndex++);
            cell.setCellValue(fieldMetadataFull.getTitle());
            cell.setCellStyle(headerStyle);
        }
        cellIndex = excelColumnCallback.addExtraHeaders(headerRow, headerStyle, cellIndex);

        for (TEntity entity : data) {
            Row row = sheet.createRow(rowIndex++);
            cellIndex = 0;

            for (FieldMetadataFull fieldMetadataFull : entityMetadataFull.getFieldMetadataMap().values()) {
                if (!excelColumnCallback.willAddField(fieldMetadataFull.getName())) continue;

                Cell cell = row.createCell(cellIndex++);

                if (fieldMetadataFull.getJavaType().startsWith("tr.com.meteor.crm.domain.")) {
                    Object value = fieldMetadataFull.getField().get(entity);
                    if (value instanceof IdNameEntity) {
                        cell.setCellValue(((IdNameEntity) value).getInstanceName());
                    } else if (value != null) {
                        cell.setCellValue(value.toString());
                    }
                } else {
                    if (fieldMetadataFull.getField().get(entity) != null) {
                        cell.setCellValue(fieldMetadataFull.getField().get(entity).toString());
                    }
                }

                excelColumnCallback.reRenderCell(fieldMetadataFull.getName(), entity, cell);
            }

            cellIndex = excelColumnCallback.addExtraCell(entity, row, cellIndex);
        }

        for (int i = 0; i < cellIndex; i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();

        return new ByteArrayResource(out.toByteArray());
    }

    public static <TEntity extends IdEntity> Resource generateImportTemplate(Class<TEntity> entityClass) throws Exception {
        if (!MetadataReader.getClassMetadataList().containsKey(entityClass.getName())) {
            throw new Exception("Entity Metadata Not found");
        }

        EntityMetadataFull entityMetadataFull = MetadataReader.getClassMetadataList().get(entityClass.getName());
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(entityMetadataFull.getPluralTitle());
        sheet.protectSheet(UUID.randomUUID().toString());

        int cellIndex = 0;

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setLocked(true);

        CellStyle lockedHeaderStyle = workbook.createCellStyle();
        lockedHeaderStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
        lockedHeaderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        lockedHeaderStyle.setLocked(true);

        CellStyle unlockedCellStyle = workbook.createCellStyle();
        unlockedCellStyle.setLocked(false);

        for (int i = 0; i < entityMetadataFull.getFieldMetadataMap().size(); i++) {
            sheet.setDefaultColumnStyle(i, unlockedCellStyle);
        }

        Row headerRow = sheet.createRow(0);
        for (FieldMetadataFull fieldMetadataFull : entityMetadataFull.getFieldMetadataMap().values()) {
            Cell cell = headerRow.createCell(cellIndex++);
            cell.setCellValue(fieldMetadataFull.getTitle());
            if (fieldMetadataFull.isRequired()) {
                cell.setCellStyle(lockedHeaderStyle);
            } else {
                cell.setCellStyle(headerStyle);
            }
        }

        for (int i = 0; i < cellIndex; i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();

        return new ByteArrayResource(out.toByteArray());
    }

    public static <TEntity extends IdEntity> Resource toCsv(List<TEntity> data, Class<TEntity> entityClass) throws Exception {
        if (!MetadataReader.getClassMetadataList().containsKey(entityClass.getName())) {
            throw new Exception("Entity Metadata Not found");
        }

        EntityMetadataFull entityMetadataFull = MetadataReader.getClassMetadataList().get(entityClass.getName());
        StringBuilder csvContent = new StringBuilder();

        csvContent.append(
            entityMetadataFull.getFieldMetadataMap()
                .values()
                .stream()
                .map(FieldMetadataFull::getTitle)
                .collect(Collectors.joining(";"))
        );

        for (TEntity entity : data) {
            List<String> row = new ArrayList<>();

            for (FieldMetadataFull fieldMetadataFull : entityMetadataFull.getFieldMetadataMap().values()) {
                if (fieldMetadataFull.getJavaType().startsWith("tr.com.meteor.crm.domain.")) {
                    Object value = fieldMetadataFull.getField().get(entity);
                    if (value instanceof IdNameEntity) {
                        row.add(((IdNameEntity) value).getInstanceName());
                    } else if (value != null) {
                        row.add(value.toString());
                    } else {
                        row.add("");
                    }
                } else {
                    if (fieldMetadataFull.getField().get(entity) != null) {
                        row.add(fieldMetadataFull.getField().get(entity).toString());
                    } else {
                        row.add("");
                    }
                }
            }

            csvContent
                .append("\r\n")
                .append(
                    row.stream()
                        .map(x -> x.contains(";") ? "\"" + x + "\"" : x)
                        .collect(Collectors.joining(";"))
                )
                .append(";");
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write(csvContent.toString().getBytes(StandardCharsets.UTF_8));

        return new ByteArrayResource(out.toByteArray());
    }

    public static Resource toExcel(List<Map<String, Object>> data) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();

        int rowIndex = 0;
        int cellIndex = 0;

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        boolean isHeaderCreated = false;

        for (Map<String, Object> d : data) {
            if (!isHeaderCreated) {
                Row row = sheet.createRow(rowIndex++);
                for (String key : d.keySet()) {
                    Cell cell = row.createCell(cellIndex++);
                    cell.setCellValue(key);
                    cell.setCellStyle(headerStyle);
                }

                isHeaderCreated = true;
            }

            Row row = sheet.createRow(rowIndex++);
            cellIndex = 0;

            for (Object o : d.values()) {
                if (o == null) {
                    row.createCell(cellIndex++);
                } else {
                    row.createCell(cellIndex++).setCellValue(o.toString());
                }
            }
        }

        for (int i = 0; i < cellIndex; i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();

        return new ByteArrayResource(out.toByteArray());
    }

    public interface ExcelColumnCallback<TEntity extends IdEntity> {
        default int addExtraHeaders(Row headerRow, CellStyle cellStyle, int cellIndex) {
            return cellIndex;
        }

        default int addExtraCell(TEntity tEntity, Row row, int cellIndex) {
            return cellIndex;
        }

        default boolean willAddField(String fieldName) {
            return true;
        }

        default void reRenderCell(String fieldName, TEntity tEntity, Cell cell) {
        }
    }
}
