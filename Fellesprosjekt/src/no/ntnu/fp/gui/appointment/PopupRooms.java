package no.ntnu.fp.gui.appointment;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListModel;

import no.ntnu.fp.client.Client;
import no.ntnu.fp.model.employee.Employee;
import no.ntnu.fp.model.room.Room;

public class PopupRooms {
	
private Room room;
private int choice;
	
	public PopupRooms(Client client, int size){
		
		List<Room> rooms = client.getRooms();
		Room[] roomsArray = new Room[rooms.size()];
		JList<Room> roomList = new JList<Room>();
		for (int i=0; i<rooms.size(); i++){
			roomsArray[i]=rooms.get(i);
		}
		roomList.setListData(roomsArray);
		
		Object[] options = new Object[]{"Book selected", "Auto-book", "Cancel"};
		choice = JOptionPane.showInternalOptionDialog(null, roomList, "Rooms", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, null);
		
		switch(choice){
		case 0:
			room = roomList.getSelectedValue();
			break;
		case 1:
			room=autoReserve(rooms, size);
			break;
		default:
			break;
		}
	}
	public Room getRoom(){
		return room;
	}
	
	private Room autoReserve(List<Room> rooms, int size){
		for(int i=0; i<rooms.size(); i++){
			if(size <= rooms.get(i).getSize()){
				return rooms.get(i);
			}
		}
		return null;
	}

}
