package org.apache.ibatis.mapping;

import org.apache.ibatis.session.Configuration;

/**
 * @author furious 2024/4/11
 */
public class ResultMap {

    private Configuration configuration;
    /**
     * select标签中的resultType
     */
    private Class<?> type;

    public ResultMap() {
    }

    public ResultMap(Configuration configuration, Class<?> type) {
        this.configuration = configuration;
        this.type = type;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }
}
