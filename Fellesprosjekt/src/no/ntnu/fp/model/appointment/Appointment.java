package no.ntnu.fp.model.appointment;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
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
import org.xml.sax.SAXException;

import no.ntnu.fp.model.appointment.Participant.State;
import no.ntnu.fp.model.employee.Employee;
import no.ntnu.fp.model.room.Room;
import no.ntnu.fp.model.time.Time;
import no.ntnu.fp.timeexception.TimeException;

public class Appointment {
	private Time start;
	private Time end;
	private Date date;
	private String subject;
	private Employee leader;
	private String description;
	private List<Participant> participants;
	private int roomNumber;
	private String location;
	private int id;

	private Appointment() {
		roomNumber=0;
		participants = new ArrayList<Participant>();
	}

	public Appointment(Employee createdBy) {
		participants = new ArrayList<Participant>();
		participants.add(new Participant(createdBy, State.ACCEPTED));
		this.leader=createdBy;
		roomNumber=0;
	}
	
	public int getRoomNumber() {
		return roomNumber;
	}

	public void setRoomNumber(int roomNumber) {
		this.roomNumber = roomNumber;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Time getStart() {
		return start;
	}

	public void setStart(Time start) {
		this.start = start;
	}

	public Time getEnd() {
		return end;
	}

	public void setEnd(Time end) {
		this.end=end;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date=date;
	}
	
	public Employee getLeader(){
		return this.leader;
	}
	
	public void setLeader(Employee leader) {
		this.leader = leader;
	}

	public void setParticipants(List<Participant> participants) {
		this.participants = participants;
	}
	
	public List<Participant> getParticipants() {
		return participants;
	}
	
	public void addParticipant(Employee e){
		participants.add(new Participant(e, State.PENDING));
	}
	
	public void addParticipant(Employee e, State s) {
		participants.add(new Participant(e, s));
	}

	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}

	public String toString() {
		return subject+" \n"+leader.getName();
	}
	//Lager en xml fil med data fra appointment
	public String toXML() throws ParserConfigurationException, TransformerException {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

		Document doc = docBuilder.newDocument();
		Element rootElement = doc.createElement("appointment");
		doc.appendChild(rootElement);

		Element date = doc.createElement("date");
		rootElement.appendChild(date);

		Element month = doc.createElement("month");
		date.appendChild(month);
		month.appendChild(doc.createTextNode(getDate().getMonth() + ""));

		Element year = doc.createElement("year");
		date.appendChild(year);
		year.appendChild(doc.createTextNode(getDate().getYear() + ""));

		Element dayInMonth = doc.createElement("dayInMonth");
		date.appendChild(dayInMonth);
		dayInMonth.appendChild(doc.createTextNode(getDate().getDate()+ ""));


		Element startTime = doc.createElement("starttime");
		rootElement.appendChild(startTime);
		startTime.appendChild(doc.createTextNode(getStart().toString()));

		Element endTime = doc.createElement("endtime");
		rootElement.appendChild(endTime);
		endTime.appendChild(doc.createTextNode(getEnd().toString()));

		Element subject = doc.createElement("subject");
		rootElement.appendChild(subject);
		subject.appendChild(doc.createTextNode(getSubject()));
		
		Element id = doc.createElement("id");
		rootElement.appendChild(id);
		id.appendChild(doc.createTextNode(getId() + ""));

		if (getLocation() != null) {
			Element location = doc.createElement("location");
			rootElement.appendChild(location);
			location.appendChild(doc.createTextNode(getLocation()));
		}

		if (getRoomNumber() != 0) {
			Element roomnr = doc.createElement("roomnr");
			rootElement.appendChild(roomnr);
			roomnr.appendChild(doc.createTextNode(getRoomNumber()+""));
		}

		Element participants = doc.createElement("participants");
		rootElement.appendChild(participants);
		for (Participant p : this.participants) {
			Element participant = doc.createElement("participant");
			participants.appendChild(participant);
			Element username = doc.createElement("username");
			participant.appendChild(username);
			username.appendChild(doc.createTextNode(p.getEmployee().getUsername()));	
			Element name = doc.createElement("name");
			participant.appendChild(name);
			name.appendChild(doc.createTextNode(p.getEmployee().getName()));
			Element state = doc.createElement("state");
			participant.appendChild(state);
			state.appendChild(doc.createTextNode(stateToString(p.getState())));

		}

		Element leader = doc.createElement("leader");
		rootElement.appendChild(leader);
		Element username = doc.createElement("lusername");
		leader.appendChild(username);
		username.appendChild(doc.createTextNode(getLeader().getUsername()));
		Element name = doc.createElement("lname");
		leader.appendChild(name);
		name.appendChild(doc.createTextNode(getLeader().getName()));

		Element description = doc.createElement("description");
		rootElement.appendChild(description);
		description.appendChild(doc.createTextNode(getDescription()));

		TransformerFactory transformerFactory2 = TransformerFactory.newInstance();
		Transformer transformer2 = transformerFactory2.newTransformer();
		// write the content into xml file for testing
		DOMSource source = new DOMSource(doc);
//		StreamResult toFile = new StreamResult(new File("file.xml"));
//		transformer2.transform(source, toFile);


		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		StringWriter stringWriter = new StringWriter();
		Result result = new StreamResult(stringWriter);
		transformer.transform(source, result);
		return stringWriter.getBuffer().toString();
	}

	public static Appointment xmlToAppointment(String xml) {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(xml));
			Document doc = db.parse(is);
			doc.getDocumentElement().normalize();
			//TODO: delete?
			String loc="", roomnr="0";

			Appointment appointment = new Appointment();
			List<Participant> participants = new ArrayList<Participant>();
			NodeList nodeLst = doc.getElementsByTagName("appointment");
			for (int i = 0; i < nodeLst.getLength(); i++) {
				Node nNode = nodeLst.item(i);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) nNode;
					int month = Integer.parseInt(getTagValues("month", element));
					int year = Integer.parseInt(getTagValues("year", element));
					int dayInMonth = Integer.parseInt(getTagValues("dayInMonth", element));
					appointment.setDate(new Date(year, month, dayInMonth));
					String starttime = getTagValues("starttime", element);
					appointment.setStart(Time.parseTime(starttime));
					String endtime = getTagValues("endtime", element);
					appointment.setEnd(Time.parseTime(endtime));
					String subject = getTagValues("subject", element);
					appointment.setSubject(subject);
					int id = Integer.parseInt(getTagValues("id", element));
					appointment.setId(id);
					
					//Element has no attributes, which is why element.hasAttribute("..."); never works
					if (getTagValues("location", element)!=null) {
						loc = getTagValues("location", element);
						appointment.setLocation(loc);
					}
					
					if (getTagValues("roomnr", element)!=null) {
						roomnr = getTagValues("roomnr", element);
						appointment.setRoomNumber((Integer.parseInt(roomnr)));
					}
					
					String description = getTagValues("description", element);
					appointment.setDescription(description);

					NodeList pList = doc.getElementsByTagName("participant");
					for (int j = 0; j < pList.getLength(); j++) {
						Node pNode = pList.item(j);
						if (pNode.getNodeType() == Node.ELEMENT_NODE) {
							Element pElement = (Element) pNode;
							String username = getTagValues("username", pElement);
							String name = getTagValues("name", pElement);
							String state = getTagValues("state",pElement);
							participants.add(new Participant(new Employee(name, username), stringToState(state)));
						}
					}
					String lusername = getTagValues("lusername", element);
					String lname = getTagValues("lname", element);
					appointment.setLeader(new Employee(lname, lusername));

					//Set leader as accepted
					for (Participant part : participants) {
						if (part.getEmployee().getUsername().equals(lusername))
							part.setState(State.ACCEPTED);
					}
					appointment.setParticipants(participants);
				}
			}
			return appointment;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static ArrayList<Appointment> xmlToAppoinmentList(String xml) throws ParserConfigurationException, SAXException, IOException, TimeException{
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		InputSource is = new InputSource(new StringReader(xml));
		Document doc = db.parse(is);
		doc.getDocumentElement().normalize();

		String loc="", roomnr="13";
		
		ArrayList<Appointment> appointmentList = new ArrayList<Appointment>();
		NodeList nodeLst = doc.getElementsByTagName("appointment");
		
		for (int i = 0; i < nodeLst.getLength(); i++) {
			Appointment appointment = new Appointment();
			List<Participant> participants = new ArrayList<Participant>();
			Node nNode = nodeLst.item(i);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) nNode;
				int month = Integer.parseInt(getTagValues("month", element));
				int year = Integer.parseInt(getTagValues("year", element));
				int dayInMonth = Integer.parseInt(getTagValues("dayInMonth", element));
				appointment.setDate(new Date(year, month, dayInMonth));
				String starttime = getTagValues("starttime", element);
				appointment.setStart(Time.parseTime(starttime));
				String endtime = getTagValues("endtime", element);
				appointment.setEnd(Time.parseTime(endtime));
				String subject = getTagValues("subject", element);
				appointment.setSubject(subject);
				int id = Integer.parseInt(getTagValues("id", element));
				appointment.setId(id);
				
				//Element has no attributes, which is why element.hasAttribute("..."); never works
				if (getTagValues("location", element)!=null) {
					loc = getTagValues("location", element);
					appointment.setLocation(loc);
				}
				
				if (getTagValues("roomnr", element)!=null) {
					roomnr = getTagValues("roomnr", element);
					appointment.setRoomNumber((Integer.parseInt(roomnr)));
				}
				
				String description = getTagValues("description", element);
				appointment.setDescription(description);
				String lusername = getTagValues("lusername", element);
				String lname = getTagValues("lname", element);
				Employee l = new Employee(lname, lusername);
				appointment.setLeader(l);
				participants.add(new Participant(l, State.ACCEPTED));
				
				//
				NodeList pList = doc.getElementsByTagName("participant"+i);
				System.out.println("pList lengde: " + pList.getLength());
				for (int j = 0; j < pList.getLength(); j++) {
					Node pNode = pList.item(j);
					if (pNode.getNodeType() == Node.ELEMENT_NODE) {
						Element pElement = (Element) pNode;
						String username = getTagValues("username", pElement);
						String name = getTagValues("name", pElement);
						String state = getTagValues("state", pElement);
						if (username.equals(lusername)) 
							continue;
						participants.add(new Participant(new Employee(name, username), stringToState(state)));
					}
				}

				//Set leader as accepted
				for (Participant part : participants) {
					if (part.getEmployee().getUsername().equals(lusername))
						part.setState(State.ACCEPTED);
				}
				appointment.setParticipants(participants);
				appointmentList.add(appointment);
			}
		}
		return appointmentList;
	}
	
	public static String appointmentListToXML(ArrayList<Appointment> appointmentList){
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder;
		try {
			docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("appointments");
			doc.appendChild(rootElement);
			
			int j = 0;
			for (Appointment a : appointmentList) {
				Element appointment = doc.createElement("appointment");
				rootElement.appendChild(appointment);
				
				Element date = doc.createElement("date");
				appointment.appendChild(date);
				
				Element month = doc.createElement("month");
				date.appendChild(month);
				month.appendChild(doc.createTextNode(a.getDate().getMonth() + ""));
				
				Element year = doc.createElement("year");
				date.appendChild(year);
				year.appendChild(doc.createTextNode(a.getDate().getYear() + ""));
				
				Element dayInMonth = doc.createElement("dayInMonth");
				date.appendChild(dayInMonth);
				dayInMonth.appendChild(doc.createTextNode(a.getDate().getDate()+ ""));
				
				Element startTime = doc.createElement("starttime");
				appointment.appendChild(startTime);
				startTime.appendChild(doc.createTextNode(a.getStart().toString()));
				
				Element endTime = doc.createElement("endtime");
				appointment.appendChild(endTime);
				endTime.appendChild(doc.createTextNode(a.getEnd().toString()));
				
				Element subject = doc.createElement("subject");
				appointment.appendChild(subject);
				subject.appendChild(doc.createTextNode(a.getSubject()));
				
				Element id = doc.createElement("id");
				appointment.appendChild(id);
				id.appendChild(doc.createTextNode(a.getId() + ""));
				if (a.getLocation() != null) {
					Element location = doc.createElement("location");
					appointment.appendChild(location);
					location.appendChild(doc.createTextNode(a.getLocation()));
				}
				if (a.getRoomNumber() != 0) {
					Element roomnr = doc.createElement("roomnr");
					appointment.appendChild(roomnr);
					roomnr.appendChild(doc.createTextNode(a.getRoomNumber()+""));
				}
				
				for (int i = 0; i < a.getParticipants().size(); i++) {
					Participant p = a.getParticipants().get(i);
					Element participant = doc.createElement("participant" + j);
					appointment.appendChild(participant);
					Element username = doc.createElement("username");
					participant.appendChild(username);
					username.appendChild(doc.createTextNode(p.getEmployee().getUsername()));	
					Element name = doc.createElement("name");
					participant.appendChild(name);
					name.appendChild(doc.createTextNode(p.getEmployee().getName()));
					Element state = doc.createElement("state");
					participant.appendChild(state);
					state.appendChild(doc.createTextNode(a.stateToString(p.getState())));
				}
				
				Element leader = doc.createElement("leader");
				appointment.appendChild(leader);
				Element username = doc.createElement("lusername");
				leader.appendChild(username);
				username.appendChild(doc.createTextNode(a.getLeader().getUsername()));
				Element name = doc.createElement("lname");
				leader.appendChild(name);
				name.appendChild(doc.createTextNode(a.getLeader().getName()));
				
				Element description = doc.createElement("description");
				appointment.appendChild(description);
				description.appendChild(doc.createTextNode(a.getDescription()));
				j++;
			}
			
			DOMSource source = new DOMSource(doc);
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer optimusPrime = transformerFactory.newTransformer();
			StringWriter stringWriter = new StringWriter();
			Result result = new StreamResult(stringWriter);
			optimusPrime.transform(source, result);
			
			return stringWriter.getBuffer().toString();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
		return null;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	private static String getTagValues(String sTag, Element element) {
		if (element.getElementsByTagName(sTag).item(0)==null) {
			return null;
		}
		NodeList nList = element.getElementsByTagName(sTag).item(0).getChildNodes();
		Node nValue = (Node) nList.item(0);
		if (nValue==null) {
			return null;
		}
		return nValue.getNodeValue();
	}
	
	public Appointment getCopy() {
		Appointment a = new Appointment(this.leader);
		a.start=this.start;
		a.end=this.end;
		if (this.date!=null) {
			a.date=new Date(this.date.getTime());
		}
		a.subject=this.subject;
		a.id=this.id;
		a.description=this.description;
		a.roomNumber=this.roomNumber;
		a.location=this.location;
		List<Participant> list = new ArrayList<Participant>(this.getParticipants().size());
		for (int i = 0; i < this.getParticipants().size(); i++) {
			list.add(new Participant(this.getParticipants().get(i).getEmployee(), this.getParticipants().get(i).getState()));
		}
		a.participants=list;
		return a;
	}
	
	public Participant getParticipant(String username){
		for (Participant p : participants) {
			if(p.getEmployee().getUsername().equals(username)){
				return p;
			}
		}
		return null;
	}
	
	public static State stringToState(String state){
		if (state.equals("accepted")){
			return State.ACCEPTED;
		} else if(state.equals("denied")){
			return State.DENIED;
		}else{
			return State.PENDING;
		}
	
	}
	
	public String stateToString(State e){
		switch(e){
		case ACCEPTED:
			return "accepted";
		case DENIED:
			return "denied";
		default: return "pending";
		}
	}
}
