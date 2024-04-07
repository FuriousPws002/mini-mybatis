package org.apache.ibatis.builder.xml;

import java.io.InputStream;
import java.util.List;
import java.util.Objects;

import org.apache.ibatis.builder.BaseBuilder;
import org.apache.ibatis.builder.BuilderException;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.parsing.XPathParser;
import org.apache.ibatis.session.Configuration;

/**
 * xml解析类，解析xml中<mapper>标签的值
 *
 * @author furious 2024/4/7
 */
public class XMLMapperBuilder extends BaseBuilder {

    private final XPathParser parser;
    private final MapperBuilderAssistant builderAssistant;
    private final String resource;

    public XMLMapperBuilder(InputStream inputStream, Configuration configuration, String resource, String namespace) {
        super(configuration);
        this.builderAssistant = new MapperBuilderAssistant(configuration, resource);
        builderAssistant.setCurrentNamespace(namespace);
        this.resource = resource;
        this.parser = new XPathParser(inputStream);
    }

    public void parse() {
        if (!configuration.isResourceLoaded(resource)) {
            configurationElement(parser.evalNode("/mapper"));
            configuration.addLoadedResource(resource);
        }
    }

    private void configurationElement(XNode context) {
        try {
            String namespace = context.getAttribute("namespace");
            if (Objects.isNull(namespace) || namespace.isEmpty()) {
                throw new BuilderException("mapper namespace can not be empty");
            }
            buildStatementFromContext(context.evalNodes("insert"));
        } catch (Exception e) {
            throw new BuilderException("parse mapper xml error", e);
        }
    }

    private void buildStatementFromContext(List<XNode> list) {
        for (XNode context : list) {
            final XMLStatementBuilder statementParser = new XMLStatementBuilder(configuration, builderAssistant, context);
            statementParser.parseStatementNode();
        }
    }
}
