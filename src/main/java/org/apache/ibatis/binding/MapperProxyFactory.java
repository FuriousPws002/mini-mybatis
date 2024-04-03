package org.apache.ibatis.binding;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import org.apache.ibatis.session.SqlSession;

/**
 * @author furious 2024/4/3
 */
public class MapperProxyFactory<T> {

    private final Class<T> mapperInterface;

    public MapperProxyFactory(Class<T> mapperInterface) {
        this.mapperInterface = mapperInterface;
    }

    public <T> T newInstance(SqlSession sqlSession) {
        InvocationHandler handler = new MapperProxy(sqlSession, mapperInterface);
        return (T) Proxy.newProxyInstance(mapperInterface.getClassLoader(), new Class[]{mapperInterface}, handler);
    }
}
