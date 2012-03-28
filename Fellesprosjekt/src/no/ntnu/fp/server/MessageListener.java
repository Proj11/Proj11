package no.ntnu.fp.server;

import no.ntnu.fp.model.appointment.Appointment;

public interface MessageListener {
	public void messageReceived(String message);
	public void appointmentReceived(Appointment a);
	public void connectionClosed(HandleAClient clientHandler);
}
