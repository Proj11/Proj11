package no.ntnu.fp.model.employee;

import java.io.StringReader;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class Employee {
	
	private String name;
	private String username;
	
	public String toString(){
		return name;
	}
	
	public String getUsername() {
		return username;
	}

	public Employee(String name) {
		this.username=name;
		this.name=name;
	}
	
	public Employee(String name, String username) {
		this.name = name;
		this.username = username;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public static String allEmployeesToXML(List<Employee> allEmployees) throws ParserConfigurationException, TransformerException, InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

		Document doc = docBuilder.newDocument();
		Element rootElement = doc.createElement("employees");
		doc.appendChild(rootElement);

		for (Employee e : allEmployees) {
			Element employee = doc.createElement("employee");
			rootElement.appendChild(employee);
			
			Element name = doc.createElement("name");
			employee.appendChild(name);
			name.appendChild(doc.createTextNode(e.getName()));
			
			Element username = doc.createElement("username");
			employee.appendChild(username);
			username.appendChild(doc.createTextNode(e.getUsername()));
		}

		DOMSource source = new DOMSource(doc);
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		StringWriter stringWriter = new StringWriter();
		Result result = new StreamResult(stringWriter);
		transformer.transform(source, result);
		return stringWriter.getBuffer().toString();
	}
	
	public String toXML() throws ParserConfigurationException, TransformerException {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

		Document doc = docBuilder.newDocument();
		Element rootElement = doc.createElement("employee");
		doc.appendChild(rootElement);
		
		Element name = doc.createElement("name");
		rootElement.appendChild(name);
		name.appendChild(doc.createTextNode(getName()));
		
		Element username = doc.createElement("username");
		rootElement.appendChild(username);
		username.appendChild(doc.createTextNode(getUsername()));

		DOMSource source = new DOMSource(doc);
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		StringWriter stringWriter = new StringWriter();
		Result result = new StreamResult(stringWriter);
		transformer.transform(source, result);
		return stringWriter.getBuffer().toString();
	}
	
	public static List<Employee> xmlToEmployeeList(String xml) {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(xml));
			Document doc = db.parse(is);
			doc.getDocumentElement().normalize();

			List<Employee> employees = new ArrayList<Employee>();
			NodeList nodeLst = doc.getElementsByTagName("employee");
			for (int i = 0; i < nodeLst.getLength(); i++) {
				Node nNode = nodeLst.item(i);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) nNode;
					String name = getTagValues("name", element);
					String username = getTagValues("username", element);
					employees.add(new Employee(name, username));					
				}
			}
			return employees;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Employee fromXML(String xml) {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(xml));
			Document doc = db.parse(is);
			doc.getDocumentElement().normalize();
			NodeList nodeLst = doc.getElementsByTagName("employee");
			for (int i = 0; i < nodeLst.getLength(); i++) {
				Node nNode = nodeLst.item(i);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) nNode;
					String name = getTagValues("name", element);
					String username = getTagValues("username", element);
					return (new Employee(name, username));					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;		
	}
	
	private static String getTagValues(String sTag, Element element) {
		if (element.getElementsByTagName(sTag).item(0)==null) {
			return null;
		}
		NodeList nList = element.getElementsByTagName(sTag).item(0).getChildNodes();
		Node nValue = (Node) nList.item(0);
		return nValue.getNodeValue();
	}
}
