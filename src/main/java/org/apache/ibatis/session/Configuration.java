package org.apache.ibatis.session;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.ibatis.binding.MapperRegistry;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.resultset.DefaultResultSetHandler;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.executor.statement.PrepareStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.InterceptorChain;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.PointcutRegistry;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.scripting.xmltags.XMLLanguageDriver;
import org.apache.ibatis.type.TypeHandlerRegistry;

/**
 * mybatis全局配置类
 *
 * @author furious 2024/4/3
 */
public class Configuration {

    private DataSource dataSource;

    private final MapperRegistry mapperRegistry = new MapperRegistry(this);
    private final Set<String> loadedResources = new HashSet<>();
    private final Map<String, MappedStatement> mappedStatements = new HashMap<>();
    private final TypeHandlerRegistry typeHandlerRegistry = new TypeHandlerRegistry();
    private final LanguageDriver languageDriver = new XMLLanguageDriver();
    private final Map<String, ResultMap> resultMaps = new HashMap<>();
    private final PointcutRegistry pointcutRegistry = new PointcutRegistry();
    private final InterceptorChain interceptorChain = new InterceptorChain(this);

    public Configuration() {
    }

    public <T> void addMapper(Class<T> type) {
        mapperRegistry.addMapper(type);
    }

    public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
        return mapperRegistry.getMapper(type, sqlSession);
    }

    public void addLoadedResource(String resource) {
        loadedResources.add(resource);
    }

    public boolean isResourceLoaded(String resource) {
        return loadedResources.contains(resource);
    }

    public void addMappedStatement(MappedStatement ms) {
        mappedStatements.put(ms.getId(), ms);
    }

    public MappedStatement getMappedStatement(String id) {
        return mappedStatements.get(id);
    }

    public Collection<String> getMappedStatementNames() {
        return mappedStatements.keySet();
    }

    public Collection<MappedStatement> getMappedStatements() {
        return mappedStatements.values();
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public StatementHandler newStatementHandler(Executor executor, MappedStatement mappedStatement, Object parameterObject, BoundSql boundSql) {
        StatementHandler statementHandler = new PrepareStatementHandler(executor, mappedStatement, parameterObject, boundSql);
        statementHandler = interceptorChain.pluginAll(statementHandler);
        return statementHandler;
    }

    public ParameterHandler newParameterHandler(MappedStatement mappedStatement, Object parameterObject, BoundSql boundSql) {
        return languageDriver.createParameterHandler(mappedStatement, parameterObject, boundSql);
    }

    public ResultSetHandler newResultSetHandler(MappedStatement mappedStatement) {
        return new DefaultResultSetHandler(mappedStatement);
    }

    public TypeHandlerRegistry getTypeHandlerRegistry() {
        return typeHandlerRegistry;
    }

    public LanguageDriver getLanguageDriver() {
        return languageDriver;
    }

    public void addResultMap(ResultMap resultMap) {
        resultMaps.put(resultMap.getId(), resultMap);
    }

    public ResultMap getResultMap(String id) {
        return resultMaps.get(id);
    }

    public PointcutRegistry getPointcutRegistry() {
        return pointcutRegistry;
    }

    public void addInterceptor(Interceptor interceptor) {
        interceptorChain.addInterceptor(interceptor);
    }
}
