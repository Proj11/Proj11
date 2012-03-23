package no.ntnu.fp.model.room;

public class Room {
	
	private int roomnr;
	private int size;
	
	public Room(int roomnr, int size){
		this.roomnr = roomnr;
		this.size = size;
	}

	public int getRoomnr() {
		return roomnr;
	}

	public void setRoomnr(int roomnr) {
		this.roomnr = roomnr;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
	
	public String toString() {
		return "Room: "+roomnr+" - Size: "+size;
	}
}
