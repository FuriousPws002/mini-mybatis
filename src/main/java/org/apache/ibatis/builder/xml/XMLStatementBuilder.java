package org.apache.ibatis.builder.xml;

import java.util.Locale;

import org.apache.commons.lang3.ClassUtils;
import org.apache.ibatis.builder.BaseBuilder;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.builder.StaticSqlSource;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.session.Configuration;

/**
 * 解析xml中
 * <insert>
 * <delete>
 * <update>
 * <select>
 * 标签的值
 *
 * @author furious 2024/4/7
 */
public class XMLStatementBuilder extends BaseBuilder {

    private final MapperBuilderAssistant builderAssistant;
    private final XNode context;

    public XMLStatementBuilder(Configuration configuration, MapperBuilderAssistant builderAssistant, XNode context) {
        super(configuration);
        this.builderAssistant = builderAssistant;
        this.context = context;
    }

    public void parseStatementNode() {
        String id = context.getAttribute("id");
        String nodeName = context.getName();
        SqlCommandType sqlCommandType = SqlCommandType.valueOf(nodeName.toUpperCase(Locale.ENGLISH));
        SqlSource sqlSource = configuration.getLanguageDriver().createSqlSource(configuration, context, null);
        String resultType = context.getAttribute("resultType");
        Class<?> resultTypeClass = resolveClass(resultType);
        builderAssistant.addMappedStatement(id, sqlCommandType, sqlSource,resultTypeClass);
    }
}
