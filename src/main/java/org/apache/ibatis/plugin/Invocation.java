package org.apache.ibatis.plugin;

import java.lang.reflect.Method;

/**
 * @author furious 2024/4/18
 */
public class Invocation {

    private final Object target;
    private final Method method;
    private final Object[] args;

    public Invocation(Object target, Method method, Object[] args) {
        this.target = target;
        this.method = method;
        this.args = args;
    }

    public <T> T getTarget() {
        return (T)target;
    }

    public Method getMethod() {
        return method;
    }

    public Object[] getArgs() {
        return args;
    }

    public <T> T proceed() throws Exception {
        return (T)method.invoke(target, args);
    }
}
