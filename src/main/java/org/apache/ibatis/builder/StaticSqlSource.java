package org.apache.ibatis.builder;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.SqlSource;

/**
 * @author furious 2024/4/7
 */
public class StaticSqlSource implements SqlSource {

    private final String sql;

    public StaticSqlSource(String sql) {
        this.sql = sql;
    }

    @Override
    public BoundSql getBoundSql(Object parameterObject) {
        return new BoundSql(this.sql);
    }
}
