package tr.com.meteor.crm.utils;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;

public final class ExcelUtils {
    public static String round(String cellReference, int decimalCount) {
        return "ROUND(" + cellReference + ", " + decimalCount + ")";
    }

    public static String round(Cell cell, int decimalCount) {
        return round(cell.getAddress().formatAsString(), decimalCount);
    }

    public static String divide(String dividedCellReference, String dividerCellReference) {
        return "(" + dividedCellReference + "/" + dividerCellReference + ")";
    }

    public static String divide(Cell dividedCell, Cell divider) {
        return divide(dividedCell.getAddress().formatAsString(), divider.getAddress().formatAsString());
    }

    public static String multiply(String m1CellReference, String m2CellReference) {
        return "(" + m1CellReference + "*" + m2CellReference + ")";
    }

    public static String multiply(Cell m1Cell, Cell m2Cell) {
        return multiply(m1Cell.getAddress().formatAsString(), m2Cell.getAddress().formatAsString());
    }

    public static String concat(String c1, String c2) {
        return "(" + c1 + "&" + c2 + ")";
    }

    public static String concat(Cell c1Cell, Cell c2Cell) {
        return concat(c1Cell.getAddress().formatAsString(), c2Cell.getAddress().formatAsString());
    }

    public static String diff(String d1CellReference, String d2CellReference) {
        return "(" + d1CellReference + "-" + d2CellReference + ")";
    }

    public static String diff(Cell d1, Cell d2) {
        return diff(d1.getAddress().formatAsString(), d2.getAddress().formatAsString());
    }

    public static String ifError(String formula, String errorText) {
        return "IFERROR(" + formula + ", " + errorText + ")";
    }

    public static void addBorderToCell(XSSFCellStyle cellStyle, BorderStyle borderStyle) {
        cellStyle.setBorderTop(borderStyle);
        cellStyle.setBorderLeft(borderStyle);
        cellStyle.setBorderRight(borderStyle);
        cellStyle.setBorderBottom(borderStyle);
    }
}
