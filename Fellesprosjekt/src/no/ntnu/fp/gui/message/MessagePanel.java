package no.ntnu.fp.gui.message;

import java.awt.*;
import java.util.Collection;

import javax.swing.*;

import no.ntnu.fp.model.appointment.Appointment;
import no.ntnu.fp.model.employee.Employee;
import no.ntnu.fp.model.message.Message;
import no.ntnu.fp.model.message.MessageTableModel;

public class MessagePanel extends JPanel {
	
	private JTable elements;
	private JButton gotoButton;
	private MessageTableModel tableModel;
	
	public MessagePanel() {
		super();
		//making TableModel for the table
		tableModel=new MessageTableModel();
		
		//TODO REMOVE DUMMY DATA
		Appointment a=new Appointment(new Employee("Torb"));
		a.setSubject("ROFLOLZOMFGBBQ");
		tableModel.add(new Message(a));
		Appointment b=new Appointment(new Employee("Mats"));
		b.setSubject("This is a subject txt");
		tableModel.add(new Message(b));
		
		//making table to put in scrollpane
		elements = new JTable(tableModel);
		elements.getTableHeader().setReorderingAllowed(false);
		elements.setColumnSelectionAllowed(false);
		elements.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		//adding scrollpane
		JScrollPane scrollPane = new JScrollPane(elements);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setPreferredSize(new Dimension(280, 550));
		add(scrollPane);
		
		//adding gotoButton
		gotoButton = new JButton("Go to");
		add(gotoButton);
	}
	
	public void removeMessage(Message message) {
		tableModel.remove(message);
	}
	
	public void addMessage(Message message) {
		tableModel.add(message);
	}
	
	public void addAllMessages(Collection<Message> messages) {
		tableModel.addAllMessages(messages);
	}
	
	public void removeAllMessages(Collection<Message> messages) {
		tableModel.removeAllMessages(messages);
	}
	
	public Message getSelectedMessage() {
		return tableModel.getMessage(elements.getSelectedRow());
	}
	
	public JButton getGoToButton() {
		return gotoButton;
	}
}
