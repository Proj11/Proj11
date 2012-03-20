package no.ntnu.fp.gui.employee;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;


public class EmployeePanel extends JPanel {
	
	private JTextField searchField;
	private JButton searchButton;
	private JList searchResults;
	private JButton addButton;
	private JList chosenEmployees;
	private JButton removeButton;
	private JPanel searchPanel;
	private JLabel searchLabel;
	
	public EmployeePanel() {
		setLayout(new GridBagLayout());
		searchPanel = new JPanel();
		searchField = new JTextField(15);
		searchLabel = new JLabel("Search employee:");
		searchButton = new JButton("Search");
		searchButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				//TODO: Add method to make a search query				
			}
		});
		GridBagConstraints c = new GridBagConstraints();
		searchPanel.setLayout(new GridBagLayout());
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		c.weighty = 1;
		c.ipady = 1;
		c.anchor = GridBagConstraints.LINE_START;
		searchPanel.add(searchLabel, c);
		c.gridy = 1;
		searchPanel.add(searchField, c);
		c.gridx = 1;
		searchPanel.add(searchButton, c);
		c.gridx = 0;
		c.gridy = 0;
		this.add(searchPanel, c);
		
		searchResults = new JList();
		JScrollPane employeeScrollPane=new JScrollPane();
		employeeScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		employeeScrollPane.add(searchResults);
		employeeScrollPane.setPreferredSize(new Dimension(280, 400));
		employeeScrollPane.setMaximumSize(new Dimension(280, 200));
		employeeScrollPane.setMinimumSize(new Dimension(280, 200));
		c.gridx = 0;
		c.gridy = 2;
		add(employeeScrollPane, c);		
		
		addButton = new JButton("Add to calendar");
		addButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO: Add selected employee from list to the calendar
				
			}
		});
		c.gridy = 3;
		add(addButton, c);
		
		chosenEmployees = new JList();
		JScrollPane chosenScrollPane=new JScrollPane();
		chosenScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		chosenScrollPane.add(searchResults);
		chosenScrollPane.setPreferredSize(new Dimension(280, 200));
		chosenScrollPane.setMaximumSize(new Dimension(280, 200));
		chosenScrollPane.setMinimumSize(new Dimension(280, 200));
		c.gridy = 4;
		add(chosenScrollPane, c);	
		removeButton = new JButton("Remove from calendar");
		removeButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO: Remove selected employees calendar from the calendar.
				
			}
		});
		c.gridy = 5;
		add(removeButton, c);
		
	}
	

}
