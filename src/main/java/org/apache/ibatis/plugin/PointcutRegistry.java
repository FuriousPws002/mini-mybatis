package org.apache.ibatis.plugin;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.executor.statement.StatementHandler;

/**
 * @author furious 2024/4/18
 */
public class PointcutRegistry {

    public static final String Executor_update_MappedStatement_Object = "Executor_update_MappedStatement_Object";
    public static final String Executor_query_MappedStatement_Object = "Executor_query_MappedStatement_Object";
    public static final String ParameterHandler_getParameterObject_ = "ParameterHandler_getParameterObject_";
    public static final String ParameterHandler_setParameters_PreparedStatement = "ParameterHandler_setParameters_PreparedStatement";
    public static final String ResultSetHandler_handleResultSets_Statement = "ResultSetHandler_handleResultSets_Statement";
    public static final String StatementHandler_parameterize_Statement = "StatementHandler_parameterize_Statement";
    public static final String StatementHandler_update_Statement = "StatementHandler_update_Statement";
    public static final String StatementHandler_prepare_Connection_Integer = "StatementHandler_prepare_Connection_Integer";
    public static final String StatementHandler_query_Statement = "StatementHandler_query_Statement";
    public static final String StatementHandler_getBoundSql_ = "StatementHandler_getBoundSql_";

    private final Map<String, PointcutDefinition> definitions = new LinkedHashMap<>();

    public PointcutRegistry() {
        Arrays.stream(Executor.class.getMethods()).forEach(m -> new PointcutDefinition(m, this));
        Arrays.stream(ParameterHandler.class.getMethods()).forEach(m -> new PointcutDefinition(m, this));
        Arrays.stream(ResultSetHandler.class.getMethods()).forEach(m -> new PointcutDefinition(m, this));
        Arrays.stream(StatementHandler.class.getMethods()).forEach(m -> new PointcutDefinition(m, this));
    }

    public Map<String, PointcutDefinition> getDefinitions() {
        return definitions;
    }

    public PointcutDefinition getDefinition(String key) {
        return definitions.get(key);
    }
}
