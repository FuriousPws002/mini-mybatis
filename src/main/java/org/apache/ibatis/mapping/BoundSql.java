package org.apache.ibatis.mapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author furious 2024/4/7
 */
public class BoundSql {

    private final String sql;
    private final List<ParameterMapping> parameterMappings;
    private final Object parameterObject;
    private final Map<String, Object> additionalParameters;

    public BoundSql(String sql) {
        this(sql, null, null);
    }

    public BoundSql(String sql, List<ParameterMapping> parameterMappings, Object parameterObject) {
        this(sql, parameterMappings, parameterObject, new HashMap<>());
    }

    public BoundSql(String sql, List<ParameterMapping> parameterMappings, Object parameterObject, Map<String, Object> additionalParameters) {
        this.sql = sql;
        this.parameterMappings = parameterMappings;
        this.parameterObject = parameterObject;
        this.additionalParameters = additionalParameters;
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

    public void setAdditionalParameter(String key, Object value) {
        additionalParameters.put(key, value);
    }

    public Object getAdditionalParameter(String key) {
        return additionalParameters.get(key);
    }

    public boolean hasAdditionalParameter(String key) {
        return additionalParameters.containsKey(key);
    }

    public Map<String, Object> getAdditionalParameters() {
        return additionalParameters;
    }
}
