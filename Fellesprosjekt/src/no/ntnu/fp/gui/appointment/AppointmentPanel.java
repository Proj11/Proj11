package no.ntnu.fp.gui.appointment;

import no.ntnu.fp.gui.employee.EmployeeList;
import no.ntnu.fp.gui.time.TimeSpinner;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import com.toedter.calendar.JDateChooser;

public class AppointmentPanel extends JPanel{
	
	private JDateChooser dateChooser;
	private JSpinner startTime, endTime;
	private JList employeeList;
	private JButton addEmployee, removeEmployee;
	private JTextField subject;
	private JTextArea description;
	private JCheckBox autoReserve;
	private JComboBox rooms;
	private JTextField place;
	private JButton deleteButton;
	private JButton createAppointmentButton;
	private JButton saveButton;
	private JButton clearButton;
	public JDateChooser getDateChooser() {
		return dateChooser;
	}


	public JSpinner getStartTime() {
		return startTime;
	}


	public JSpinner getEndTime() {
		return endTime;
	}


	public JList getEmployeeList() {
		return employeeList;
	}


	public JButton getAddEmployee() {
		return addEmployee;
	}


	public JButton getRemoveEmployee() {
		return removeEmployee;
	}


	public JTextField getSubject() {
		return subject;
	}


	public JTextArea getDescription() {
		return description;
	}


	public JCheckBox getAutoReserve() {
		return autoReserve;
	}


	public JComboBox getRooms() {
		return rooms;
	}


	public JTextField getPlace() {
		return place;
	}


	public JButton getDeleteButton() {
		return deleteButton;
	}


	public JButton getCreateAppointmentButton() {
		return createAppointmentButton;
	}


	public JButton getSaveButton() {
		return saveButton;
	}


	public JButton getClearButton() {
		return clearButton;
	}


	
	
	public AppointmentPanel() {
		super();
		setLayout(new FlowLayout(FlowLayout.LEADING));
		//The date and time panel
		JPanel panel=new JPanel();
		panel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx=0;
		c.gridy=0;
		panel.add(new JLabel("Select date: "), c);
		c.gridx=1;
		c.gridy=0;
		panel.add(new JLabel("Start time: "), c);
		c.gridx=2;
		c.gridy=0;
		panel.add(new JLabel("End time: "), c);
		//DATE CHOOSER
		c.gridx=0;
		c.gridy=1;
		dateChooser=new JDateChooser();
		dateChooser.setPreferredSize(new Dimension(100, 24));
		dateChooser.setMaximumSize(new Dimension(100, 24));
		dateChooser.setMinimumSize(new Dimension(100, 24));
		dateChooser.setToolTipText("Date for appointment ");
		dateChooser.setDate(Calendar.getInstance().getTime());
		panel.add(dateChooser, c);
		//START TIME CHOOSER
		c.gridx=1;
		c.gridy=1;
		startTime=new TimeSpinner();
		panel.add(startTime, c);
		//END TIME CHOOSER
		c.gridx=2;
		c.gridy=1;
		endTime=new TimeSpinner();
		panel.add(endTime, c);
		add(panel);
		
		//The subject panel
		panel=new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(new JLabel("Subject:"));
		subject=new JTextField();
		subject.setColumns(24);
		panel.add(subject);
		add(panel);
		
		//The employee panel
		panel=new JPanel();
		panel.setLayout(new GridBagLayout());
		c.gridx=0;
		c.gridy=0;
		panel.add(new JLabel("Employees:"), c);
		c.gridx=0;
		c.gridy=1;
		employeeList=new EmployeeList();
		JScrollPane employeeScrollPane=new JScrollPane();
		employeeScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		employeeScrollPane.add(employeeList);
		employeeScrollPane.setPreferredSize(EmployeeList.defaultSize);
		employeeScrollPane.setMaximumSize(EmployeeList.defaultSize);
		employeeScrollPane.setMinimumSize(EmployeeList.defaultSize);
		panel.add(employeeScrollPane, c);
		JPanel buttonPanel=new JPanel();
		Dimension d = new Dimension(140, 25);
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
		addEmployee=new JButton("Add Employee");
		addEmployee.setPreferredSize(d);
		addEmployee.setMaximumSize(d);
		addEmployee.setMinimumSize(d);
		buttonPanel.add(addEmployee);
		removeEmployee=new JButton("Remove Employee");
		removeEmployee.setPreferredSize(d);
		removeEmployee.setMaximumSize(d);
		removeEmployee.setMinimumSize(d);
		buttonPanel.add(removeEmployee);
		c.gridx=1;
		c.gridy=1;
		panel.add(buttonPanel, c);
		add(panel);
		
		//The description panel
		panel=new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(new JLabel("Description:"));
		description=new JTextArea();
		description.setWrapStyleWord(true);
		description.setLineWrap(true);
		JScrollPane scrollPane=new JScrollPane(description, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setPreferredSize(new Dimension(280, 200));
		panel.add(scrollPane);
		add(panel);
		
		//The manual-reservation panel
		//The auto-reservation panel
		d = new Dimension(120, 24);
		panel = new JPanel();
		autoReserve = new JCheckBox("Auto reserve?");
		panel.add(autoReserve);
		add(panel);
		panel=new JPanel();
		rooms = new JComboBox();
		rooms.setPreferredSize(d);
		place = new JTextField(10);
		place.setPreferredSize(d);
		panel.setLayout(new GridBagLayout());
		c=new GridBagConstraints();
		c.gridx=0;
		c.gridy=0;
		panel.add(new JLabel("Available rooms:"), c);
		c.gridx=1;
		c.gridy=0;
		panel.add(new JLabel("Location:"), c);
		c.gridx=0;
		c.gridy=1;
		panel.add(rooms, c);
		c.gridx=1;
		c.gridy=1;
		panel.add(place, c);
		add(panel);
		
		//The edit buttons panel
		panel = new JPanel();
		c=new GridBagConstraints();
		d = new Dimension(80, 24);
		panel.setLayout(new GridBagLayout());
		deleteButton = new JButton("Delete");
		deleteButton.setPreferredSize(d);
		createAppointmentButton = new JButton("Create");
		createAppointmentButton.setPreferredSize(d);
		saveButton = new JButton("Save");
		saveButton.setPreferredSize(d);
		clearButton = new JButton("Clear");
		clearButton.setPreferredSize(d);
		c.insets=new Insets(3, 3, 3, 3);
		c.gridx=0;
		c.gridy=0;
		panel.add(deleteButton,c);
		c.gridx = 1;
		panel.add(createAppointmentButton,c);
		c.gridx=0;
		c.gridy=1;
		panel.add(saveButton,c);
		c.gridx=1;
		panel.add(clearButton,c);
		add(panel);
		
		
		// actions
		clearButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				clearFields();
			}
		});
		
		
	}

	public void clearFields(){
		dateChooser.setDate(Calendar.getInstance().getTime());
		subject.setText("");
		description.setText("");
		employeeList.removeAll();
		place.setText("");
		autoReserve.setSelected(false);
		rooms.setSelectedItem(null);
		//needs code to include Model
	}
	
}
