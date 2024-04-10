package org.apache.ibatis.scripting.xmltags;

/**
 * @author furious 2024/4/10
 */
public class StaticTextSqlNode implements SqlNode {

    private final String text;

    public StaticTextSqlNode(String text) {
        this.text = text;
    }

    @Override
    public boolean apply(DynamicContext context) {
        context.appendSql(text);
        return true;
    }
}