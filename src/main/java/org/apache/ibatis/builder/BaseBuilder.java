package org.apache.ibatis.builder;

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
}
