package org.apache.ibatis.builder;

import java.util.List;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlSource;

/**
 * @author furious 2024/4/7
 */
public class StaticSqlSource implements SqlSource {

    private final String sql;
    private final List<ParameterMapping> parameterMappings;

    public StaticSqlSource(String sql) {
        this(sql, null);
    }

    public StaticSqlSource(String sql, List<ParameterMapping> parameterMappings) {
        this.sql = sql;
        this.parameterMappings = parameterMappings;
    }

    @Override
    public BoundSql getBoundSql(Object parameterObject) {
        return new BoundSql(sql, parameterMappings, parameterObject);
    }
}
