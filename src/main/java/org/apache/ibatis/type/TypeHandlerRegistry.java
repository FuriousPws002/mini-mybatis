package org.apache.ibatis.type;

import java.util.HashMap;
import java.util.Map;

/**
 * @author furious 2024/4/9
 */
public class TypeHandlerRegistry {

    private final TypeHandler<Object> unknownTypeHandler = new UnknownTypeHandler(this);
    private final Map<Class<?>, TypeHandler<?>> allTypeHandlersMap = new HashMap<>();

    public TypeHandlerRegistry() {
        register(String.class, new StringTypeHandler());

        register(Integer.class, new IntegerTypeHandler());
        register(int.class, new IntegerTypeHandler());

        register(Object.class, unknownTypeHandler);
    }

    public <T> void register(Class<T> javaType, TypeHandler<? extends T> typeHandler) {
        allTypeHandlersMap.put(javaType, typeHandler);
    }

    @SuppressWarnings("unchecked")
    public <T> TypeHandler<T> getTypeHandler(Class<T> type) {
        return (TypeHandler<T>) allTypeHandlersMap.get(type);
    }

    public TypeHandler<Object> getUnknownTypeHandler() {
        return unknownTypeHandler;
    }
}
