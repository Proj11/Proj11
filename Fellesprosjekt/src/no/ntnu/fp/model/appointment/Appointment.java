package no.ntnu.fp.model.appointment;

import java.util.Date;

import no.ntnu.fp.model.employee.Employee;
import no.ntnu.fp.model.time.Time;

public class Appointment {
	private Time start;
	private int duration;
	private Date date;
	private String subject;
	private Employee leader;
	
	public Appointment(Employee createdBy) {
		this.leader=createdBy;
	}

	public Time getStart() {
		return start;
	}

	public void setStart(Time start) {
		this.start = start;
	}
	public Employee getLeader(){
		return this.leader;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date=date;
	}
	
	public String toString() {
		return subject+"\n"+leader.getName();
	}
}
