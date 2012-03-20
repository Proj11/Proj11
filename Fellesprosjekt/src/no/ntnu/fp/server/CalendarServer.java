package no.ntnu.fp.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.ResultSet;

import no.ntnu.fp.db.Database;

public class CalendarServer {
	
	private ServerSocket serverSocket;
	ObjectOutputStream out;
	ObjectInputStream in;
	String message;
	
	
	
	public CalendarServer() throws Exception{
		serverSocket = new ServerSocket(8000);
		Socket socket;
		while (true) {
//			//Receive something
			socket = serverSocket.accept();
			out = new ObjectOutputStream(socket.getOutputStream());
			out.flush();
			in = new ObjectInputStream(socket.getInputStream());
			
			message = (String) in.readObject();
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
