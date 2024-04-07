package org.apache.ibatis.session;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


import org.apache.ibatis.binding.MapperRegistry;
import org.apache.ibatis.mapping.MappedStatement;

/**
 * mybatis全局配置类
 *
 * @author furious 2024/4/3
 */
public class Configuration {

    private final MapperRegistry mapperRegistry = new MapperRegistry(this);
    private final Set<String> loadedResources = new HashSet<>();
    private final Map<String, MappedStatement> mappedStatements = new HashMap<>();

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
}
