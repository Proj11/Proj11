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
		textArea.setLineWrap(true);
		JScrollPane scrollPane=new JScrollPane(textArea);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		add(scrollPane);
		pack();
		setVisible(true);
		
		serverSocket = new ServerSocket(8000);
		Socket socket;
		int clientNo = 0;
		while (true) {
			socket = serverSocket.accept();
			
//			out = new ObjectOutputStream(socket.getOutputStream());
//			out.flush();
//			in = new ObjectInputStream(socket.getInputStream());
//			message = (String) in.readObject();
			
			HandleAClient task = new HandleAClient(socket, textArea);
			new Thread(task).start();
			clientNo++;
			
			
				
		}
//		out.close();
//		in.close();
//		socket.close();
//		textArea.append("Connection closed\n");
		
	}
	
	private String receive() throws IOException, ClassNotFoundException {
		return (String) in.readObject();
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
