package no.ntnu.fp.gui;

import no.ntnu.fp.gui.appointment.AppointmentPanel;
import no.ntnu.fp.gui.employee.EmployeePanel;
import no.ntnu.fp.gui.message.MessagePanel;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JTabbedPane;

public class ApplicationSidePanel extends JTabbedPane {

	private final Dimension size = new Dimension(300, 0); // width, height of
															// the component.
															// The height value
															// is not important,
															// thus it is 0
	private AppointmentPanel appPanel; // Appointment Panel
	private MessagePanel msgPanel; // Notification Panel
	private EmployeePanel empPanel; // Employee Panel, used to decide which
									// employees calendar you want to see in
									// your calendar

	public ApplicationSidePanel() {
		super();
		setPreferredSize(size); // Set the size
		setBorder(BorderFactory.createEtchedBorder()); // Create the border
		addTab("Notifications", msgPanel = new MessagePanel()); // Add the
																// notifications
																// tab
		addTab("Employees", empPanel = new EmployeePanel()); // Add the
																// employees tab
		addTab("Appointment", appPanel = new AppointmentPanel()); // Add the
																	// appointment
																	// tab
	}

	public Dimension getSize() {
		return size;
	}

	public AppointmentPanel getAppPanel() {
		return appPanel;
	}

	public MessagePanel getMsgPanel() {
		return msgPanel;
	}

	public EmployeePanel getEmpPanel() {
		return empPanel;
	}
}