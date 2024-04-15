package org.apache.ibatis.executor.resultset;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.ibatis.executor.ExecutorException;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
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
        if (resultMap.hasNestedResultMaps()) {
            handleRowValuesForNestedResultMap(rsw, resultMap, multipleResults);
        } else {
            handleRowValuesForSimpleResultMap(rsw, resultMap, multipleResults);
        }
    }

    private void handleRowValuesForNestedResultMap(ResultSetWrapper rsw, ResultMap resultMap, List<Object> multipleResults) throws SQLException {
        ResultSet resultSet = rsw.getResultSet();
        // nested resultmaps
        Map<String, Object> nestedResultObjects = new HashMap<>();
        while (!resultSet.isClosed() && resultSet.next()) {
            String rowKey = createRowKey(rsw, resultMap);
            Object partialObject = nestedResultObjects.get(rowKey);
            Object rowValue = getRowValue(rsw, resultMap, rowKey, partialObject, nestedResultObjects);
            if (Objects.isNull(partialObject)) {
                multipleResults.add(rowValue);
            }
        }
    }

    private Object getRowValue(ResultSetWrapper rsw, ResultMap resultMap, String combinedKey, Object partialObject, Map<String, Object> nestedResultObjects) throws SQLException {
        Object rowValue = partialObject;
        if (Objects.nonNull(rowValue)) {
            applyNestedResultMappings(rsw, resultMap, rowValue, nestedResultObjects, combinedKey);
        } else {
            rowValue = createResultObject(resultMap);
            applyAutomaticMappings(rsw, resultMap, rowValue);
            applyNestedResultMappings(rsw, resultMap, rowValue, nestedResultObjects, combinedKey);
            nestedResultObjects.put(combinedKey, rowValue);
        }

        return rowValue;
    }

    private void applyNestedResultMappings(ResultSetWrapper rsw, ResultMap resultMap, Object parentRowValue, Map<String, Object> nestedResultObjects, String parentRowKey) {
        if (Objects.isNull(resultMap.getResultMappings())) {
            return;
        }
        for (ResultMapping resultMapping : resultMap.getResultMappings()) {
            final String nestedResultMapId = resultMapping.getNestedResultMapId();
            if (StringUtils.isEmpty(nestedResultMapId)) {
                continue;
            }
            try {
                ResultMap nestedResultMap = configuration.getResultMap(nestedResultMapId);
                String rowKey = createRowKey(rsw, nestedResultMap);
                String combinedKey = rowKey + parentRowKey;
                Object rowValue = nestedResultObjects.get(combinedKey);
                boolean knownValue = rowValue != null;
                rowValue = getRowValue(rsw, nestedResultMap, combinedKey, rowValue, nestedResultObjects);
                if (rowValue != null && !knownValue) {
                    linkObjects(parentRowValue, resultMapping, rowValue);
                }
            } catch (SQLException e) {
                throw new ExecutorException("Error getting nested result map values for '" + resultMapping.getProperty() + "'.  Cause: " + e, e);
            }
        }
    }

    private void linkObjects(Object parentRowValue, ResultMapping resultMapping, Object rowValue) {
        try {
            List old = (List<?>) FieldUtils.readField(parentRowValue, resultMapping.getProperty(), true);
            Field field = FieldUtils.getDeclaredField(parentRowValue.getClass(), resultMapping.getProperty(), true);
            Object list = field.getType().newInstance();
            Method add = List.class.getDeclaredMethod("add", Object.class);
            add.invoke(list, rowValue);
            old.addAll(((List) list));
        } catch (Exception e) {
            ExceptionUtils.rethrow(e);
        }
    }


    private void handleRowValuesForSimpleResultMap(ResultSetWrapper rsw, ResultMap resultMap, List<Object> multipleResults) throws SQLException {
        ResultSet resultSet = rsw.getResultSet();
        while (!resultSet.isClosed() && resultSet.next()) {
            Object rowValue = getRowValue(rsw, resultMap);
            multipleResults.add(rowValue);
        }
    }

    private String createRowKey(ResultSetWrapper rsw, ResultMap resultMap) throws SQLException {
        Object rowValue = getRowValue(rsw, resultMap);
        return rsw.hashCode() + ToStringBuilder.reflectionToString(rowValue, ToStringStyle.NO_CLASS_NAME_STYLE);
    }

    private Object getRowValue(ResultSetWrapper rsw, ResultMap resultMap) throws SQLException {
        Object rowValue = createResultObject(resultMap);
        applyAutomaticMappings(rsw, resultMap, rowValue);
        return rowValue;
    }

    private boolean applyAutomaticMappings(ResultSetWrapper rsw, ResultMap resultMap, Object rowValue) throws SQLException {
        List<UnMappedColumnAutoMapping> autoMapping = createAutomaticMappings(rsw, resultMap, rowValue);
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

    private List<UnMappedColumnAutoMapping> createAutomaticMappings(ResultSetWrapper rsw, ResultMap resultMap, Object rowValue) {
        List<UnMappedColumnAutoMapping> autoMapping = new ArrayList<>();
        //若存在resultMapping，使用resultMapping
        if (Objects.nonNull(resultMap.getResultMappings()) && !resultMap.getResultMappings().isEmpty()) {
            return resultMap.getResultMappings().stream()
                    .filter(m -> StringUtils.isEmpty(m.getNestedResultMapId()))
                    .map(m -> {
                        UnMappedColumnAutoMapping auto = new UnMappedColumnAutoMapping(m.getColumn(), m.getProperty(), m.getTypeHandler());
//                        if(Objects.isNull(auto.typeHandler)){
//
//                        }
                        return auto;
                    }).collect(Collectors.toList());
        }
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
