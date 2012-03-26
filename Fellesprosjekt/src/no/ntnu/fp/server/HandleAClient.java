package no.ntnu.fp.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import no.ntnu.fp.db.Database;
import no.ntnu.fp.model.appointment.Appointment;
import no.ntnu.fp.model.appointment.Participant;
import no.ntnu.fp.model.appointment.Participant.State;
import no.ntnu.fp.model.employee.Employee;
import no.ntnu.fp.model.room.Room;
import no.ntnu.fp.model.time.Time;
import no.ntnu.fp.timeexception.TimeException;

public class HandleAClient extends JFrame implements Runnable {

	private Socket socket;
	ObjectOutputStream out;
	ObjectInputStream in;
	JTextArea textArea; 
	
	public HandleAClient(Socket socket, JTextArea textArea) throws IOException {
		this.socket = socket;
		this.textArea=textArea;
		
	}
	@Override
	public void run() {
		try {
			in = new ObjectInputStream(socket.getInputStream());
			out = new ObjectOutputStream(socket.getOutputStream());
			while (true) {
				String input = (String) in.readObject();
				doSomething(input);
				
			}
		} catch (Exception e) {
			try {
				in.close();
				out.close();
				Thread.currentThread().interrupt();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	private void doSomething(String message) throws Exception {
		textArea.append(message+"\n");
		char id = message.charAt(0);
		switch (id) {
		case Constants.LOGON:
			Employee emp = logon(message.substring(1));
			if (emp != null){
				sendMessage(Constants.TRUE + emp.toXML());
			}
			else 
				sendMessage(Constants.FALSE + "");
			break;
		case Constants.CREATE_APPOINTMENT:
			boolean create = createAppointment(message.substring(1));
			if (create){
				sendMessage(Constants.TRUE + "");
			}
			else 
				sendMessage(Constants.FALSE + "");
			break;
		case Constants.EDIT_APPOINTMENT:
			boolean edit = editAppointment(message.substring(1));
			if (edit){
				sendMessage(Constants.TRUE + "");
			}
			else 
				sendMessage(Constants.FALSE + "");
			break;
		case Constants.DELETE_APPOINTMENT:
			boolean del = deleteAppointment(Integer.parseInt(message.substring(1)));
			if (del){
				sendMessage(Constants.TRUE + "");
			}
			else 
				sendMessage(Constants.FALSE + "");
			break;
		case Constants.GET_EMPLOYEES:
			ArrayList<Employee> empList = getEmployeesFromDB("");
			sendMessage(parseEmployeesToXML(empList));
			break;
		case Constants.GET_ROOMS:
			Appointment a = Appointment.xmlToAppointment(message.substring(1));
			ArrayList<Room> roomList = getAvailableRooms(a);
			sendMessage(parseRoomsToXML(roomList));
			break;
		case Constants.CLOSE_CONNECTION:
			in.close();
			out.close();
			Thread.currentThread().interrupt();
			break;
			
		default:
			break;
		}
	}
	
	public boolean editAppointment(String appointmentString){
		Appointment a = Appointment.xmlToAppointment(appointmentString);
		deleteAppointment(a.getId());
		return createAppointment(appointmentString);
	}

	private void sendMessage(String msg) {
		try {
			out.writeObject(msg);
			out.flush();
			textArea.append("Message sent: "+msg+"\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private ArrayList<Room> getAvailableRooms(Appointment a) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, ParserConfigurationException, TransformerException{
		ArrayList<Room> roomList = new ArrayList<Room>();
		Database db = Database.getDatabase();
		ResultSet rs = db.query("SELECT * FROM MeetingRoom;");
		while (rs.next()){
			int roomnr, size;
			roomnr = Integer.parseInt(rs.getString("roomnr"));
			size = Integer.parseInt(rs.getString("roomsize"));
			if (size >= a.getParticipants().size()) {
				if (isRoomAvailable(roomnr, a))
					roomList.add(new Room(roomnr, size));
			}
		}
		return roomList;
	}
	
	private boolean isRoomAvailable(int roomID, Appointment a) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		Database db = Database.getDatabase();
		ResultSet rs = db.query(
				"SELECT * FROM Appointment AS a JOIN MeetingRoom AS mr on a.roomnr=" + roomID + 
				" AND mr.roomnr=" + roomID + " AND DATE=" + a.getDate());
		while (rs.next()) {
			try {
				Time start = Time.parseTime(rs.getString("starttime"));
				Time end = Time.parseTime(rs.getString("endtime"));
				if ((a.getStart().compareTo(start) < 0) && (a.getEnd().compareTo(start) < 0)) 
					return true;
				if (a.getStart().compareTo(end) > 0)
					return true;			
			} catch (TimeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
	}
	
	public ArrayList<Employee> getEmployeesFromDB(String query) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
		ArrayList<Employee> empList = new ArrayList<Employee>();
		Database db = Database.getDatabase();
		ResultSet rs = db.query("SELECT * FROM Employee WHERE name LIKE '" + query + "%';");
		while (rs.next()){
			String username, name;
			username = rs.getString("username");
			name = rs.getString("name");
			empList.add(new Employee(name, username));
		}
		return empList;
	}
	
	public Appointment getAppointmentFromDB(int appointmentID) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, TimeException, ParserConfigurationException, TransformerException{
		Database db = Database.getDatabase();
		ResultSet rs = db.query("SELECT * FROM Appointment JOIN Employee ON (Appointment.createdBy = Employee.username) WHERE appointmentID= '" + appointmentID + "';");
		String leader="", name="", date="", starttime="", endtime="", subject="", description="", location="", roomnr="";
		while (rs.next()){
			name = rs.getString("name");
			leader = rs.getString("username");
			date = rs.getString("date");
			starttime = rs.getString("starttime");
			endtime = rs.getString("endtime");
			subject = rs.getString("subject");
			description = rs.getString("description");
			if (rs.getString("roomnr") == null)
				location = rs.getString("location");
			else roomnr = rs.getString("roomnr");
		}
		Appointment app = new Appointment(new Employee(name, leader));
		app.setDate(new Date(Long.parseLong(date)));
		app.setId(appointmentID);
		app.setStart(Time.parseTime(starttime));
		app.setEnd(Time.parseTime(endtime));
		app.setSubject(subject);
		app.setDescription(description);
		app.setRoomNumber(Integer.parseInt(roomnr));
		app.setLocation(location);
		ArrayList<Participant> participants = new ArrayList<Participant>();
		participants.add(new Participant(new Employee(name, leader), State.ACCEPTED));
		
		ResultSet rsPart = db.query("SELECT * FROM Participant JOIN Employee ON (Participant.username = Employee.username) WHERE appointmentID= '" + appointmentID + "';");
		String username, emName;
		Participant.State state;
		while (rsPart.next()){
			username = rsPart.getString("username");
			emName = rsPart.getString("name");
			state = (Participant.State.valueOf(rsPart.getString("state")));
			participants.add(new Participant(new Employee(emName, username), state));
		}
		app.setParticipants(participants);
		sendMessage(app.toXML());
		return app;
	}
	
	public ArrayList<Appointment> getAppointmentsFromDB() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, TimeException, ParserConfigurationException, TransformerException{
		Database db = Database.getDatabase();
		ArrayList<Appointment> appointments = new ArrayList<Appointment>();
		ArrayList<String> appointmentSize = new ArrayList<String>();
		ResultSet rs = db.query("SELECT * FROM Appointment;");
		while (rs.next()){
			String appointment = rs.getString("appointmentID");
			appointmentSize.add(appointment);
		}
		for (String stringAppointmentID : appointmentSize) {
			int appointmentID = Integer.parseInt(stringAppointmentID);
			appointments.add(getAppointmentFromDB(appointmentID));
		}
		//TODO: Sende dette til klienten.
		//sendMessage(appointments);
		return appointments;
	}
	
	public String parseEmployeesToXML(ArrayList<Employee> empList) throws ParserConfigurationException, TransformerException, InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
		return Employee.allEmployeesToXML(empList);
	}
	public String parseRoomsToXML(ArrayList<Room> roomList) throws ParserConfigurationException, TransformerException, InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
		return Room.allRoomsToXML(roomList);
	}
	
	public static Employee logon(String logonString) throws Exception{
		String[] logonArray = logonString.split("-");
		String username = logonArray[0];
		String password = logonArray[1];
		Employee emp;
		Database db = Database.getDatabase();
		ResultSet rs = db.query("SELECT * FROM Employee WHERE username='" + username + "' AND password='" + password + "'");
		if (rs.next()){
			return new Employee(rs.getNString("name"), rs.getString("username"));
		}
		return null;
	}
	
	public boolean createAppointment(String appointmentString){
		Appointment a = Appointment.xmlToAppointment(appointmentString);
		try {
			Database db = Database.getDatabase();
			int id = db.insertWithIdReturn("INSERT INTO Appointment (date, starttime, endtime, subject, location, description, roomnr, createdBy) values ('"
			+ a.getDate().getTime() + "', '" + a.getStart().toString() + "', '" + a.getEnd().toString() + "', '" + a.getSubject() + "', '"
					+ a.getLocation() + "', '" + a.getDescription() + "', '" + a.getRoomNumber() + "', '" + a.getLeader().getName() + "');");
					for (Participant p : a.getParticipants()){
						db.insert("INSERT INTO Participant (username, appointmentID, state) values" + 
						"('" + p.getEmployee().getUsername() + "', '" + id + "', 'PENDING');");
						db.insert("UPDATE Participant SET state = 'ACCEPTED' WHERE username = '" + a.getLeader().getUsername() +"';");
					}
					sendMessage(a.toXML());
			return true;
		}
		catch (Exception exception){
			exception.printStackTrace();
			return false;
		}
	}
	
	public static boolean deleteAppointment(int appointmentID){
		try {
			Database db = Database.getDatabase();
			db.insert("DELETE FROM Appointment WHERE appointmentID = '" + appointmentID + "';");
			return true;
		}
		catch (Exception deleteException){
			deleteException.printStackTrace();
			return false;
		}
	}
}
