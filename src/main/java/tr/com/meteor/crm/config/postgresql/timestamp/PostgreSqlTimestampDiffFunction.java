package tr.com.meteor.crm.config.postgresql.timestamp;

import org.hibernate.QueryException;
import org.hibernate.dialect.function.SQLFunction;
import org.hibernate.engine.spi.Mapping;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.type.IntegerType;
import org.hibernate.type.Type;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import java.util.List;

public class PostgreSqlTimestampDiffFunction implements SQLFunction {
    private static final String FUNCTION_TEMPLATE = "DATE_PART('#unit#', #ts1#::timestamp - #ts2#::timestamp)";
    private static final String PARAM_1 = "#ts1#";
    private static final String PARAM_2 = "#ts2#";
    private static final String PARAM_UNIT = "#unit#";

    public static Expression TimestampDiff(CriteriaBuilder criteriaBuilder, PostgreSqlTimeStampDiffFuncUnit.Unit unit, Expression ex1, Expression ex2) {
        return criteriaBuilder.function(
            "postgresql_ts_diff",
            Integer.class,
            new PostgreSqlTimeStampDiffFuncUnit(unit),
            ex1,
            ex2
        );
    }

    @Override
    public boolean hasArguments() {
        return true;
    }

    @Override
    public boolean hasParenthesesIfNoArguments() {
        return true;
    }

    @Override
    public Type getReturnType(Type type, Mapping mapping) throws QueryException {
        return IntegerType.INSTANCE;
    }

    @Override
    public String render(Type type, List list, SessionFactoryImplementor sessionFactoryImplementor) throws QueryException {
        if (list == null || list.size() != 3) {
            throw new QueryException("This method requires 3 argument.");
        }

        return FUNCTION_TEMPLATE
            .replace(PARAM_UNIT, (CharSequence) list.get(0))
            .replace(PARAM_1, (CharSequence) list.get(1))
            .replace(PARAM_2, (CharSequence) list.get(2));
    }
}
