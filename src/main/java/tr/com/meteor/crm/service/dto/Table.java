package tr.com.meteor.crm.service.dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Table {

    private String title;
    private List<Row> rows = new ArrayList<>();

    public Table(String title) {
        this.title = title;
    }

    public List<Row> getRows() {
        return rows;
    }

    public void setRows(List<Row> rows) {
        this.rows = rows;
    }

    public void addRow(Row row) {
        rows.add(row);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public static class Row {
        private List<Cell> cells = new ArrayList<>();

        public Row() {
        }

        public Row(Cell... cells) {
            this.cells = Arrays.asList(cells);
        }

        public List<Cell> getCells() {
            return cells;
        }

        public void setCells(List<Cell> cells) {
            this.cells = cells;
        }

        public void addCell(Cell cell) {
            cells.add(cell);
        }
    }

    public static class Cell {
        private Object value;
        private Type type;

        public Cell() {
        }

        public Cell(Object value) {
            this.value = value;
            setType();
        }

        public Cell(Object value, Type type) {
            this.value = value;
            this.type = type;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
            setType();
        }

        public Type getType() {
            return type;
        }

        private void setType() {
            if (value == null) {
                type = null;
                return;
            }
            switch (value.getClass().getSimpleName()) {
                case "String":
                    type = Type.STRING;
                    break;
                case "Instant":
                    type = Type.INSTANT;
                    break;
                case "Double":
                    type = Type.DOUBLE;
                    break;
                case "Integer":
                    type = Type.INTEGER;
                    break;
                case "BigDecimal":
                    type = Type.BIG_DECIMAL;
                    break;
                case "Long":
                    type = Type.LONG;
                    break;
            }
        }

        public enum Type {
            STRING, INSTANT, DOUBLE, INTEGER, BIG_DECIMAL, PERCENTAGE, LONG
        }
    }
}
