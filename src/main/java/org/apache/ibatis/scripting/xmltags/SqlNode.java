package org.apache.ibatis.scripting.xmltags;

/**
 * @author furious 2024/4/10
 */
public interface SqlNode {

    boolean apply(DynamicContext context);
}
