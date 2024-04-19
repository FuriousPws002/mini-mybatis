package org.apache.ibatis.plugin;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Objects;

import org.apache.ibatis.session.Configuration;

/**
 * @author furious 2024/4/19
 */
public class Plugin implements InvocationHandler {

    private final Object target;
    private final Configuration configuration;
    private final Interceptor interceptor;

    public Plugin(Object target, Configuration configuration, Interceptor interceptor) {
        this.target = target;
        this.configuration = configuration;
        this.interceptor = interceptor;
    }

    public static Object wrap(Object target, Configuration configuration, Interceptor interceptor) {
        String pointcut = interceptor.pointcut();
        PointcutDefinition definition = configuration.getPointcutRegistry().getDefinition(pointcut);
        //不是当前接口类型不代理
        if (!definition.getType().isAssignableFrom(target.getClass())) {
            return target;
        }
        return Proxy.newProxyInstance(target.getClass().getClassLoader(), new Class<?>[]{definition.getType()}, new Plugin(target, configuration, interceptor));
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        PointcutDefinition definition = configuration.getPointcutRegistry().getDefinition(interceptor.pointcut());
        if (!Objects.equals(definition.getId(), PointcutDefinition.id(method))) {
            //没有代理该方法，直接返回
            return method.invoke(target, args);
        }
        return interceptor.intercept(new Invocation(target, method, args));
    }
}
