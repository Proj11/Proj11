package no.ntnu.fp.model.message;

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
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import no.ntnu.fp.model.appointment.Appointment;
import no.ntnu.fp.model.employee.Employee;

public class Message {
	
	private Employee recipient;
	private Employee messageCreatedBy;
	private String messageText;
	private int messageID;
	private int appointmentId;
	
	private Appointment appointment;
	
	public Employee getRecipient() {
		return recipient;
	}

	public void setRecipient(Employee recipient) {
		this.recipient = recipient;
	}

	public Employee getMessageCreatedBy() {
		return messageCreatedBy;
	}

	public void setMessageCreatedBy(Employee messageCreatedBy) {
		this.messageCreatedBy = messageCreatedBy;
	}

	public String getMessageText() {
		return messageText;
	}

	public void setMessageText(String messageText) {
		this.messageText = messageText;
	}

	public int getMessageID() {
		return messageID;
	}

	public void setMessageID(int messageID) {
		this.messageID = messageID;
	}
	
	public int getAppointmentId(){
		return appointmentId;
	}
	
	public void setAppointmentId(int appointmentId){
		this.appointmentId = appointmentId;
	}

	public Message(Appointment appointment) {
		this.appointment=appointment;
	}
	
	public Message() {
		// TODO Auto-generated constructor stub
	}

	public Appointment getAppointment(){
		return appointment;
	}
	
	public String toXML() throws ParserConfigurationException, TransformerException, InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

		Document doc = docBuilder.newDocument();
		Element rootElement = doc.createElement("message");
		doc.appendChild(rootElement);
		
		Element messageID = doc.createElement("messageID");
		rootElement.appendChild(messageID);
		messageID.appendChild(doc.createTextNode(getMessageID()+ ""));
		
		Element appointmentID = doc.createElement("appointmentID");
		rootElement.appendChild(appointmentID);
		appointmentID.appendChild(doc.createTextNode(getAppointmentId()+ ""));
		
		Element recipient = doc.createElement("recipient");
		rootElement.appendChild(recipient);
		recipient.appendChild(doc.createTextNode(getRecipient().getUsername()));
		
		Element recipientName = doc.createElement("recipientName");
		rootElement.appendChild(recipientName);
		recipientName.appendChild(doc.createTextNode(getRecipient().getName()));
		
		Element messageCreatedBy = doc.createElement("messageCreatedBy");
		rootElement.appendChild(messageCreatedBy);
		messageCreatedBy.appendChild(doc.createTextNode(getMessageCreatedBy().getUsername()));
		
		Element messageCreatedByName = doc.createElement("messageCreatedByName");
		rootElement.appendChild(messageCreatedByName);
		messageCreatedByName.appendChild(doc.createTextNode(getMessageCreatedBy().getName()));
		
		Element messageText = doc.createElement("messageText");
		rootElement.appendChild(messageText);
		messageText.appendChild(doc.createTextNode(getMessageText()));

		DOMSource source = new DOMSource(doc);
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer optimusPrime = transformerFactory.newTransformer();
		StringWriter stringWriter = new StringWriter();
		Result result = new StreamResult(stringWriter);
		optimusPrime.transform(source, result);
		return stringWriter.getBuffer().toString();
	}
	
	public static Message xmlToMessage(String xml) {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(xml));
			Document doc = db.parse(is);
			doc.getDocumentElement().normalize();

			Message message = new Message();
			NodeList nodeLst = doc.getElementsByTagName("message");
			for (int i = 0; i < nodeLst.getLength(); i++) {
				Node nNode = nodeLst.item(i);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) nNode;
					message.setMessageID(Integer.parseInt(getTagValues("messageID", element)));
					message.setAppointmentId(Integer.parseInt(getTagValues("appointmentID", element)));
					message.setRecipient(new Employee((getTagValues("recipientName", element)), (getTagValues("recipient", element))));
					message.setMessageCreatedBy(new Employee((getTagValues("messageCreatedByName", element)), (getTagValues("messageCreatedBy", element))));
					message.setMessageText(getTagValues("messageText", element));
				}
			}
			return message;
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
