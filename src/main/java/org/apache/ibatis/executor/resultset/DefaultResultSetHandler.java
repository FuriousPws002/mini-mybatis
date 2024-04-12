package org.apache.ibatis.executor.resultset;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;

/**
 * @author furious 2024/4/12
 */
public class DefaultResultSetHandler implements ResultSetHandler {

    private final Configuration configuration;
    private final MappedStatement mappedStatement;
    private final TypeHandlerRegistry typeHandlerRegistry;

    public DefaultResultSetHandler(MappedStatement mappedStatement) {
        this.configuration = mappedStatement.getConfiguration();
        this.mappedStatement = mappedStatement;
        this.typeHandlerRegistry = configuration.getTypeHandlerRegistry();
    }

    @Override
    public <T> List<T> handleResultSets(Statement stmt) throws SQLException {
        final List<Object> multipleResults = new ArrayList<>();
        ResultSet rs = stmt.getResultSet();
        if (Objects.isNull(rs)) {
            return Collections.emptyList();
        }
        ResultSetWrapper rsw = new ResultSetWrapper(rs, configuration);
        ResultMap resultMap = mappedStatement.getResultMap();
        handleResultSet(rsw, resultMap, multipleResults);
        return (List<T>) multipleResults;
    }

    private void handleResultSet(ResultSetWrapper rsw, ResultMap resultMap, List<Object> multipleResults) throws SQLException {
        try {
            handleRowValues(rsw, resultMap, multipleResults);
        } finally {
            closeResultSet(rsw.getResultSet());
        }
    }

    private void handleRowValues(ResultSetWrapper rsw, ResultMap resultMap, List<Object> multipleResults) throws SQLException {
        handleRowValuesForSimpleResultMap(rsw, resultMap, multipleResults);
    }

    private void handleRowValuesForSimpleResultMap(ResultSetWrapper rsw, ResultMap resultMap, List<Object> multipleResults) throws SQLException {
        ResultSet resultSet = rsw.getResultSet();
        while (!resultSet.isClosed() && resultSet.next()) {
            Object rowValue = getRowValue(rsw, resultMap);
            multipleResults.add(rowValue);
        }
    }

    private Object getRowValue(ResultSetWrapper rsw, ResultMap resultMap) throws SQLException {
        Object rowValue = createResultObject(resultMap);
        applyAutomaticMappings(rsw, rowValue);
        return rowValue;
    }

    private boolean applyAutomaticMappings(ResultSetWrapper rsw, Object rowValue) throws SQLException {
        List<UnMappedColumnAutoMapping> autoMapping = createAutomaticMappings(rsw, rowValue);
        boolean foundValues = false;
        if (!autoMapping.isEmpty()) {
            for (UnMappedColumnAutoMapping mapping : autoMapping) {
                final Object value = mapping.typeHandler.getResult(rsw.getResultSet(), mapping.column);
                if (value != null) {
                    foundValues = true;
                }
                try {
                    FieldUtils.writeField(rowValue, mapping.property, value, true);
                } catch (IllegalAccessException e) {
                    ExceptionUtils.rethrow(e);
                }
            }
        }
        return foundValues;
    }

    private List<UnMappedColumnAutoMapping> createAutomaticMappings(ResultSetWrapper rsw, Object rowValue) {
        List<UnMappedColumnAutoMapping> autoMapping = new ArrayList<>();
        List<String> columnLabels = rsw.getColumnLabels();
        for (String columnLabel : columnLabels) {
            Field field = FieldUtils.getDeclaredField(rowValue.getClass(), columnLabel, true);
            String property = Objects.isNull(field) ? null : columnLabel;
            if (Objects.nonNull(property)) {
                Class<?> propertyType = field.getType();
                if (typeHandlerRegistry.hasTypeHandler(propertyType)) {
                    final TypeHandler<?> typeHandler = typeHandlerRegistry.getTypeHandler(propertyType);
                    autoMapping.add(new UnMappedColumnAutoMapping(columnLabel, property, typeHandler));
                }
            }
        }
        return autoMapping;
    }

    private Object createResultObject(ResultMap resultMap) throws SQLException {
        try {
            return resultMap.getType().newInstance();
        } catch (Exception e) {
            throw new SQLException("create return type fail " + e);
        }
    }

    private void closeResultSet(ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException ignored) {
        }
    }

    /**
     * 数据库中列和对象中属性映射
     */
    private static class UnMappedColumnAutoMapping {
        private final String column;
        private final String property;
        private final TypeHandler<?> typeHandler;

        public UnMappedColumnAutoMapping(String column, String property, TypeHandler<?> typeHandler) {
            this.column = column;
            this.property = property;
            this.typeHandler = typeHandler;
        }
    }
}
