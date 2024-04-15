package org.apache.ibatis.builder;


import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandler;

/**
 * @author furious 2024/4/7
 */
public class MapperBuilderAssistant extends BaseBuilder {

    private String currentNamespace;
    private final String resource;

    public MapperBuilderAssistant(Configuration configuration, String resource) {
        super(configuration);
        this.resource = resource;
    }

    public void setCurrentNamespace(String currentNamespace) {
        this.currentNamespace = currentNamespace;
    }

    public void addMappedStatement(String id, SqlCommandType sqlCommandType, SqlSource sqlSource) {
        id = currentNamespace + "." + id;
        MappedStatement statement = new MappedStatement.Builder(configuration, id, sqlSource, sqlCommandType)
                .resource(resource)
                .build();
        configuration.addMappedStatement(statement);
    }

    public void addMappedStatement(String id, SqlCommandType sqlCommandType, SqlSource sqlSource, Class<?> resultTypeClass) {
        id = currentNamespace + "." + id;
        MappedStatement statement = new MappedStatement.Builder(configuration, id, sqlSource, sqlCommandType)
                .resource(resource)
                .resultMap(getStatementResultMap(resultTypeClass))
                .build();
        configuration.addMappedStatement(statement);
    }

    public void addMappedStatement(String id, SqlCommandType sqlCommandType, SqlSource sqlSource, Class<?> resultTypeClass, String resultMap) {
        id = currentNamespace + "." + id;
        MappedStatement statement = new MappedStatement.Builder(configuration, id, sqlSource, sqlCommandType)
                .resource(resource)
                .resultMap(getStatementResultMap(resultTypeClass, resultMap))
                .build();
        configuration.addMappedStatement(statement);
    }


    public ResultMapping buildResultMapping(Class<?> resultType, String property, String column, String nestedResultMap) throws Exception {
        Field field = FieldUtils.getDeclaredField(resultType, property, true);
        Class<?> propertyType = field.getType();
        final TypeHandler<?> typeHandler = configuration.getTypeHandlerRegistry().getTypeHandler(propertyType);
        ResultMapping resultMapping = new ResultMapping(configuration, property, column, typeHandler);
        resultMapping.setNestedResultMapId(nestedResultMap);
        return resultMapping;
    }

    public ResultMap addResultMap(String id, Class<?> type, List<ResultMapping> resultMappings) {
        ResultMap resultMap = new ResultMap(configuration, type);
        resultMap.setId(currentNamespace + "." + id);
        resultMap.setResultMappings(resultMappings);
        configuration.addResultMap(resultMap);
        return resultMap;
    }

    private ResultMap getStatementResultMap(Class<?> resultType) {
        return getStatementResultMap(resultType, null);
    }

    private ResultMap getStatementResultMap(Class<?> resultType, String resultMap) {
        if (Objects.isNull(resultType) && Objects.isNull(resultMap)) {
            return null;
        }
        if (Objects.nonNull(resultType)) {
            return new ResultMap(configuration, resultType);
        }
        return configuration.getResultMap(currentNamespace + "." + resultMap);
    }

}
