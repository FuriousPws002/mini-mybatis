package org.apache.ibatis.plugin;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author furious 2024/4/18
 */
public class PointcutDefinition {

    private final Class<?> type;
    private final Method method;
    private final Class<?>[] args;
    private final String id;

    public PointcutDefinition(Method method, PointcutRegistry registry) {
        this.type = method.getDeclaringClass();
        this.method = method;
        this.args = method.getParameterTypes();
        this.id = id(method);
        registry.getDefinitions().put(id, this);
    }

    public static String id(Method method) {
        return String.join("_", method.getDeclaringClass().getSimpleName(), method.getName(), Arrays.stream(method.getParameterTypes()).map(Class::getSimpleName).collect(Collectors.joining("_")));
    }

    public Class<?> getType() {
        return type;
    }

    public Method getMethod() {
        return method;
    }

    public Class<?>[] getArgs() {
        return args;
    }

    public String getId() {
        return id;
    }
}
