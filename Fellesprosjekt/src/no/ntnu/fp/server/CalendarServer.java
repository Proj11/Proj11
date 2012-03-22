package no.ntnu.fp.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import no.ntnu.fp.db.Database;
import no.ntnu.fp.model.appointment.Appointment;
import no.ntnu.fp.model.appointment.Participant;
import no.ntnu.fp.model.employee.Employee;

public class CalendarServer extends JFrame {
	
	private ServerSocket serverSocket;
	ObjectOutputStream out;
	ObjectInputStream in;
	String message;
	
	JTextArea textArea;
	
	public CalendarServer() throws Exception{
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Calendar Server");
		
		textArea = new JTextArea();
		textArea.setColumns(50);
		textArea.setRows(30);
		JScrollPane scrollPane=new JScrollPane(textArea);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		add(scrollPane);
		
		pack();
		setVisible(true);
		
		serverSocket = new ServerSocket(8000);
		Socket socket;
		while (true) {
//			//Receive something
			socket = serverSocket.accept();
			out = new ObjectOutputStream(socket.getOutputStream());
			out.flush();
			in = new ObjectInputStream(socket.getInputStream());
			
			message = (String) in.readObject();
			
			out.close();
			in.close();
			socket.close();
			textArea.append("Connection closed\n");
				
		}
		
	}
	
	private String receive() throws IOException, ClassNotFoundException {
		return (String) in.readObject();
	}
	
	private void doSomething(String message) throws Exception {
		textArea.append(message+"\n");
		char id = message.charAt(0);
		switch (id) {
		case Constants.LOGON:
			boolean blogon = logon(message.substring(1));
			if (blogon){
				sendMessage(Constants.TRUE + "");
			}
			else 
				sendMessage(Constants.FALSE + "");
			break;
		case Constants.CREATE_APPOINTMENT:
			boolean create = createAppointment(message.substring(1));
			if (create){
				sendMessage(Constants.TRUE + "");
			}
			else sendMessage(Constants.FALSE + "");
			break;
		case Constants.EDIT_APPOINTMENT:
			boolean edit = editAppointment(message.substring(1));
			if (edit){
				sendMessage(Constants.TRUE + "");
			}
			else sendMessage(Constants.FALSE + "");
			break;
		case Constants.DELETE_APPOINTMENT:
			boolean del = deleteAppointment(Integer.parseInt(message.substring(1)));
			if (del){
				sendMessage(Constants.TRUE + "");
			}
			else sendMessage(Constants.FALSE + "");
			break;
		case Constants.GET_EMPLOYEES:
			ArrayList<Employee> empList = getEmployeesFromDB("");
			sendMessage(parseEmployeesToXML(empList));
			break;
			
		default:
			break;
		}
	}
	
	public static boolean editAppointment(String appointmentString, int id){
		Appointment a = Appointment.xmlToAppointment(appointmentString);
		try {
			Database db = Database.getDatabase();
			db.insert("UPDATE Appointment SET (date, starttime, endtime, subject, location, description, roomnr, createdBy) values ('"
			+ a.getDate().getTime() + "', '" + a.getStart().toString() + "', '" + a.getEnd().toString() + "', '" + a.getSubject() + "', '"
					+ a.getLocation() + "', '" + a.getDescription() + "', '" + a.getRoomNumber() + "', '" + a.getLeader().getName() + "');");
					for (Participant p : a.getParticipants()){
						db.insert("INSERT INTO Participant (username, appointmentID, state) values" + 
						"('" + p.getEmployee().getUsername() + "', '" + id + "', 'PENDING');");
						db.insert("UPDATE Participant SET state = 'ACCEPTED' WHERE username = '" + a.getLeader().getUsername() +"';");
					}
			return true;
		}
		catch (Exception exception){
			exception.printStackTrace();
			return false;
		}
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
	
	public static ArrayList<Employee> getEmployeesFromDB(String query) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
		ArrayList<Employee> empList = new ArrayList<Employee>();
		Database db = Database.getDatabase();
		ResultSet rs = db.query("SELECT * FROM Employee WHERE name LIKE '" + query + "';");
		while (rs.next()){
			String username, name;
			username = rs.getString("username");
			name = rs.getString("name");
			empList.add(new Employee(name, username));
		}
		return empList;
	}
	
	public static String parseEmployeesToXML(ArrayList<Employee> empList) throws ParserConfigurationException, TransformerException, InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
		return Employee.allEmployeesToXML(empList);
	}
	
	public static boolean logon(String logonString) throws Exception{
		String[] logonArray = logonString.split("-");
		String username = logonArray[0];
		String password = logonArray[1];
		Database db = Database.getDatabase();
		ResultSet rs = db.query("SELECT * FROM Employee WHERE username='" + username + "' AND password='" + password + "'");
		if (rs.next()){
			return true;
		}
		return false;
	}
	
	public static boolean createAppointment(String appointmentString){
		Appointment a = Appointment.xmlToAppointment(appointmentString);
		System.out.println("5: "+a.getRoomNumber());
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
	
	public static void main(String[] args) throws Exception {
//		Employee emp = new Employee("herp", "derp");
//		Employee emp2 = new Employee("derp", "herp");
//		Appointment appo = new Appointment(emp);
//		appo.setDate(new java.util.Date(2012-1900, 3, 20));
//		appo.setStart(new Time(12, 00));
//		appo.setEnd(new Time(16, 00));
//		appo.setSubject("durr");
//		appo.setRoomNumber(123);
// 		appo.getParticipants().add(new Participant(emp2, State.PENDING));
//		appo.setDescription("lol hvis dette funker");
		//CalendarServer.createAppointment(appo.toXML());
		//CalendarServer.deleteAppointment(0);
		
		try {
			new CalendarServer();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}
