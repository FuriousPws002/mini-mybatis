package org.apache.ibatis.mapping;

import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandler;

/**
 * @author furious 2024/4/13
 */
public class ResultMapping {

    private Configuration configuration;
    private String property;
    private String column;
    private TypeHandler<?> typeHandler;
    private String nestedResultMapId;

    public ResultMapping(Configuration configuration, String property, String column, TypeHandler<?> typeHandler) {
        this.configuration = configuration;
        this.property = property;
        this.column = column;
        this.typeHandler = typeHandler;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public String getProperty() {
        return property;
    }

    public String getColumn() {
        return column;
    }

    public TypeHandler<?> getTypeHandler() {
        return typeHandler;
    }

    public String getNestedResultMapId() {
        return nestedResultMapId;
    }

    public void setNestedResultMapId(String nestedResultMapId) {
        this.nestedResultMapId = nestedResultMapId;
    }
}
