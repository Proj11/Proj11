package no.ntnu.fp.model.appointment;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import no.ntnu.fp.model.appointment.Participant.State;
import no.ntnu.fp.model.employee.Employee;
import no.ntnu.fp.model.time.Time;

public class Appointment {
	private Time start;
	private Time end;
	private Date date;
	private String subject;
	private Employee leader;
	private String description;
	private List<Participant> participants;
	
	public Appointment(Employee createdBy) {
		participants = new ArrayList<Participant>();
		participants.add(new Participant(createdBy, State.ACCEPTED));
		this.leader=createdBy;
	}

	public Time getStart() {
		return start;
	}

	public void setStart(Time start) {
		this.start = start;
	}
	
	public Employee getLeader(){
		return this.leader;
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
	
	public String toString() {
		return subject+"\n"+leader.getName();
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
		date.appendChild(doc.createTextNode("12. mars bla bla"));
		
		
		Element startTime = doc.createElement("starttime");
		rootElement.appendChild(startTime);
		startTime.appendChild(doc.createTextNode("starttime"));
		
		Element endTime = doc.createElement("endtime");
		rootElement.appendChild(endTime);
		endTime.appendChild(doc.createTextNode("getEndTIme"));
		
		Element subject = doc.createElement("subject");
		rootElement.appendChild(subject);
		subject.appendChild(doc.createTextNode(getSubject()));
		
		
		
		Element participants = doc.createElement("participants");
		rootElement.appendChild(participants);
		for (Participant p : this.participants) {
			Element username = doc.createElement("username");
			participants.appendChild(username);
			username.appendChild(doc.createTextNode(p.getEmployee().getUsername()));			
		}
		
		Element leader = doc.createElement("leader");
		rootElement.appendChild(leader);
		leader.appendChild(doc.createTextNode(getLeader().getUsername()));
		
		Element description = doc.createElement("description");
		rootElement.appendChild(description);
		description.appendChild(doc.createTextNode(getDescription()));
		
		// write the content into xml file
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File("file.xml"));
 
		// Output to console for testing
		//StreamResult result = new StreamResult(System.out);
 
		transformer.transform(source, result);
 
		System.out.println("File saved!");
		
		
		
		return null;
		
		
	}
	
	public static void main(String[] args) throws ParserConfigurationException, TransformerException {
		Participant p = new Participant(new Employee("lylz"), State.PENDING);
		Appointment a = new Appointment(new Employee("derp"));
		a.setDate(new Date(2012, 3, 20));
		a.setStart(new Time(12, 00));
		a.setEnd(new Time(13, 00));
		a.setDescription("dette blir kult!");
		List<Participant> ps = a.getParticipants();
		ps.add(p);
		a.setParticipants(ps);
		a.setSubject("sub");
		a.toXML();
	}

	public void setLeader(Employee leader) {
		this.leader = leader;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setParticipants(List<Participant> participants) {
		this.participants = participants;
	}

	public String getDescription() {
		return description;
	}

	public List<Participant> getParticipants() {
		return participants;
	}
}
