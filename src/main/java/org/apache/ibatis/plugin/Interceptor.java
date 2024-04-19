package org.apache.ibatis.plugin;

import org.apache.ibatis.session.Configuration;

/**
 * @author furious 2024/4/19
 */
public interface Interceptor {

    String pointcut();

    Object intercept(Invocation invocation) throws Throwable;

    default Object plugin(Object target, Configuration configuration) {
        return Plugin.wrap(target, configuration, this);
    }
}
