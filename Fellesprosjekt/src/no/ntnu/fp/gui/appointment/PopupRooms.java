package no.ntnu.fp.gui.appointment;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import no.ntnu.fp.client.Client;
import no.ntnu.fp.model.employee.Employee;
import no.ntnu.fp.model.room.Room;

public class PopupRooms {
	
private Room room;
	
	public PopupRooms(Client client){
		
		ArrayList<Room> rooms = client.getRooms();
		Room[] choices = null;
		for (int i =0; i < rooms.size();i++){
			choices[i] = rooms.get(i);
		}

			room =  (Room)JOptionPane.showInputDialog(null, "Select room", "Rooms",JOptionPane.QUESTION_MESSAGE,
					null,choices, choices[0]);
	}
	public Employee getRoom(){
		return room;
	}

}
