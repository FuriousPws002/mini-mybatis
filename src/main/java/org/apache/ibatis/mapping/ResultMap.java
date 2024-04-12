package org.apache.ibatis.mapping;

import java.util.List;
import java.util.Objects;

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

    private String id;
    private List<ResultMapping> resultMappings;
    /**
     * 是否包含嵌套resultMap
     */
    private boolean hasNestedResultMaps;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<ResultMapping> getResultMappings() {
        return resultMappings;
    }

    public void setResultMappings(List<ResultMapping> resultMappings) {
        this.resultMappings = resultMappings;
        this.hasNestedResultMaps = resultMappings.stream().anyMatch(m -> Objects.nonNull(m.getNestedResultMapId()));
    }

    public boolean hasNestedResultMaps() {
        return hasNestedResultMaps;
    }
}
