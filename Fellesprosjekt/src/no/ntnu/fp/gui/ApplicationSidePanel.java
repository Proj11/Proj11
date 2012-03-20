package no.ntnu.fp.gui;

import no.ntnu.fp.gui.appointment.AppointmentPanel;
import no.ntnu.fp.gui.employee.EmployeePanel;
import no.ntnu.fp.gui.message.MessagePanel;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JTabbedPane;

public class ApplicationSidePanel extends JTabbedPane {
	
	public final Dimension size=new Dimension(300, 0); // width, height of the component. The height value is not important, thus it is 0
	public AppointmentPanel appPanel; //Appointment Panel
	public MessagePanel msgPanel; //Notification Panel
	public EmployeePanel empPanel; //Employee Panel, used to decide which employees calendar you want to see in your calendar
	
	public ApplicationSidePanel() {
		super();
		setPreferredSize(size); //Set the size
		setBorder(BorderFactory.createEtchedBorder()); //Create the border
		addTab("Notifications", msgPanel=new MessagePanel()); //Add the notifications tab
		addTab("Employees", empPanel=new EmployeePanel()); //Add the employees tab
		addTab("Appointment", appPanel=new AppointmentPanel()); //Add the appointment tab
	}
	
	
	
	
}