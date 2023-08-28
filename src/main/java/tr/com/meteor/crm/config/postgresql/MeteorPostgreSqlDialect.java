package tr.com.meteor.crm.config.postgresql;

import io.github.jhipster.domain.util.FixedPostgreSQL95Dialect;
import tr.com.meteor.crm.config.postgresql.timestamp.PostgreSqlTimestampDiffFunction;

public class MeteorPostgreSqlDialect extends FixedPostgreSQL95Dialect {
    public MeteorPostgreSqlDialect() {
        super();
        registerFunction(
            "postgresql_ts_diff",
            new PostgreSqlTimestampDiffFunction()
        );
    }
}
