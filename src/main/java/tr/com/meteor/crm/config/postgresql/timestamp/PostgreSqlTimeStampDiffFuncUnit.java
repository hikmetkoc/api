package tr.com.meteor.crm.config.postgresql.timestamp;

import org.hibernate.query.criteria.internal.Renderable;
import org.hibernate.query.criteria.internal.compile.RenderingContext;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Selection;
import java.util.Collection;
import java.util.List;

public class PostgreSqlTimeStampDiffFuncUnit implements Expression, Renderable {
    private Unit unit;

    PostgreSqlTimeStampDiffFuncUnit(Unit unit) {
        this.unit = unit;
    }

    @Override
    public Predicate isNull() {
        return null;
    }

    @Override
    public Predicate isNotNull() {
        return null;
    }

    @Override
    public Predicate in(Object... objects) {
        return null;
    }

    @Override
    public Expression as(Class aClass) {
        return null;
    }

    @Override
    public Predicate in(Expression expression) {
        return null;
    }

    @Override
    public Predicate in(Collection collection) {
        return null;
    }

    @Override
    public Predicate in(Expression[] expressions) {
        return null;
    }

    @Override
    public Selection alias(String s) {
        return null;
    }

    @Override
    public boolean isCompoundSelection() {
        return false;
    }

    @Override
    public List<Selection<?>> getCompoundSelectionItems() {
        return null;
    }

    @Override
    public Class getJavaType() {
        return null;
    }

    @Override
    public String getAlias() {
        return null;
    }

    @Override
    public String render(RenderingContext renderingContext) {
        return unit.getValue();
    }

    @Override
    public String renderProjection(RenderingContext renderingContext) {
        return null;
    }

    @Override
    public String renderGroupBy(RenderingContext renderingContext) {
        return null;
    }

    public enum Unit {
        DAY("day");

        private String value;

        Unit(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
