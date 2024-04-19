package org.apache.ibatis.plugin;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.Configuration;

/**
 * @author furious 2024/4/19
 */
public class InterceptorChain {

    private final Configuration configuration;
    private final List<Interceptor> interceptors = new ArrayList<>();

    public InterceptorChain(Configuration configuration) {
        this.configuration = configuration;
    }

    public <T> T pluginAll(Object target) {
        for (Interceptor interceptor : interceptors) {
            target = interceptor.plugin(target, configuration);
        }
        return (T) target;
    }

    public void addInterceptor(Interceptor interceptor) {
        interceptors.add(interceptor);
    }
}
