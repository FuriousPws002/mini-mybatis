package org.apache.ibatis.mapping;

import org.apache.ibatis.session.Configuration;

/**
 * @author furious 2024/4/7
 */
public class MappedStatement {

    private String resource;
    private Configuration configuration;
    private String id;
    private SqlSource sqlSource;
    private SqlCommandType sqlCommandType;
    private ResultMap resultMap;

    public String getId() {
        return id;
    }

    public SqlCommandType getSqlCommandType() {
        return sqlCommandType;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public BoundSql getBoundSql(Object parameterObject) {
        BoundSql boundSql = sqlSource.getBoundSql(parameterObject);
        return new BoundSql(boundSql.getSql(), boundSql.getParameterMappings(), parameterObject, boundSql.getAdditionalParameters());
    }

    public ResultMap getResultMap() {
        return resultMap;
    }

    public static class Builder {
        private MappedStatement mappedStatement = new MappedStatement();

        public Builder(Configuration configuration, String id, SqlSource sqlSource, SqlCommandType sqlCommandType) {
            mappedStatement.configuration = configuration;
            mappedStatement.id = id;
            mappedStatement.sqlSource = sqlSource;
            mappedStatement.sqlCommandType = sqlCommandType;
        }

        public Builder resource(String resource) {
            mappedStatement.resource = resource;
            return this;
        }

        public Builder resultMap(ResultMap resultMap) {
            mappedStatement.resultMap = resultMap;
            return this;
        }

        public MappedStatement build() {
            return mappedStatement;
        }
    }
}
