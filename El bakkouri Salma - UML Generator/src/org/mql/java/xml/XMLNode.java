package org.mql.java.xml;

import java.util.List;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLNode {
	private Node node;

	public XMLNode(String source) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newDefaultInstance();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(source);
			node = document.getFirstChild();
			while (node.getNodeType() != Node.ELEMENT_NODE) {
				node = node.getNextSibling();
			}
		} catch (Exception e) {
			System.out.println("Erreur : " + e.getMessage());
		}
	}

	public XMLNode[] children() {
		List<XMLNode> list = new Vector<XMLNode>();
		NodeList nl = node.getChildNodes();
		int n = nl.getLength();
		for (int i = 0; i < n; i++) {
			Node child = nl.item(i);
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				list.add(new XMLNode(child));
			}
		}
		XMLNode t[] = new XMLNode[list.size()];
		list.toArray(t);
		return t;
	}

	public XMLNode child(String name) {
		List<XMLNode> list = new Vector<XMLNode>();
		NodeList nl = node.getChildNodes();
		int n = nl.getLength();
		for (int i = 0; i < n; i++) {
			Node child = nl.item(i);
			if (child.getNodeName().equals(name)) {
				return new XMLNode(child);
			}
		}
		return null;
	}

	public String getName() {
		return node.getNodeName();
	}

	public String getValue() {
		NodeList list = node.getChildNodes();
		if (list.getLength() == 1 && list.item(0).getNodeType() == Node.TEXT_NODE) {
			return list.item(0).getNodeValue();
		}
		return null;
	}

	public String attribute(String name) {
		NamedNodeMap atts = node.getAttributes();
		return atts.getNamedItem(name) != null ? atts.getNamedItem(name).getNodeValue() : null;
	}

	public int intAttribute(String name) {
		String att = attribute(name);
		int value = -1;
		try {
			return Integer.parseInt(att);
		} catch (Exception e) {
			return -1;
		}
	}

	public double doubleAttribute(String name) {
		String att = attribute(name);
		try {
			return Double.parseDouble(att);
		} catch (NumberFormatException e) {
			return Double.NaN;
		}
	}

	public String getType() {
		switch (node.getNodeType()) {
		case Node.ELEMENT_NODE:
			return "Element";
		case Node.ATTRIBUTE_NODE:
			return "Attribute";
		case Node.TEXT_NODE:
			return "Text";
		case Node.CDATA_SECTION_NODE:
			return "CDATA Section";
		case Node.ENTITY_REFERENCE_NODE:
			return "Entity Reference";
		case Node.ENTITY_NODE:
			return "Entity";
		case Node.PROCESSING_INSTRUCTION_NODE:
			return "Processing Instruction";
		case Node.COMMENT_NODE:
			return "Comment";
		case Node.DOCUMENT_NODE:
			return "Document";
		case Node.DOCUMENT_TYPE_NODE:
			return "Document Type";
		case Node.DOCUMENT_FRAGMENT_NODE:
			return "Document Fragment";
		case Node.NOTATION_NODE:
			return "Notation";
		default:
			return "Unknown";
		}
	}

	public boolean removeChild(String name) {
		NodeList children = node.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i);
			if (child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equals(name)) {
				node.removeChild(child);
				return true;
			}
		}
		return false;
	}

	public XMLNode(Node node) {
		this.node = node;
	}
}
