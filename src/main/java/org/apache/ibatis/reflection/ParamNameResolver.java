package org.apache.ibatis.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.ibatis.annotations.Param;

/**
 * @author furious 2024/4/10
 */
public class ParamNameResolver {

    private boolean hasParamAnnotation;

    /**
     * map中key为参数序号，value为参数名称
     * method(@Param("M") int a, @Param("N") int b) -> {{0, "M"}, {1, "N"}}
     */
    private final SortedMap<Integer, String> names;

    public ParamNameResolver(Method method) {
        final Annotation[][] paramAnnotations = method.getParameterAnnotations();
        final SortedMap<Integer, String> map = new TreeMap<>();
        int paramCount = paramAnnotations.length;
        for (int paramIndex = 0; paramIndex < paramCount; paramIndex++) {
            String name = null;
            for (Annotation annotation : paramAnnotations[paramIndex]) {
                if (annotation instanceof Param) {
                    name = ((Param) annotation).value();
                    hasParamAnnotation = true;
                    break;
                }
            }
            if (Objects.isNull(name)) {
                name = method.getParameters()[paramIndex].getName();
            }
            map.put(paramIndex, name);
        }
        names = Collections.unmodifiableSortedMap(map);
    }

    public Object getNamedParams(Object[] args) {
        final int paramCount = names.size();
        if (Objects.isNull(args) || paramCount == 0) {
            return null;
        } else if (!hasParamAnnotation && paramCount == 1) {
            //没有Param注解，同时只有一个参数，直接返回这个参数值
            return args[0];
        } else {
            final Map<String, Object> param = new HashMap<>();
            for (Map.Entry<Integer, String> entry : names.entrySet()) {
                param.put(entry.getValue(), args[entry.getKey()]);
            }
            return param;
        }
    }
}
