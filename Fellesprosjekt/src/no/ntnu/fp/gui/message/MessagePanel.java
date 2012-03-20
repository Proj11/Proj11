package no.ntnu.fp.gui.message;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.swing.*;
import javax.swing.table.TableModel;

import no.ntnu.fp.gui.employee.EmployeeList;
import no.ntnu.fp.model.appointment.Appointment;
import no.ntnu.fp.model.employee.Employee;
import no.ntnu.fp.model.message.Message;
import no.ntnu.fp.model.message.MessageTableModel;

public class MessagePanel extends JPanel {
	
	private JScrollPane scrollPane;
	private JTable elements;
	private MessageTableModel tableModel;
	private JButton gotoButton;
	private JPanel tablePanel;
	
	public MessagePanel() {
		super();
		//making tabemodel for the table
		tableModel=new MessageTableModel();
		Appointment a=new Appointment(new Employee("Torb"));
		a.setSubject("Testing");
		tableModel.add(new Message(a));
		Appointment b=new Appointment(new Employee("Mats"));
		b.setSubject("Testing");
		tableModel.add(new Message(b));
		
		//making table to put in scrollpane
		elements = new JTable(tableModel);
		elements.getTableHeader().setReorderingAllowed(false);
		elements.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		//adding scrollpane
		scrollPane = new JScrollPane(elements);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setPreferredSize(new Dimension(280, 550));
		add(scrollPane);
		
		//adding gotoButton
		gotoButton = new JButton("Go to");
		add(gotoButton);
		
		
		
	}
}
