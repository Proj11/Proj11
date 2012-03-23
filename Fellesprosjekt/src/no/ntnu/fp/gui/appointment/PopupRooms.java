package no.ntnu.fp.gui.appointment;

import java.util.List;

import javax.swing.JList;
import javax.swing.JOptionPane;

import no.ntnu.fp.client.Client;
import no.ntnu.fp.model.room.Room;

public class PopupRooms {
	
private Room room;
	
	public PopupRooms(Client client, int size){
		
		List<Room> rooms = client.getRooms();
		Room[] roomsArray = new Room[rooms.size()];
		for (int i=0; i<rooms.size(); i++){
			roomsArray[i]=rooms.get(i);
		}
		
		room = (Room)JOptionPane.showInputDialog(null, "Select Room", "Rooms", JOptionPane.QUESTION_MESSAGE,null, roomsArray, roomsArray[0]);
	}
	public Room getRoom(){
		return room;
	}
	
}
