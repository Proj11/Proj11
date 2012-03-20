package no.ntnu.fp.gui.appointment;

import no.ntnu.fp.gui.employee.EmployeeList;
import no.ntnu.fp.gui.time.TimeSpinner;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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

public class AppointmentPanel extends JPanel {
	
	public JDateChooser dateChooser;
	public JSpinner startTime, endTime;
	public JList employeeList;
	public JButton addEmployee, removeEmployee;
	public JTextField subject;
	public JTextArea description;
	private JCheckBox autoReserve;
	private JComboBox rooms;
	private JTextField place;
	private JButton deleteButton;
	private JButton createAppointmentButton;
	private JButton saveButton;
	private JButton clearButton;
	
	
	public AppointmentPanel() {
		super();
		
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
		
		//The auto-reservation panel
		panel=new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		Box box = Box.createHorizontalBox();
		panel.add(new JLabel("Auto reservation?"));
		autoReserve =new JCheckBox();
		box.add(autoReserve);
		box.add(Box.createHorizontalStrut(160));
		panel.add(box);
		add(panel);
		
		//The manual-reservation panel
		panel = new JPanel(); 
		rooms = new JComboBox();
		place = new JTextField(10);
		JPanel roomReservPanel = new JPanel();
		roomReservPanel.setLayout(new BoxLayout(roomReservPanel, BoxLayout.X_AXIS));
		Box verticalBox = Box.createVerticalBox();
		Box roomLabelBox = Box.createHorizontalBox();
//		roomLabelBox.add(new JLabel("Available rooms"));
		roomLabelBox.add(Box.createHorizontalStrut(80));
//		Box roomBox = Box.createHorizontalBox();
//		roomBox.add(rooms);
//		roomBox.add(Box.createHorizontalStrut(70));
		verticalBox.add(new JLabel("Available rooms"));
		verticalBox.add(rooms);
		roomReservPanel.add(verticalBox);
//		placeRoomLabelBox.add(Box.createHorizontalStrut(60));
//		placeRoomLabelBox.add(new JLabel("Place"));
//		verticalBox.add(placeRoomLabelBox);
//		Box placeRoomBox = Box.createHorizontalBox();
//		placeRoomBox.add(rooms);
//		placeRoomBox.add(Box.createHorizontalStrut(90));
//		placeRoomBox.add(place);
//		verticalBox.add(placeRoomBox);
//		roomReserv.add(new JLabel("Avaliable rooms:"));
//		roomReserv.add(rooms);
		JPanel placeReservePanel = new JPanel();
		placeReservePanel.setLayout(new BoxLayout(placeReservePanel, BoxLayout.X_AXIS));
		Box placeVerticalBox = Box.createVerticalBox();
		placeVerticalBox.add(new JLabel("Place:"));
		placeVerticalBox.add(place);
		placeReservePanel.add(placeVerticalBox);
//		Box placeBox = Box.createHorizontalBox();
//		placeBox.add(place);
//		placeBox.add(Box.createHorizontalStrut(70));
//		placeVerticalBox.add(placeBox);
//		placeReservePanel.add(placeVerticalBox);
//		placeReserve.add(new JLabel("Place:"));
//		placeReserve.add(place);
		panel.add(roomReservPanel);
		panel.add(placeReservePanel);
//		panel.add(verticalBox);		
		add(panel);
		
		//The edit buttons panel
		panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		deleteButton = new JButton("Delete");
		createAppointmentButton = new JButton("Create");
		saveButton = new JButton("Save");
		clearButton = new JButton("Clear");
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
		
		
	}
}