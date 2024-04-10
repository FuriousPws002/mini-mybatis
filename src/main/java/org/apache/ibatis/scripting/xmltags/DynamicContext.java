package org.apache.ibatis.scripting.xmltags;

import java.util.StringJoiner;

/**
 * @author furious 2024/4/10
 */
public class DynamicContext {

    private final StringJoiner sqlBuilder = new StringJoiner(" ");

    public DynamicContext() {
    }

    public void appendSql(String sql) {
        sqlBuilder.add(sql);
    }

    public String getSql() {
        return sqlBuilder.toString().trim();
    }
}
