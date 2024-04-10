package org.apache.ibatis.mapping;

import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandler;

/**
 * @author furious 2024/4/10
 */
public class ParameterMapping {

    private Configuration configuration;
    private String property;
    private Class<?> javaType = Object.class;
    private TypeHandler typeHandler;

    public ParameterMapping() {
    }

    public ParameterMapping(Configuration configuration, String property) {
        this.configuration = configuration;
        this.property = property;
        this.typeHandler = configuration.getTypeHandlerRegistry().getUnknownTypeHandler();
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public Class<?> getJavaType() {
        return javaType;
    }

    public void setJavaType(Class<?> javaType) {
        this.javaType = javaType;
    }

    public TypeHandler getTypeHandler() {
        return typeHandler;
    }

    public void setTypeHandler(TypeHandler typeHandler) {
        this.typeHandler = typeHandler;
    }
}
