package no.ntnu.fp.gui.appointment;

import no.ntnu.fp.gui.time.TimeSpinner;
import no.ntnu.fp.model.appointment.Appointment;
import no.ntnu.fp.model.employee.ParticipantListModel;
import no.ntnu.fp.model.time.Time;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

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
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.toedter.calendar.JDateChooser;

public class AppointmentPanel extends JPanel implements ActionListener, KeyListener, PropertyChangeListener, ChangeListener {
	
	private Appointment model;
	private JDateChooser dateChooser;
	private JSpinner startTime, endTime;
	private JList participantList;
	private JButton addParticipant, removeParticipant;
	private JTextField subject;
	private JTextArea description;
	private JCheckBox autoReserve;
	private JComboBox rooms;
	private JTextField place;
	private JButton deleteButton;
	private JButton saveButton;
	private JButton clearButton;
	
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
		participantList=new ParticipantList();
		JScrollPane employeeScrollPane=new JScrollPane();
		employeeScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		employeeScrollPane.add(participantList);
		employeeScrollPane.setPreferredSize(ParticipantList.defaultSize);
		employeeScrollPane.setMaximumSize(ParticipantList.defaultSize);
		employeeScrollPane.setMinimumSize(ParticipantList.defaultSize);
		panel.add(employeeScrollPane, c);
		JPanel buttonPanel=new JPanel();
		Dimension d = new Dimension(140, 25);
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
		addParticipant=new JButton("Add Participant");
		addParticipant.setPreferredSize(d);
		addParticipant.setMaximumSize(d);
		addParticipant.setMinimumSize(d);
		buttonPanel.add(addParticipant);
		removeParticipant=new JButton("Remove Participant");
		removeParticipant.setPreferredSize(d);
		removeParticipant.setMaximumSize(d);
		removeParticipant.setMinimumSize(d);
		buttonPanel.add(removeParticipant);
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
		c.insets=new Insets(2, 2, 2, 2);
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
		d = new Dimension(90, 24);
		panel.setLayout(new GridBagLayout());
		deleteButton = new JButton("Delete");
		deleteButton.setPreferredSize(d);
		saveButton = new JButton("Save");
		saveButton.setPreferredSize(d);
		clearButton = new JButton("New");
		clearButton.setPreferredSize(d);
		c.insets=new Insets(2, 2, 2, 2);
		c.gridx=0;
		panel.add(deleteButton,c);
		c.gridx=1;
		panel.add(saveButton,c);
		c.gridx=2;
		panel.add(clearButton,c);
		add(panel);
		
		setAppointmentModel(new Appointment(null));
		
		dateChooser.addPropertyChangeListener(this);
		startTime.addChangeListener(this);
		endTime.addChangeListener(this);
		subject.addKeyListener(this);
		description.addKeyListener(this);
		clearButton.addActionListener(this);
	}
	
	public JButton getDeleteButton() {
		return deleteButton;
	}

	public JButton getSaveButton() {
		return saveButton;
	}
	
	public JButton getAddParticipant() {
		return addParticipant;
	}
	
	public Appointment getAppointmentModel() {
		return model;
	}

	public void setAppointmentModel(Appointment a) {
		if (a!=null) {
			model=a;
		} else {
			model=new Appointment(null); //TODO Replace null with the currently logged in user.	
		}
		dateChooser.setDate(model.getDate());
		startTime.getModel().setValue(model.getStart()); 
		endTime.getModel().setValue(model.getEnd());
		participantList.setModel(new ParticipantListModel(model.getParticipants()));
		subject.setText(model.getSubject());
		description.setText(model.getDescription());
	}


	@Override
	public void keyPressed(KeyEvent e) {}
	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getSource()==subject) {
			model.setSubject(subject.getText());
		} else if (e.getSource()==description) {
			model.setDescription(description.getText());
		}
	}
	@Override
	public void keyTyped(KeyEvent e) {}


	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==clearButton) {
			setAppointmentModel(new Appointment(null));
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent e) {
		if (e.getSource()==dateChooser) {
			model.setDate(dateChooser.getDate());
		}
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		if (e.getSource()==startTime) {
			model.setStart((Time)startTime.getValue());
		} else if (e.getSource()==endTime) {
			model.setEnd((Time)endTime.getValue());
		}
	}
}