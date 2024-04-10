package org.apache.ibatis.scripting.xmltags;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.builder.BaseBuilder;
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

    public XMLScriptBuilder(Configuration configuration, XNode context) {
        this(configuration, context, null);
    }

    public XMLScriptBuilder(Configuration configuration, XNode context, Class<?> parameterType) {
        super(configuration);
        this.context = context;
        this.parameterType = parameterType;
    }

    public SqlSource parseScriptNode() {
        MixedSqlNode rootSqlNode = parseDynamicTags(context);
        return new RawSqlSource(configuration, rootSqlNode, parameterType);
    }

    protected MixedSqlNode parseDynamicTags(XNode node) {
        List<SqlNode> contents = new ArrayList<>();
        NodeList children = node.getNode().getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            XNode child = node.newXNode(children.item(i));
            if (child.getNode().getNodeType() == Node.TEXT_NODE) {
                contents.add(new StaticTextSqlNode(child.getBody()));
            }
        }
        return new MixedSqlNode(contents);
    }
}
