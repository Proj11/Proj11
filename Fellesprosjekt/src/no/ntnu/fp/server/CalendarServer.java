package no.ntnu.fp.server;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.ResultSet;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import no.ntnu.fp.db.Database;

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
			case '1':
				boolean b = logon(message.substring(1));
				if (b){
					sendMessage("1");
				}
				else 
					sendMessage("0");
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
	
	public static void main(String[] args) throws Exception {
		try {
			new CalendarServer();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
