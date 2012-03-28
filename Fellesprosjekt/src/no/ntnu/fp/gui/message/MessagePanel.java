package no.ntnu.fp.gui.message;

import java.awt.*;
import java.util.Collection;

import javax.swing.*;

import no.ntnu.fp.gui.ApplicationToolbar;
import no.ntnu.fp.gui.CalendarClient;
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
		
		//making table to put in JScrollPane
		elements = new JTable(tableModel);
		elements.getTableHeader().setReorderingAllowed(false);
		elements.setColumnSelectionAllowed(false);
		elements.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		gotoButton = new JButton("Go to");
		
		//adding JScrollPane
		JScrollPane scrollPane = new JScrollPane(elements);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setMinimumSize(new Dimension(280, 200));
		int h=CalendarClient.size.height-(150+ApplicationToolbar.size.height+gotoButton.getHeight());
		scrollPane.setPreferredSize(new Dimension(280, h));
		add(scrollPane);
		
		//adding gotoButton
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
		if (elements.getSelectedRow()!=-1) {
			return tableModel.getMessage(elements.getSelectedRow());
		}
		return null;
	}
	
	public JButton getGoToButton() {
		return gotoButton;
	}
}
