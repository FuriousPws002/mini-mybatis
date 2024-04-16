package org.apache.ibatis.scripting.xmltags;

import ognl.Ognl;

/**
 * @author furious 2024/4/16
 */
public class IfSqlNode implements SqlNode {

    private final String test;
    private final SqlNode contents;

    public IfSqlNode(String test, SqlNode contents) {
        this.test = test;
        this.contents = contents;
    }

    @Override
    public boolean apply(DynamicContext context) {
        try {
            Object value = Ognl.getValue(Ognl.parseExpression(test), context.getParameterObject());
            if (value instanceof Boolean && (Boolean) value) {
                contents.apply(context);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
