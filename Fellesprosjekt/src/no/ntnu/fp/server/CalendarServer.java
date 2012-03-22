package no.ntnu.fp.server;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import no.ntnu.fp.db.Database;
import no.ntnu.fp.model.appointment.Appointment;
import no.ntnu.fp.model.appointment.Participant;
import no.ntnu.fp.model.appointment.Participant.State;
import no.ntnu.fp.model.employee.Employee;
import no.ntnu.fp.model.time.Time;

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
			textArea.append(message+"\n");
			char id = message.charAt(0);
			
//			//Do something
			switch (id) {
			case '2':
				boolean b = logon(message.substring(1));
				if (b){
					sendMessage(Constants.TRUE);
				}
				else 
					sendMessage(Constants.FALSE);
				break;
			case '3':
				boolean c = createAppointment(message.substring(1));
				if (c){
					sendMessage(Constants.TRUE);
				}
				else sendMessage(Constants.FALSE);
				break;
			case '4':
				getEmployeesFromDB();
				break;

			default:
				break;
			}
			
			out.close();
			in.close();
			socket.close();
			textArea.append("Connection closed\n");
				
		}
		
	}
	
	private void sendMessage(String msg) {
		try {
			out.writeObject(msg);
			out.flush();
			textArea.append("Message sent: "+msg+"\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static ArrayList<Employee> getEmployeesFromDB() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
		ArrayList<Employee> empList = new ArrayList<Employee>();
		Database db = Database.getDatabase();
		ResultSet rs = db.query("SELECT * FROM Employee;");
		while (rs.next()){
			String username, name;
			username = rs.getString("username");
			name = rs.getString("name");
			empList.add(new Employee(name, username));
		}
		return empList;
	}
	
	public static String getEmployees(){
		return null;
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
