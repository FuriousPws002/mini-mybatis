package org.apache.ibatis.mapping;

import java.util.List;

/**
 * @author furious 2024/4/7
 */
public class BoundSql {

    private final String sql;
    private final List<ParameterMapping> parameterMappings;
    private final Object parameterObject;

    public BoundSql(String sql) {
        this(sql, null, null);
    }

    public BoundSql(String sql, List<ParameterMapping> parameterMappings, Object parameterObject) {
        this.sql = sql;
        this.parameterMappings = parameterMappings;
        this.parameterObject = parameterObject;
    }

    public List<ParameterMapping> getParameterMappings() {
        return parameterMappings;
    }

    public Object getParameterObject() {
        return parameterObject;
    }

    public String getSql() {
        return this.sql;
    }
}
