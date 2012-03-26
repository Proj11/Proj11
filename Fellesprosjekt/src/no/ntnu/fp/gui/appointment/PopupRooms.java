package no.ntnu.fp.gui.appointment;

import java.util.List;

import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import no.ntnu.fp.client.Client;
import no.ntnu.fp.model.appointment.Appointment;
import no.ntnu.fp.model.room.Room;

public class PopupRooms {
	
private Room room;
	
	public PopupRooms(Client client, int size, Appointment a){
		
		List<Room> rooms;
		try {
			rooms = client.getRooms(a);
			Room[] roomsArray = new Room[rooms.size()];
			for (int i=0; i<rooms.size(); i++){
				roomsArray[i]=rooms.get(i);
			}
			if (rooms.size() < 1) {
				System.out.println("Ingen tilgjenglige rom");
			}
			room = (Room)JOptionPane.showInputDialog(null, "Select Room", "Rooms", JOptionPane.QUESTION_MESSAGE,null, roomsArray, roomsArray[0]);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public Room getRoom(){
		return room;
	}
	
}
