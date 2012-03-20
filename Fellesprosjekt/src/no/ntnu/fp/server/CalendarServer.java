package no.ntnu.fp.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.ResultSet;

import no.ntnu.fp.db.Database;
import no.ntnu.fp.net.co.SendTimer;

public class CalendarServer {
	
	private ServerSocket serverSocket;
	ObjectOutputStream out;
	ObjectInputStream in;
	String message;
	
	
	@SuppressWarnings("deprecation")
	public CalendarServer() throws IOException {
		serverSocket = new ServerSocket(8000);
		Socket socket = serverSocket.accept();
//		DataInputStream inputFromClient = new DataInputStream(socket.getInputStream());
//		DataOutputStream outputToClient = new DataOutputStream(socket.getOutputStream());
		while (true) {
			
			out = new ObjectOutputStream(socket.getOutputStream());
			out.flush();
			in = new ObjectInputStream(socket.getInputStream());
			
			sendMessage("Connection successful");
			
			
			
			
			
			
			
//			//Receive something
//			
//			//Do something
//			
//			//Send something
//			if (input.equals("test"))
//				outputToClient.writeChars("Dette er en test");
//			else
//				outputToClient.writeChars("Dette er ikke en test");
//			outputToClient.flush();
		}
		
	}
	
	private void sendMessage(String msg) {
		try {
			out.writeObject(msg);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static boolean logon(String username, String password) throws Exception{
		Database db = Database.getDatabase();
		ResultSet rs = db.query("SELECT * FROM Employee WHERE username='" + username + "' AND password='" + password + "'");
		if (rs.next())
			return true;
		return false;
	}
	
	public static void main(String[] args) {
		try {
			new CalendarServer();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
