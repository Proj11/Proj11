package no.ntnu.fp.gui.message;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.swing.*;
import javax.swing.table.TableModel;

import no.ntnu.fp.gui.employee.EmployeeList;
import no.ntnu.fp.model.employee.Employee;
import no.ntnu.fp.model.message.Message;
import no.ntnu.fp.model.message.MessageTableModel;

public class MessagePanel extends JPanel {
	
	private JScrollPane scrollPane;
	private JTable elements;
	private MessageTableModel tableModel;
	
	public MessagePanel() {
		super();
		
		tableModel=new MessageTableModel();
		tableModel.add(new Message(null));
		tableModel.add(new Message(null));
		elements = new JTable(tableModel);
		elements.getTableHeader().setReorderingAllowed(false);
		scrollPane = new JScrollPane(elements);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setPreferredSize(new Dimension(280, 450));
		add(scrollPane);	
	}
}
