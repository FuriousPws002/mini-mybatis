package org.apache.ibatis.parsing;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.apache.ibatis.builder.BuilderException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * xml文档解析工具类
 *
 * @author furious 2024/4/7
 */
public class XPathParser {

    private final Document document;
    private final XPath xpath;

    public XPathParser(InputStream inputStream) {
        this.xpath = XPathFactory.newInstance().newXPath();
        try {
            this.document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputStream);
        } catch (Exception e) {
            throw new BuilderException("xml mapper builder document fail");
        }
    }

    public XNode evalNode(String expression) {
        return evalNode(document, expression);
    }

    public XNode evalNode(Object root, String expression) {
        Node node = (Node) evaluate(expression, root, XPathConstants.NODE);
        if (Objects.isNull(node)) {
            return null;
        }
        return new XNode(this, node);
    }

    public List<XNode> evalNodes(Node root, String expression) {
        List<XNode> xnodes = new ArrayList<>();
        NodeList nodes = (NodeList) evaluate(expression, root, XPathConstants.NODESET);
        for (int i = 0; i < nodes.getLength(); i++) {
            xnodes.add(new XNode(this, nodes.item(i)));
        }
        return xnodes;
    }

    private Object evaluate(String expression, Object root, QName returnType) {
        try {
            return xpath.evaluate(expression, root, returnType);
        } catch (Exception e) {
            throw new BuilderException("Error evaluating XPath.  Cause: " + e, e);
        }
    }
}
