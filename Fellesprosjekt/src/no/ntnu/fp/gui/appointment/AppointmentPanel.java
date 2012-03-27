package no.ntnu.fp.gui.appointment;

import no.ntnu.fp.gui.CalendarClient;
import no.ntnu.fp.gui.time.TimeSpinner;
import no.ntnu.fp.model.appointment.Appointment;
import no.ntnu.fp.model.appointment.Participant;
import no.ntnu.fp.model.appointment.ParticipantListModel;
import no.ntnu.fp.model.appointment.Participant.State;
import no.ntnu.fp.model.employee.Employee;
import no.ntnu.fp.model.room.Room;
import no.ntnu.fp.model.time.Time;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
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

public class AppointmentPanel extends JPanel implements ActionListener, KeyListener, PropertyChangeListener, ChangeListener, FocusListener {
	
	private Appointment model;
	private JDateChooser dateChooser;
	private JSpinner startTime, endTime;
	private JList participantList;
	private JButton addParticipant, removeParticipant;
	private JTextField subject;
	private JTextArea description;
	private JButton roomsButton;
	private JTextField location;
	private JButton deleteButton;
	private JButton saveButton;
	private JButton clearButton;
	private JButton autoReserveButton,editButton;
	private JButton acceptButton, denyButton;
	private boolean isLeader;
	
	public AppointmentPanel() {
		super();
		isLeader = true;
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
		panel.add(new JLabel("Participants:"), c);
		c.gridx=0;
		c.gridy=1;
		participantList=new ParticipantList();
		participantList.setCellRenderer(new ParticipantListRenderer());
		JScrollPane employeeScrollPane=new JScrollPane(participantList);
		employeeScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		employeeScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		employeeScrollPane.setPreferredSize(ParticipantList.defaultSize);
		employeeScrollPane.setMaximumSize(ParticipantList.defaultSize);
		employeeScrollPane.setMinimumSize(ParticipantList.defaultSize);
		panel.add(employeeScrollPane, c);
		JPanel buttonPanel=new JPanel();
		Dimension d = new Dimension(140, 25);
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
		addParticipant=new JButton("Add");
		addParticipant.setPreferredSize(d);
		addParticipant.setMaximumSize(d);
		addParticipant.setMinimumSize(d);
		buttonPanel.add(addParticipant);
		removeParticipant=new JButton("Remove");
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
		
		//The auto-reservation panel
		d = new Dimension(120, 24);
		panel=new JPanel();
		panel.setLayout(new GridBagLayout());
		autoReserveButton=new JButton("Autoreserve");
		c=new GridBagConstraints();
		c.insets=new Insets(2, 2, 2, 2);
		c.gridx=0;
		c.gridy=0;
		panel.add(autoReserveButton, c);
		add(panel);
		//Location panel
		d = new Dimension(120, 24);
		panel=new JPanel();
		panel.setLayout(new GridBagLayout());
		location = new JTextField(10);
		location.setPreferredSize(d);
		roomsButton=new JButton("Get Rooms");
		c=new GridBagConstraints();
		c.insets=new Insets(2, 2, 2, 2);
		c.gridx=0;
		c.gridy=0;
		panel.add(new JLabel("Location:"), c);
		c.gridx=0;
		c.gridy=1;
		panel.add(location, c);
		c.gridx=1;
		c.gridy=1;
		panel.add(roomsButton, c);
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
		denyButton = new JButton("Deny");
		denyButton.setPreferredSize(d);
		denyButton.setVisible(false);
		acceptButton = new JButton("Accept");
		acceptButton.setPreferredSize(d);
		acceptButton.setVisible(false);
		editButton = new JButton("Edit");
		c.insets=new Insets(2, 2, 2, 2);
		c.gridx=0;
		panel.add(deleteButton,c);
		panel.add(acceptButton,c);
		c.gridx=1;
		panel.add(saveButton,c);
		panel.add(denyButton,c);
		c.gridx=2;
		panel.add(clearButton,c);
		add(panel);
		
		setAppointmentModel(null);
		if(!isLeader){
			changePanel();
		}
		
		dateChooser.addPropertyChangeListener(this);
		startTime.addChangeListener(this);
		endTime.addChangeListener(this);
		subject.addKeyListener(this);
		description.addKeyListener(this);
		location.addKeyListener(this);
		location.addFocusListener(this);
		clearButton.addActionListener(this);
	}
	
	public JButton getDeleteButton() {
		return deleteButton;
	}

	public JButton getSaveButton() {
		return saveButton;
	}
	
	public JButton getAddParticipantButton() {
		return addParticipant;
	}
	
	public JButton getRemoveParticipantButton() {
		return removeParticipant;
	}
	
	public JButton getRoomsButton() {
		return roomsButton;
	}
	
	public void setRoom(Room r) {
		this.setRoom(r.getRoomnr());
	}
	public JButton getAutoReserveButton(){
		return autoReserveButton;
	}
	
	public void setRoom(int r) {
		model.setRoomNumber(r);
		model.setLocation("");
		location.setText("Room: "+r);
	}
	
	public void addParticipant(Employee e) {
		boolean alreadyParticipating=false;
		for (int i = 0; i < model.getParticipants().size(); i++) {
			if (model.getParticipants().get(i).getEmployee().getUsername().equals(e.getUsername())) {
				alreadyParticipating=true;
				break;
			}
		}
		if (!alreadyParticipating) {
			((ParticipantListModel)participantList.getModel()).add(new Participant(e, State.PENDING));
		}
	}
	
	public void removeSelectedParticipant() {
		((ParticipantListModel)participantList.getModel()).remove((Participant)participantList.getSelectedValue());
	}
	
	public Appointment getAppointmentModel() {
		return model;
	}

	public void setAppointmentModel(Appointment a) {
		if (a!=null) {
			model=a;
		} else {
			model=new Appointment(CalendarClient.USER);	
		}
		dateChooser.setDate(model.getDate());
		startTime.getModel().setValue(model.getStart());
		endTime.getModel().setValue(model.getEnd());
		participantList.setModel(new ParticipantListModel(model.getParticipants()));
		subject.setText(model.getSubject());
		description.setText(model.getDescription());
		if (model.getRoomNumber()!=0) {
			location.setText("Room: "+model.getRoomNumber());
		} else {
			location.setText(model.getLocation());
		}
		
	}
	public void changePanel(){
		dateChooser.setEnabled(false);
		startTime.setEnabled(false);
		endTime.setEnabled(false);
		subject.setEnabled(false);
		addParticipant.setEnabled(false);
		removeParticipant.setEnabled(false);
		description.setEnabled(false);
		autoReserveButton.setEnabled(false);
		location.setEnabled(false);
		getRoomsButton().setEnabled(false);
		deleteButton.setVisible(false);
		saveButton.setVisible(false);
		editButton.setVisible(false);
		denyButton.setVisible(true);
		acceptButton.setVisible(true);
	}


	public JButton getEditButton() {
		return editButton;
	}

	public void setEditButton(JButton editButton) {
		this.editButton = editButton;
	}

	public JButton getAcceptButton() {
		return acceptButton;
	}

	public void setAcceptButton(JButton acceptButton) {
		this.acceptButton = acceptButton;
	}

	public JButton getDenyButton() {
		return denyButton;
	}

	public void setDenyButton(JButton denyButton) {
		this.denyButton = denyButton;
	}

	@Override
	public void keyPressed(KeyEvent e) {}
	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getSource()==subject) {
			model.setSubject(subject.getText());
		} else if (e.getSource()==description) {
			model.setDescription(description.getText());
		} else if (e.getSource()==location) {
			model.setLocation(location.getText());
		}
	}
	@Override
	public void keyTyped(KeyEvent e) {}


	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==clearButton) {
			setAppointmentModel(null);
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

	@Override
	public void focusGained(FocusEvent e) {
		if (e.getSource()==location && model.getRoomNumber()!=0) {
			location.setText("");
		}
	}

	@Override
	public void focusLost(FocusEvent e) {
		if (e.getSource()==location && !location.getText().equals("") && model.getRoomNumber()!=0) {
			model.setRoomNumber(0);
		} else if (e.getSource()==location && location.getText().equals("") && model.getRoomNumber()!=0) {
			location.setText("Room: "+model.getRoomNumber());
			model.setLocation("");
		}
	}
}