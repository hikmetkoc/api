package tr.com.meteor.crm.utils.request;

import tr.com.meteor.crm.domain.IdEntity;
import tr.com.meteor.crm.utils.metadata.MetadataReader;
import tr.com.meteor.crm.utils.metadata.FieldMetadataFull;

import java.util.List;

public class Column {
    private String name;
    private String title;
    private ColumnType columnType = ColumnType.DEFAULT;

    public static <TEntity extends IdEntity> List<Column> validateAndFix(Class<TEntity> entityClass, List<Column> columns) {
        for (Column column : columns) {
            FieldMetadataFull fieldMetadataFull = MetadataReader.getCrmFieldMetadataFromPath(entityClass, column.name);

            if (fieldMetadataFull != null) {
                if (fieldMetadataFull.getJavaType().endsWith(".AttributeValue") && !column.name.endsWith(".label")) {
                    column.name = column.name + ".label";
                }
            }
        }

        return columns;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Column name(String name) {
        this.name = name;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Column title(String title) {
        this.title = title;
        return this;
    }

    public ColumnType getColumnType() {
        return columnType;
    }

    public void SetColumnType(ColumnType columnType) {
        this.columnType = columnType;
    }

    public Column columnType(ColumnType columnType) {
        this.columnType = columnType;
        return this;
    }
}
