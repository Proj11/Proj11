package no.ntnu.fp.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import no.ntnu.fp.model.appointment.Appointment;
import no.ntnu.fp.model.employee.Employee;
import no.ntnu.fp.model.room.Room;
import no.ntnu.fp.server.Constants;

public class Client {
	
	private ObjectOutputStream out;
	private ObjectInputStream in;
	
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
	public void sendMessage(String msg) {
		try {
			out.writeObject(msg);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public synchronized String receive() throws IOException, ClassNotFoundException {
		return (String) in.readObject();
	}
	
	public Employee logOn(String username, String password) throws IOException, ClassNotFoundException{
		sendMessage(Constants.LOGON + username + "-" + password);
		String result = (String)in.readObject();
		if (result.charAt(0) == '1')
			return Employee.fromXML(result.substring(1));
		return null;
	}
	
	public boolean createAppointment(Appointment appointment){
		try {
			sendMessage(Constants.CREATE_APPOINTMENT + appointment.toXML());
			return true;
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
		return false;
		//TODO: code to send message to participants
	}
	
	public List<Employee> getEmployees() {
		String employeesAsXML;
		try {
			sendMessage(Constants.GET_EMPLOYEES+"");
			employeesAsXML = receive();
			return Employee.xmlToEmployeeList(employeesAsXML);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//rooms --> xml bitches
	public List<Room> getRooms(Appointment a) throws ParserConfigurationException, TransformerException {
		String roomsAsXML;
		try {
			sendMessage(Constants.GET_ROOMS + a.toXML());
			roomsAsXML = receive();
			return Room.xmlToRoomList(roomsAsXML);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean editAppointment(Appointment a) {
		try {
			sendMessage(Constants.EDIT_APPOINTMENT + a.toXML());
			return true;
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public void close() throws IOException {
		sendMessage(Constants.CLOSE_CONNECTION + "");
		out.close();
		in.close();
	}
	
	public static void main(String[] args) throws Exception {
		try {
			new Client();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
