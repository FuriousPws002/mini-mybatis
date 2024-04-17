package org.apache.ibatis.scripting.xmltags;

import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

/**
 * @author furious 2024/4/10
 */
public class DynamicContext {

    private final StringJoiner sqlBuilder = new StringJoiner(" ");
    private Object parameterObject;
    private final Map<String, Object> bindings = new HashMap<>();

    public DynamicContext() {
    }

    public DynamicContext(Object parameterObject) {
        this.parameterObject = parameterObject;
    }

    public void appendSql(String sql) {
        sqlBuilder.add(sql);
    }

    public String getSql() {
        return sqlBuilder.toString().trim();
    }

    public Object getParameterObject() {
        return parameterObject;
    }

    public void setParameterObject(Object parameterObject) {
        this.parameterObject = parameterObject;
    }

    public Map<String, Object> getBindings() {
        return bindings;
    }

    public void bind(String key, Object value) {
        bindings.put(key, value);
    }
}
