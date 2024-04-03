package org.apache.ibatis.session;

import org.apache.ibatis.binding.MapperRegistry;

/**
 * mybatis全局配置类
 *
 * @author furious 2024/4/3
 */
public class Configuration {

    private final MapperRegistry mapperRegistry = new MapperRegistry(this);

    public Configuration() {
    }

    public <T> void addMapper(Class<T> type) {
        mapperRegistry.addMapper(type);
    }

    public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
        return mapperRegistry.getMapper(type, sqlSession);
    }
}
