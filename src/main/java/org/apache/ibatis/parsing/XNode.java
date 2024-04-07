package org.apache.ibatis.parsing;

import java.util.List;
import java.util.Objects;
import java.util.Properties;

import org.w3c.dom.CharacterData;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * 文档节点实体封装
 *
 * @author furious 2024/4/7
 */
public class XNode {

    private final Node node;
    private final String name;
    private final String body;
    private final Properties attributes;
    private final XPathParser xpathParser;

    public XNode(XPathParser xpathParser, Node node) {
        this.xpathParser = xpathParser;
        this.node = node;
        this.name = node.getNodeName();
        this.attributes = parseAttributes(node);
        this.body = parseBody(node);
    }

    public <T> T getAttribute(String name) {
        return (T) attributes.getProperty(name);
    }

    public String getBody() {
        return body;
    }

    public List<XNode> evalNodes(String expression) {
        return xpathParser.evalNodes(node, expression);
    }

    public String getName() {
        return name;
    }

    private Properties parseAttributes(Node n) {
        Properties attributes = new Properties();
        NamedNodeMap attributeNodes = n.getAttributes();
        if (attributeNodes != null) {
            for (int i = 0; i < attributeNodes.getLength(); i++) {
                Node attribute = attributeNodes.item(i);
                attributes.put(attribute.getNodeName(), attribute.getNodeValue());
            }
        }
        return attributes;
    }

    private String parseBody(Node node) {
        String data = getBodyData(node);
        if (data == null) {
            NodeList children = node.getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                Node child = children.item(i);
                data = getBodyData(child);
                if (data != null) {
                    break;
                }
            }
        }
        return data;
    }

    private String getBodyData(Node child) {
        if (Objects.equals(child.getNodeType(), Node.TEXT_NODE)) {
            return ((CharacterData) child).getData();
        }
        return null;
    }
}