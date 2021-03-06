package no.ntnu.fp.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

import no.ntnu.fp.gui.CalendarClient;
import no.ntnu.fp.model.appointment.Appointment;
import no.ntnu.fp.model.appointment.Participant;
import no.ntnu.fp.model.employee.Employee;
import no.ntnu.fp.model.message.Message;
import no.ntnu.fp.model.room.Room;
import no.ntnu.fp.server.Constants;
import no.ntnu.fp.timeexception.TimeException;

public class Client {
	
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private CalendarClient calendar;
	
	public Client(CalendarClient calendar) throws UnknownHostException, IOException, ClassNotFoundException, InterruptedException {
		Socket socket = new Socket("localhost", 8000);
		out = new ObjectOutputStream(socket.getOutputStream());
		out.flush();
		in = new ObjectInputStream(socket.getInputStream());
		this.calendar = calendar;
	}
	
	public void setCalendarClient(CalendarClient calendar) {
		this.calendar = calendar;
	}
	
	/**
	 * Method to send a message (String) to the Server.
	 * @param msg
	 */
	public void sendMessage(String msg) {
		try {
			System.out.println("Sending message: "+msg);
			out.writeObject(msg);
			out.flush();
			System.out.println("Message successfully sent");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void doSomething(String message){
		char id = message.charAt(0);
		switch (id){
		case Constants.RECEIVE_MESSAGE_FROM_SERVER:
			System.out.println("Client linje 64");
			calendar.fireMessagesChanged();
			break;
		}
	}
	
	public synchronized String receive() throws IOException, ClassNotFoundException {
		String s=(String) in.readObject();
		System.out.println("Client recieve(): "+s);
		doSomething(s);
		return s;
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
			sendMessage(Constants.SEND_MESSAGE_TO_CLIENT +"");
			return true;
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
		return false;
		//TODO: code to send message to participants
	}
	
	public void deleteAppointment(int appointmentID){
		sendMessage(Constants.DELETE_APPOINTMENT + "" + appointmentID);
	}
	
	public List<Appointment> getAppointmentList(String username) throws ParserConfigurationException, SAXException, TimeException{
		String appointmentsAsXML;
		try{
			sendMessage(Constants.GET_APPOINTMENTS + username);
			appointmentsAsXML = receive();
			ArrayList<Appointment> allApps = Appointment.xmlToAppoinmentList(appointmentsAsXML);
			ArrayList<Appointment> appList = new ArrayList<Appointment>();
			for (Appointment a : allApps) {
				for (Participant p : a.getParticipants()) {
					if (p.getEmployee().getUsername().equals(username)) {
						appList.add(a);
						break;
					}
				}
				continue;
			}
			return appList;
		} catch (ClassNotFoundException e){
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		}
		return null;
	}
	
	/*public Message createMessage(String messageString, int i){
		String createMsgAsXML;
		try {
			sendMessage(Constants.CREATE_MESSAGE + messageString + "");
			createMsgAsXML = receive();
			return Message.xmlToMessage(createMsgAsXML);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}*/
	
	public void deleteMessage(int messageID){
		sendMessage(Constants.DELETE_MESSAGE + "" +messageID);
	}
	
//	public Message getMessage(int messageID){
//		String messageAsXML;
//		try {
//			sendMessage(Constants.GET_MESSAGE_FROM_DB + "" + messageID );
//			messageAsXML = receive();
//			return Message.xmlToMessage(messageAsXML);
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
	
	public List<Message> getMessages(String username){
		String messagesAsXML;
		try {
			sendMessage(Constants.GET_MESSAGES_FROM_DB+""+username);
			messagesAsXML = receive();
			ArrayList<Message> allMsgs = Message.xmlToMessageList(messagesAsXML);
			ArrayList<Message> msgList = new ArrayList<Message>();
			for (Message m : allMsgs) {
				if (m.getRecipient().getUsername().equals(username)){
					msgList.add(m);
				}
			}
			return msgList;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
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
	
	public void sendState(int appID, String state, String username){
		String msg = Constants.SEND_STATE + ""+appID+"-"+state+"-"+username;
		sendMessage(msg);
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
	
}
