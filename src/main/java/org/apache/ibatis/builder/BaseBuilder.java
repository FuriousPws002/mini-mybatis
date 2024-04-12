package org.apache.ibatis.builder;

import java.util.Objects;

import org.apache.commons.lang3.ClassUtils;
import org.apache.ibatis.session.Configuration;

/**
 * @author furious 2024/4/7
 */
public class BaseBuilder {

    protected final Configuration configuration;

    public BaseBuilder(Configuration configuration) {
        this.configuration = configuration;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    @SuppressWarnings("unchecked")
    protected <T> Class<? extends T> resolveClass(String className) {
        if (Objects.isNull(className)) {
            return null;
        }
        try {
            return (Class<? extends T>) ClassUtils.getClass(className);
        } catch (Exception e) {
            throw new BuilderException(e);
        }
    }
}
