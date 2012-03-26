package no.ntnu.fp.model.appointment;

import no.ntnu.fp.model.employee.Employee;

public class Participant {

	private Employee employee;
	private State state;

	public Participant(Employee e, State s) {
		employee = e;
		state = s;
	}

	public State getState() {
		return state;
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
