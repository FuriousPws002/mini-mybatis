package org.apache.ibatis.mapping;

/**
 * @author furious 2024/4/7
 */
public interface SqlSource {

    BoundSql getBoundSql(Object parameterObject);
}
