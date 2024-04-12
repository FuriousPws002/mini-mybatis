package org.apache.ibatis.builder;


import java.util.Objects;

import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.session.Configuration;

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

    private ResultMap getStatementResultMap(Class<?> resultType) {
        if (Objects.isNull(resultType)) {
            return null;
        }
        return new ResultMap(configuration, resultType);
    }

}
