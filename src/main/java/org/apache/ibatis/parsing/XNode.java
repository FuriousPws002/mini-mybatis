package org.apache.ibatis.parsing;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Element;
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

    public XNode newXNode(Node node) {
        return new XNode(xpathParser, node);
    }

    public <T> T getAttribute(String name) {
        return (T) attributes.getProperty(name);
    }

    public <T> T getAttribute(String name, T def) {
        T value = (T) attributes.getProperty(name);
        if (Objects.isNull(value)) {
            return def;
        } else {
            return value;
        }
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

    public Node getNode() {
        return node;
    }

    /**
     * 获取标签ID信息，拼接父节点ID信息
     *
     * @return String
     */
    public String id() {
        StringBuilder builder = new StringBuilder();
        XNode current = this;
        while (current != null) {
            if (current != this) {
                builder.insert(0, "_");
            }
            String value = current.getAttribute("id", current.getAttribute("property"));
            if (value != null) {
                builder.insert(0, "_" + value);
            }
            builder.insert(0, current.getName());
            current = current.getParent();
        }
        return builder.toString();
    }

    public List<XNode> getChildren() {
        List<XNode> children = new ArrayList<>();
        NodeList nodeList = node.getChildNodes();
        if (Objects.nonNull(nodeList)) {
            for (int i = 0, n = nodeList.getLength(); i < n; i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    children.add(new XNode(xpathParser, node));
                }
            }
        }
        return children;
    }

    public XNode getParent() {
        Node parent = node.getParentNode();
        if (!(parent instanceof Element)) {
            return null;
        } else {
            return new XNode(xpathParser, parent);
        }
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