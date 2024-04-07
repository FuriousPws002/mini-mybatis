package org.apache.ibatis.mapping;

/**
 * @author furious 2024/4/7
 */
public class BoundSql {

    private final String sql;

    public BoundSql(String sql) {
        this.sql = sql;
    }

    public String getSql() {
        return this.sql;
    }
}
