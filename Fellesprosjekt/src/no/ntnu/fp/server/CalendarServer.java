package no.ntnu.fp.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import no.ntnu.fp.client.Client;
import no.ntnu.fp.db.Database;
import no.ntnu.fp.model.appointment.Appointment;
import no.ntnu.fp.model.appointment.Participant;
import no.ntnu.fp.model.employee.Employee;

public class CalendarServer extends JFrame implements MessageListener {
	
	private ServerSocket serverSocket;
	List<HandleAClient> clients;
	
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
		
		clients=new ArrayList<HandleAClient>();
		serverSocket = new ServerSocket(8000);
		Socket socket;
		while (true) {
			socket = serverSocket.accept();
			
			HandleAClient task = new HandleAClient(socket);
			task.addMessageListener(this);
			clients.add(task);
			new Thread(task).start();
		}	
	}
	
	public static void main(String[] args) throws Exception {
		try {
			new CalendarServer();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void messageReceived(String message) {
		textArea.append(message+"\n");
	}
	
	@Override
	public void appointmentReceived() {
		for (HandleAClient clientHandler : clients) {
			clientHandler.getAllMessagesFromDB();
			clientHandler.fireAppointmentReceived();
		}
	}

	@Override
	public void connectionClosed(HandleAClient clientHandler) {
		clients.remove(clientHandler);
	}

	@Override
	public void appointmentReceived(Appointment a) {
		// TODO Auto-generated method stub
		
	}
}
