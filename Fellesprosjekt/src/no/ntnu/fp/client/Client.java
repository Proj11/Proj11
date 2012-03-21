package no.ntnu.fp.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import no.ntnu.fp.model.appointment.Appointment;
import no.ntnu.fp.model.employee.Employee;

public class Client {
	
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private String message;
	
	public Client() throws UnknownHostException, IOException, ClassNotFoundException, InterruptedException {
		Socket socket = new Socket("localhost", 8000);
		out = new ObjectOutputStream(socket.getOutputStream());
		out.flush();
		in = new ObjectInputStream(socket.getInputStream());

	}
	
	/**
	 * Method to send a message (String) to the Server.
	 * @param msg
	 */
	private void sendMessage(String msg) {
		try {
			out.writeObject(msg);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public boolean logOn(String username, String password) throws IOException, ClassNotFoundException{
		sendMessage("2" + username + "-" + password);
		String result = (String)in.readObject();
		if (result.charAt(0) == '1')
			return true;
		return false;
	}
	
	public boolean createAppointment(Appointment appointment){
		try {
			sendMessage("3"+appointment.toXML());
			return true;
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
	public static void main(String[] args) throws Exception {
		try {
			new Client();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
