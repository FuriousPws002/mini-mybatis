package org.apache.ibatis.builder.xml;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.apache.ibatis.builder.BaseBuilder;
import org.apache.ibatis.builder.BuilderException;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
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
            resultMapElements(context.evalNodes("resultMap"));
            buildStatementFromContext(context.evalNodes("insert|select"));
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

    private void resultMapElements(List<XNode> list) {
        for (XNode resultMapNode : list) {
            try {
                resultMapElement(resultMapNode);
            } catch (Exception ignored) {
            }
        }
    }

    private ResultMap resultMapElement(XNode resultMapNode) throws Exception {
        return resultMapElement(resultMapNode, Collections.emptyList(), null);
    }

    private ResultMap resultMapElement(XNode resultMapNode, List<ResultMapping> additionalResultMappings, Class<?> enclosingType) throws Exception {
        String type = resultMapNode.getAttribute("type", resultMapNode.getAttribute("ofType"));
        Class<?> typeClass = resolveClass(type);
        List<ResultMapping> resultMappings = new ArrayList<>(additionalResultMappings);
        List<XNode> resultChildren = resultMapNode.getChildren();
        for (XNode resultChild : resultChildren) {
            resultMappings.add(buildResultMappingFromContext(resultChild, typeClass));
        }
        String id = resultMapNode.getAttribute("id", resultMapNode.id());
        return builderAssistant.addResultMap(id, typeClass, resultMappings);
    }

    private ResultMapping buildResultMappingFromContext(XNode context, Class<?> resultType) throws Exception {
        String property = context.getAttribute("property");
        String column = context.getAttribute("column");
        String nestedResultMap = processNestedResultMappings(context, Collections.emptyList(), resultType);
        return builderAssistant.buildResultMapping(resultType, property, column, nestedResultMap);
    }

    private String processNestedResultMappings(XNode context, List<ResultMapping> resultMappings, Class<?> enclosingType) throws Exception {
        if (Objects.equals(context.getName(), "collection")) {
            ResultMap resultMap = resultMapElement(context, resultMappings, enclosingType);
            return resultMap.getId();
        }
        return null;
    }
}
