package org.apache.ibatis.scripting.xmltags;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.builder.BaseBuilder;
import org.apache.ibatis.builder.BuilderException;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.scripting.defaults.RawSqlSource;
import org.apache.ibatis.session.Configuration;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author furious 2024/4/10
 */
public class XMLScriptBuilder extends BaseBuilder {

    private final XNode context;
    private final Class<?> parameterType;
    private boolean isDynamic;
    private final Map<String, NodeHandler> nodeHandlerMap = new HashMap<>();

    public XMLScriptBuilder(Configuration configuration, XNode context) {
        this(configuration, context, null);
    }

    public XMLScriptBuilder(Configuration configuration, XNode context, Class<?> parameterType) {
        super(configuration);
        this.context = context;
        this.parameterType = parameterType;
        nodeHandlerMap.put("if", new IfHandler());
        nodeHandlerMap.put("trim", new TrimHandler());
        nodeHandlerMap.put("foreach", new ForeachHandler());
    }

    public SqlSource parseScriptNode() {
        MixedSqlNode rootSqlNode = parseDynamicTags(context);
        if (isDynamic) {
            return new DynamicSqlSource(configuration, rootSqlNode);
        }
        return new RawSqlSource(configuration, rootSqlNode, parameterType);
    }

    protected MixedSqlNode parseDynamicTags(XNode node) {
        List<SqlNode> contents = new ArrayList<>();
        NodeList children = node.getNode().getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            XNode child = node.newXNode(children.item(i));
            if (child.getNode().getNodeType() == Node.TEXT_NODE) {
                contents.add(new StaticTextSqlNode(child.getBody()));
            } else if (child.getNode().getNodeType() == Node.ELEMENT_NODE) {
                String nodeName = child.getNode().getNodeName();
                NodeHandler handler = nodeHandlerMap.get(nodeName);
                if (handler == null) {
                    throw new BuilderException("not support " + nodeName + " NodeHandler");
                }
                handler.handleNode(child, contents);
                isDynamic = true;
            }
        }
        return new MixedSqlNode(contents);
    }

    private interface NodeHandler {
        void handleNode(XNode nodeToHandle, List<SqlNode> targetContents);
    }

    /**
     * 处理 <if test> 标签
     */
    private class IfHandler implements NodeHandler {

        @Override
        public void handleNode(XNode nodeToHandle, List<SqlNode> targetContents) {
            MixedSqlNode mixedSqlNode = parseDynamicTags(nodeToHandle);
            String test = nodeToHandle.getAttribute("test");
            IfSqlNode ifSqlNode = new IfSqlNode(test, mixedSqlNode);
            targetContents.add(ifSqlNode);
        }
    }

    /**
     * 处理 <trim> 标签
     */
    private class TrimHandler implements NodeHandler {

        @Override
        public void handleNode(XNode nodeToHandle, List<SqlNode> targetContents) {
            MixedSqlNode mixedSqlNode = parseDynamicTags(nodeToHandle);
            String prefix = nodeToHandle.getAttribute("prefix");
            String prefixOverrides = nodeToHandle.getAttribute("prefixOverrides");
            TrimSqlNode trim = new TrimSqlNode(mixedSqlNode, prefix, prefixOverrides);
            targetContents.add(trim);
        }
    }

    private class ForeachHandler implements NodeHandler {

        @Override
        public void handleNode(XNode nodeToHandle, List<SqlNode> targetContents) {
            MixedSqlNode mixedSqlNode = parseDynamicTags(nodeToHandle);
            String collection = nodeToHandle.getAttribute("collection");
            String item = nodeToHandle.getAttribute("item");
            String open = nodeToHandle.getAttribute("open");
            String close = nodeToHandle.getAttribute("close");
            String separator = nodeToHandle.getAttribute("separator");
            ForeachSqlNode forEachSqlNode = new ForeachSqlNode(mixedSqlNode, collection, item, open, close, separator);
            targetContents.add(forEachSqlNode);
        }
    }

}
