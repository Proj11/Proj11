package no.ntnu.fp.model.room;

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

import no.ntnu.fp.model.employee.Employee;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class Room {
	
	private int roomnr;
	private int size;
	
	public Room(int roomnr, int size){
		this.roomnr = roomnr;
		this.size = size;
	}

	public int getRoomnr() {
		return roomnr;
	}

	public void setRoomnr(int roomnr) {
		this.roomnr = roomnr;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
	
	public static String allRoomsToXML(ArrayList<Room> allRooms) throws ParserConfigurationException, TransformerException, InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

		Document doc = docBuilder.newDocument();
		Element rootElement = doc.createElement("rooms");
		doc.appendChild(rootElement);

		for (Room r : allRooms) {
			Element room = doc.createElement("room");
			rootElement.appendChild(room);
			
			Element roomnr = doc.createElement("roomnr");
			room.appendChild(roomnr);
			roomnr.appendChild(doc.createTextNode(r.getRoomnr()+""));
			
			Element size = doc.createElement("size");
			room.appendChild(size);
			size.appendChild(doc.createTextNode(r.getSize()+""));
		}

		DOMSource source = new DOMSource(doc);
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer optimusPrime = transformerFactory.newTransformer();
		StringWriter stringWriter = new StringWriter();
		Result result = new StreamResult(stringWriter);
		optimusPrime.transform(source, result);
		return stringWriter.getBuffer().toString();
	}
	
	public static List<Room> xmlToRoomList(String xml) {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(xml));
			Document doc = db.parse(is);
			doc.getDocumentElement().normalize();

			List<Room> rooms = new ArrayList<Room>();
			NodeList nodeLst = doc.getElementsByTagName("room");
			for (int i = 0; i < nodeLst.getLength(); i++) {
				Node nNode = nodeLst.item(i);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) nNode;
					int roomnr = Integer.parseInt(getTagValues("roomnr", element));
					int size = Integer.parseInt(getTagValues("size", element));
					rooms.add(new Room(roomnr, size));					
				}
			}
			return rooms;
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
	
	public String toString() {
		return "Room: "+roomnr+" - Size: "+size;
	}
}