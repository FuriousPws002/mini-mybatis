package org.apache.ibatis.binding;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;

/**
 * @author furious 2024/4/3
 */
public class MapperRegistry {

    private final Configuration config;
    private final Map<Class<?>, MapperProxyFactory<?>> knownMappers = new HashMap<>();

    public MapperRegistry(Configuration config) {
        this.config = config;
    }

    public <T> boolean hasMapper(Class<T> type) {
        return knownMappers.containsKey(type);
    }

    public <T> void addMapper(Class<T> type) {
        //不是接口不支持，直接返回
        if (!type.isInterface() || hasMapper(type)) {
            return;
        }
        knownMappers.put(type, new MapperProxyFactory<>(type));
    }

    public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
        MapperProxyFactory<?> mapperProxyFactory = knownMappers.get(type);
        if (Objects.isNull(mapperProxyFactory)) {
            throw new BindingException(type + " type is not exist");
        }
        return mapperProxyFactory.newInstance(sqlSession);
    }
}
