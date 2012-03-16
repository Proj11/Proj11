package no.ntnu.fp.model.message;

import no.ntnu.fp.model.appointment.Appointment;

public class Message {
	
	public Appointment appointment;
	
	public Message(Appointment appointment) {
		this.appointment=appointment;
	}
}
