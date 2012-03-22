package no.ntnu.fp.model.appointment;

import no.ntnu.fp.model.employee.Employee;

public class Participant {

	private Employee employee;
	private State state;
	private int appointmentID;

	public Participant(Employee e, State s, int appID) {
		employee = e;
		state = s;
		appointmentID = appID;
	}

	public State getState() {
		return state;
	}

	public int getAppointmentID() {
		return appointmentID;
	}
	
	public void setAppointmentID(int appointmentID) {
		this.appointmentID = appointmentID;
	}
	
	public void setState(State state) {
		this.state = state;
	}

	public Employee getEmployee() {
		return employee;
	}

	public enum State {
		PENDING, ACCEPTED, DENIED;

	};
}
