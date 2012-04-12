package no.ntnu.fp.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import no.ntnu.fp.db.Database;
import no.ntnu.fp.model.appointment.Appointment;
import no.ntnu.fp.model.appointment.Participant;
import no.ntnu.fp.model.appointment.Participant.State;
import no.ntnu.fp.model.employee.Employee;
import no.ntnu.fp.model.message.Message;
import no.ntnu.fp.model.room.Room;
import no.ntnu.fp.model.time.Time;
import no.ntnu.fp.timeexception.TimeException;

public class HandleAClient extends JFrame implements Runnable {

	private Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private Employee user;
	private List<MessageListener> messageListeners;
	
	public HandleAClient(Socket socket) throws IOException {
		this.socket = socket;
		messageListeners=new ArrayList<MessageListener>();
	}
	
	@Override
	public void run() {
		try {
			in = new ObjectInputStream(socket.getInputStream());
			out = new ObjectOutputStream(socket.getOutputStream());
			while (true) {
				String input = (String) in.readObject();
				fireMessageReceived(input);
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
		char id = message.charAt(0);
		switch (id) {
		case Constants.LOGON:
			Employee emp = logon(message.substring(1));
			
			if (emp != null){
				user=emp;
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
			boolean del = deleteAppointment(message.substring(1));
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
		case Constants.GET_APPOINTMENTS:
			ArrayList<Appointment> appList = getAppointmentsFromDB();
			sendMessage(parseAppointmentsToXML(appList));
			break;
			
		case Constants.GET_MESSAGES_FROM_DB:
			ArrayList<Message> msgList = getAllMessagesFromDB();
			sendMessage(parseMessagesToXML(msgList));
			break;
		case Constants.CLOSE_CONNECTION:
			in.close();
			out.close();
			fireConnectionClosed();
			Thread.currentThread().interrupt();
			break;
			
		case Constants.SEND_STATE:
			String[] s = message.substring(1).split("-");
			updateState(Integer.parseInt(s[0]), s[1], s[2]);
			break;
			
		case Constants.DELETE_MESSAGE:
			boolean delMsg = deleteMessage(message.substring(1));
			if (delMsg)
				sendMessage(Constants.TRUE + "");
			else sendMessage(Constants.FALSE +"");
			break;
		default:
			break;
		}
	}
	
	public Employee getUser() {
		return user;
	}
	
	protected void fireAppointmentReceived() {
		sendMessage(Constants.RECEIVE_MESSAGE_FROM_SERVER+"");
	}
	
	protected void fireMessageReceived(String message) {
		for (MessageListener listener : messageListeners) {
			listener.messageReceived(message);
		}
	}
	
	protected void fireAppointmentReceived(Appointment a) {
		for (MessageListener listener : messageListeners) {
			listener.appointmentReceived(a);
		}
	}
	
	protected void fireConnectionClosed() {
		for (MessageListener listener : messageListeners) {
			listener.connectionClosed(this);
		}
	}
	
	public void addMessageListener(MessageListener listener) {
		messageListeners.add(listener);
	}
	
	public void removeMessageListener(MessageListener listener) {
		messageListeners.remove(listener);
	}

	private void sendMessage(String msg) {
		try {
			out.writeObject(msg);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void updateState(int appID, String state, String username) {
		try {
			Database db = Database.getDatabase();
			db.insert("UPDATE Participant SET state='" + state.toUpperCase() + 
					"' WHERE username='" + username + "' AND appointmentID=" + appID);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private ArrayList<Room> getAvailableRooms(Appointment a) {
		ArrayList<Room> roomList = new ArrayList<Room>();
		Database db;
		try {
			db = Database.getDatabase();
			ResultSet rs = db.query("SELECT * FROM MeetingRoom");
			while (rs.next()){
				int roomnr, size;
				roomnr = Integer.parseInt(rs.getString("roomnr"));
				size = Integer.parseInt(rs.getString("roomsize"));
				if (size >= a.getParticipants().size()) {
					if (isRoomAvailable(roomnr, a)) {
						roomList.add(new Room(roomnr, size));
						System.out.println("Romnr ledig: " + roomnr);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return roomList;
	}
	
	private boolean isRoomAvailable(int roomID, Appointment a) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		Database db = Database.getDatabase();
		ResultSet rs = db.query(
				"SELECT * FROM Appointment AS a JOIN MeetingRoom AS mr on a.roomnr=" + roomID + 
				" AND mr.roomnr=" + roomID);
		boolean available = true;
		while (rs.next()) {
			available = true;
			try {
				Time start = Time.parseTime(rs.getString("starttime"));
				Time end = Time.parseTime(rs.getString("endtime"));
				Date date = new Date(rs.getLong("date"));
				if (date.getTime() == a.getDate().getTime()) {
					if ((a.getStart().compareTo(start) >= 0) && (a.getStart().compareTo(end) < 0)) 
						return false;
					if (a.getStart().compareTo(start) < 0 && a.getEnd().compareTo(end) > 0)
						return false;
					if ((a.getEnd().compareTo(start) >= 0) && (a.getEnd().compareTo(end) < 0)) {
						return false;
					}
					
					if (rs.getInt("roomnr") == roomID) {
						System.out.println(roomID + " er opptatt!");
						available = false;
					}
				}
			} catch (TimeException e) {
				e.printStackTrace();
			}
		}
		if (available)
			return true;
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
		String leader="", name="", starttime="00:00", endtime="00:00", subject="", description="", location="", roomnr="0";
		Date date = null;
		if (rs.next()){
			name = rs.getString("name");
			leader = rs.getString("username");
			date = new Date(rs.getLong("date"));
			starttime = rs.getString("starttime");
			endtime = rs.getString("endtime");
			subject = rs.getString("subject");
			description = rs.getString("description");
			if (rs.getString("roomnr") == null)
				location = rs.getString("location");
			else roomnr = rs.getString("roomnr");
		}
		Appointment app = new Appointment(new Employee(name, leader));
		app.setDate(date);
		app.setId(appointmentID);
		app.setStart(Time.parseTime(starttime));
		app.setEnd(Time.parseTime(endtime));
		app.setSubject(subject);
		app.setDescription(description);
		app.setRoomNumber(Integer.parseInt(roomnr));
		if (location != null)
			app.setLocation(location);
		else app.setLocation(location);
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
		return app;
	}
	
	public ArrayList<Appointment> getAppointmentsFromDB(){
		Database db;
		try {
			db = Database.getDatabase();
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
			return appointments;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (TimeException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	public String parseEmployeesToXML(ArrayList<Employee> empList) throws ParserConfigurationException, TransformerException, InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
		return Employee.allEmployeesToXML(empList);
	}
	public String parseRoomsToXML(ArrayList<Room> roomList) throws ParserConfigurationException, TransformerException, InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
		return Room.allRoomsToXML(roomList);
	}
	
	public String parseAppointmentsToXML(ArrayList<Appointment> appList) throws ParserConfigurationException, TransformerException, InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
		return Appointment.appointmentListToXML(appList);
	}
	
	public static Employee logon(String logonString) throws Exception{
		String[] logonArray = logonString.split("-");
		String username = logonArray[0];
		String password = logonArray[1];
		Database db = Database.getDatabase();
		ResultSet rs = db.query("SELECT * FROM Employee WHERE username='" + username + "' AND password='" + password + "'");
		if (rs.next()){
			return new Employee(rs.getNString("name"), rs.getString("username"));
		}
		return null;
	}
	
	private boolean isLeader(Participant p, Employee l){
		if (p.getEmployee().getUsername().equals(l.getUsername())){
			return true;
		}
		return false;
	}
	
	public boolean createAppointment(String appointmentString){
		Appointment a = Appointment.xmlToAppointment(appointmentString);
		try {
			Database db = Database.getDatabase();
			int id = db.insertWithIdReturn("INSERT INTO Appointment (date, starttime, endtime, subject, location, description, roomnr, createdBy) values ('"
			+ a.getDate().getTime() + "', '" + a.getStart().toString() + "', '" + a.getEnd().toString() + "', '" + a.getSubject() + "', '"
					+ a.getLocation() + "', '" + a.getDescription() + "', '" + a.getRoomNumber() + "', '" + a.getLeader().getUsername() + "');");
					for (Participant p : a.getParticipants()){
						db.insert("INSERT INTO Participant (username, appointmentID, state) values" + 
						"('" + p.getEmployee().getUsername() + "', '" + id + "', 'PENDING');");
						db.insert("UPDATE Participant SET state = 'ACCEPTED' WHERE username = '" + a.getLeader().getUsername() +"';");
						if (isLeader(p, a.getLeader())){
							continue;
						}
						db.insert("INSERT INTO Message (recipient, messageCreatedBy,  appointmentID, messageText) values " +
								"('" + p.getEmployee().getUsername() + "', '" + a.getLeader().getUsername() + "', '" + id + "', 'You have been invited to a meeting.');");
					}
			fireAppointmentReceived();
			return true;
		}
		catch (Exception exception){
			exception.printStackTrace();
			return false;
		}
	}
	
	public boolean createAppointment(String appointmentString, int appID){
		Appointment a = Appointment.xmlToAppointment(appointmentString);
		try {
			Database db = Database.getDatabase();
			db.insert("INSERT INTO Appointment (appointmentID, date, starttime, endtime, subject, location, description, roomnr, createdBy) values ('"
			+ appID + "', '"+ a.getDate().getTime() + "', '" + a.getStart().toString() + "', '" + a.getEnd().toString() + "', '" + a.getSubject() + "', '"
					+ a.getLocation() + "', '" + a.getDescription() + "', '" + a.getRoomNumber() + "', '" + a.getLeader().getUsername() + "');");
					for (Participant p : a.getParticipants()){
						db.insert("INSERT INTO Participant (username, appointmentID, state) values" + 
						"('" + p.getEmployee().getUsername() + "', '" + appID + "', 'PENDING');");
						db.insert("UPDATE Participant SET state = 'ACCEPTED' WHERE username = '" + a.getLeader().getUsername() +"';");
					}
			return true;
		}
		catch (Exception exception){
			exception.printStackTrace();
			return false;
		}
	}
	
	public boolean deleteAppointment(String appointmentID){
		try {
			Database db = Database.getDatabase();
			System.out.println(appointmentID);
			db.insert("DELETE FROM Appointment WHERE appointmentID = '" + appointmentID + "';");
			return true;
		}
		catch (Exception deleteException){
			deleteException.printStackTrace();
			return false;
		}
	}
	
	public boolean editAppointment(String appointmentString){
		Appointment a = Appointment.xmlToAppointment(appointmentString);
		int appID = a.getId();
		deleteAppointment(a.getId()+"");
		return createAppointment(appointmentString, appID);
	}
	
	/*public boolean createMessage(String messageString, int i){
		Message message = Message.xmlToMessage(messageString);
		String sentMessage=null;
		String invitedMessage = "You have been invited to a meeting.";
		String deniedMessage = "Participant " + message.getMessageCreatedBy().getName() + " has declined your invitation.";
		String deletedMessage = message.getMessageCreatedBy().getName() + " has deleted an appointment which affects you.";
		if (i == 1){
			sentMessage = invitedMessage;
		}
		if (i == 2){
			sentMessage = deniedMessage;
		}
		if (i == 3){
			sentMessage = deletedMessage;
		}
		try {
			Database db = Database.getDatabase();
			int mId = db.insertWithIdReturn("INSERT INTO Message (recipient, appointmentID, messageCreatedBy messageText)" +
					"values ('" + message.getRecipient().getUsername() +"', '" + message.getAppointmentId() +
					"', '" + message.getMessageCreatedBy().getUsername() + "', '" + sentMessage + "');");
			message.setMessageID(mId);
			return true;
		}
		catch (Exception createMessageException) {
			createMessageException.printStackTrace();
			return false;
		}
	}*/
	
	public boolean deleteMessage(String mId) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
		Database db = Database.getDatabase();
		try {
			db.insert("DELETE FROM Message WHERE messageID = '" + Integer.parseInt(mId) + "';");
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public Message getMessageFromDB(int messageID) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, ParserConfigurationException, TransformerException{
		Database db = Database.getDatabase();
		Message messageFromDB = new Message();
		String createdBy="", recipient="", createdByName="", recipientName="", appointmentID="", messageText="";
		ResultSet rs = db.query("SELECT * FROM Message JOIN Employee ON (Message.messageCreatedBy = Employee.username) WHERE messageID = '" + messageID + "';");
		if (rs.next()){
			createdBy = rs.getString("messageCreatedBy");
			createdByName = rs.getString("name");
			if (rs.getString("appointmentID") == null)
				appointmentID = "0";
			else appointmentID = rs.getString("appointmentID");
			messageText = rs.getString("messageText");
		}
		ResultSet rs2 = db.query("SELECT * FROM Message JOIN Employee ON (Message.recipient = Employee.username) WHERE messageID = '" + messageID + "';");
		if (rs2.next()){
			recipient = rs2.getString("recipient");
			recipientName = rs2.getString("name");
		}
		messageFromDB.setMessageID(messageID);
		messageFromDB.setMessageCreatedBy(new Employee(createdByName, createdBy));
		messageFromDB.setRecipient(new Employee(recipientName, recipient));
		messageFromDB.setMessageText(messageText);
		messageFromDB.setAppointmentId(Integer.parseInt(appointmentID));
		
		return messageFromDB;
	}
	
	public ArrayList<Message> getAllMessagesFromDB(){
		ArrayList<Message> messages = new ArrayList<Message>();
		try {
			Database db = Database.getDatabase();
			ArrayList<String> messagesSize = new ArrayList<String>();
			ResultSet rs = db.query("SELECT * FROM Message;");
			while (rs.next()){
				String messageID = rs.getString("messageID");
				messagesSize.add(messageID);
			}
			for (String stringMessageID : messagesSize) {
				int messageID = Integer.parseInt(stringMessageID);
				messages.add(getMessageFromDB(messageID));
			}
			return messages;
		}
		catch (Exception getAllMsgsException){
			getAllMsgsException.printStackTrace();
			return null;
		}
	}
	
	public ArrayList<Message> getAllMessagesFromDBWhere(int appointmentID) {
		ArrayList<Message> messages = new ArrayList<Message>();
		try {
			Database db = Database.getDatabase();
			ArrayList<String> messagesSize = new ArrayList<String>();
			ResultSet rs = db.query("SELECT * FROM Message WHERE appointmentID = '" + appointmentID + "';");
			while (rs.next()){
				String messageID = rs.getString("messageID");
				messagesSize.add(messageID);
			}
			for (String stringMessageID : messagesSize) {
				int messageID = Integer.parseInt(stringMessageID);
				messages.add(getMessageFromDB(messageID));
			}
			return messages;
		}
		catch (Exception getAllMsgsException){
			getAllMsgsException.printStackTrace();
			return null;
		}
	}
	
	public String parseMessagesToXML(ArrayList<Message> msgList) throws ParserConfigurationException, TransformerException, InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
		return Message.allMessagesToXML(msgList);
	}
}
