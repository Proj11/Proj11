package no.ntnu.fp.gui.appointment;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JApplet;
import javax.swing.JOptionPane;

import no.ntnu.fp.client.Client;
import no.ntnu.fp.model.employee.Employee;

public class PopupEmployees{
	private Employee participant;
	
	public PopupEmployees(Client client){
	Employee[] choices = null;
	List<Employee> employees = client.getEmployees();
	choices = new Employee[employees.size()];
	for(int i=0; i < choices.length; i++){
		choices[i] = employees.get(i);
	}
	participant = (Employee) JOptionPane.showInputDialog(null, "Select Employee:", "Employees",JOptionPane.QUESTION_MESSAGE,null,choices, choices[0]);
	}
	public Employee getParticipant(){
		return participant;
	}
}
