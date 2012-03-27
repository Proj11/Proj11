package no.ntnu.fp.gui;

import no.ntnu.fp.client.Client;
import no.ntnu.fp.gui.appointment.AppointmentPanel;
import no.ntnu.fp.gui.appointment.PopupConfirmation;
import no.ntnu.fp.gui.appointment.PopupEmployees;
import no.ntnu.fp.gui.appointment.PopupRooms;
import no.ntnu.fp.gui.calendar.CalendarPanel;
import no.ntnu.fp.model.appointment.Appointment;
import no.ntnu.fp.model.employee.Employee;
import no.ntnu.fp.model.room.Room;
import no.ntnu.fp.timeexception.TimeException;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

public class CalendarClient extends JFrame implements ComponentListener, ActionListener, PropertyChangeListener, MouseListener, WindowListener {
	
	public static Locale calendarLocale=Locale.ENGLISH;
	public final static Dimension size=Toolkit.getDefaultToolkit().getScreenSize(); //Get the information required to set the frame to full screen
	private ApplicationToolbar toolbar; //The applications tool bar
	private CalendarPanel calendarPanel; // This panel is used to display the calendar
	private ApplicationSidePanel toolPanel; // The tool panel used to create new appointments, etc.
	private Client client;
	public static Employee USER;
	
	public CalendarClient(Client c) {
		super();
		client = c;
		
		//Set values
		setTitle("Calendar Client");
		setIconImage(new ImageIcon("res/Calendar.png").getImage());
		setLayout(new BorderLayout());
		setFocusable(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setPreferredSize(size);
		setLocale(calendarLocale);		
		
		//Initialize components
		toolbar=new ApplicationToolbar();
		toolPanel=new ApplicationSidePanel();
		
		//Calculate the size of the calendar
		Dimension calendarSize=new Dimension(getPreferredSize());
		calendarSize.width-=toolPanel.getSize().width; //Subtract the size of the application side panel
		calendarSize.width-=16; // Subtract 16 because of window decorations
		calendarSize.height-=toolbar.size.height; // Subtract the size of the application tool bar
		calendarSize.height-=40; //Subtract 40 because of window decorations
		calendarPanel=new CalendarPanel(calendarSize);
		
		toolbar.month.setMonth(calendarPanel.getCalendar().getDisplayedMonth());
		toolbar.year.setYear(calendarPanel.getCalendar().getDisplayedYear());
		toolbar.week.setWeek(calendarPanel.getCalendar().getDisplayedWeek());
		
		//Add components
		add(toolbar, BorderLayout.NORTH);
		add(toolPanel, BorderLayout.WEST);
		add(calendarPanel, BorderLayout.CENTER);
		
		//Show the frame
		pack();
		setVisible(true);
		
		//add listeners, this should be the last thing you do because we don't want to call events for no reason
		addComponentListener(this);
		toolbar.nextWeek.addActionListener(this);
		toolbar.previousWeek.addActionListener(this);
		toolbar.week.addActionListener(this);
		toolbar.month.addPropertyChangeListener(this);
		toolbar.year.addPropertyChangeListener(this);
		toolPanel.getAppPanel().getSaveButton().addActionListener(this);
		toolPanel.getAppPanel().getDeleteButton().addActionListener(this);
		toolPanel.getMsgPanel().getGoToButton().addActionListener(this);
		calendarPanel.getCalendar().addMouseListener(this);
		toolPanel.getAppPanel().getAddParticipantButton().addActionListener(this);
		toolPanel.getAppPanel().getRemoveParticipantButton().addActionListener(this);
		toolPanel.getAppPanel().getRoomsButton().addActionListener(this);
		toolPanel.getAppPanel().getAutoReserveButton().addActionListener(this);
		toolPanel.getAppPanel().getAcceptButton().addActionListener(this);
		toolPanel.getAppPanel().getDenyButton().addActionListener(this);
		toolPanel.getAppPanel().getEditButton().addActionListener(this);
		
		//Get data from the server
	getAppointmentsFromServer();
	}
	
	public static void main(String[] args) {
		// Run the application
		new CalendarLogin();
	}

	@Override
	public void componentShown(ComponentEvent e) {}
	@Override
	public void componentHidden(ComponentEvent e) {}
	@Override
	public void componentMoved(ComponentEvent e) {}
	@Override
	public void componentResized(ComponentEvent e) {
		Dimension d=getSize();
		d.width-=toolPanel.getSize().width; //Subtract the size of the application side panel
		d.width-=16; //Subtract 16 because of window decorations
		d.height-=toolbar.getSize().height; // Subtract the size of the application tool bar
		d.height-=40; //Subtract 40 because of the window decorations
		calendarPanel.resizeScrollPane(d);
	}
	
	private void updateCalendarPanel() {
		toolbar.week.setWeek(calendarPanel.getCalendar().getDisplayedWeek());
		toolbar.month.setMonth(calendarPanel.getCalendar().getDisplayedMonth());
		toolbar.year.setYear(calendarPanel.getCalendar().getDisplayedYear());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==toolbar.nextWeek) {
			calendarPanel.getCalendar().setDisplayedWeek(calendarPanel.getCalendar().getDisplayedWeek()+1);
			updateCalendarPanel();
			
		} else if (e.getSource()==toolbar.previousWeek) {
			calendarPanel.getCalendar().setDisplayedWeek(calendarPanel.getCalendar().getDisplayedWeek()-1);
			updateCalendarPanel();
			
		} else if (e.getSource()==toolPanel.getAppPanel().getSaveButton()) {
			calendarPanel.getCalendar().addAppointment(toolPanel.getAppPanel().getAppointmentModel());
			client.createAppointment(toolPanel.getAppPanel().getAppointmentModel()); 
			toolPanel.getAppPanel().setAppointmentModel(null);
			
		} else if (e.getSource()==toolPanel.getAppPanel().getDeleteButton()) {
			if(new PopupConfirmation().getConfirm() == PopupConfirmation.YES){
				calendarPanel.getCalendar().removeAppointment(calendarPanel.getCalendar().getSelectedCell());		
				client.deleteAppointment(toolPanel.getAppPanel().getAppointmentModel().getId());
				toolPanel.getAppPanel().setAppointmentModel(null);
			}
		}else if(e.getSource()==toolPanel.getAppPanel().getEditButton()){
			calendarPanel.getCalendar().editAppointment(toolPanel.getAppPanel().getAppointmentModel());
			client.editAppointment(toolPanel.getAppPanel().getAppointmentModel());
			toolPanel.getAppPanel().setAppointmentModel(null);
			
		} else if (e.getSource()==toolPanel.getMsgPanel().getGoToButton()) {
			if (toolPanel.getMsgPanel().getSelectedMessage()!=null) {
				toolPanel.getAppPanel().setAppointmentModel(toolPanel.getMsgPanel().getSelectedMessage().getAppointment().getCopy());
				toolPanel.setSelectedComponent(toolPanel.getAppPanel());
			}
		} else if(e.getSource()==toolPanel.getAppPanel().getAddParticipantButton()) {
			Employee participant = new PopupEmployees(client).getParticipant();
			toolPanel.getAppPanel().addParticipant(participant);
			
		} else if (e.getSource()==toolPanel.getAppPanel().getRemoveParticipantButton()) {
			toolPanel.getAppPanel().removeSelectedParticipant();
			
		} else if (e.getSource()==toolPanel.getAppPanel().getRoomsButton()) {
			Appointment a = (toolPanel.getAppPanel().getAppointmentModel());
			PopupRooms r = new PopupRooms(client, a.getParticipants().size(), a);
			toolPanel.getAppPanel().setRoom(r.getRoom());
			
		} else if(e.getSource()==toolPanel.getAppPanel().getAutoReserveButton()){
			Appointment a = (toolPanel.getAppPanel().getAppointmentModel());
			System.out.println(a.getDate() + "\n" + a.getStart() + "\n" + a.getEnd());
			Room r = autoReserve(client, a.getParticipants().size());
			if (r == null)
				toolPanel.getAppPanel().getAppointmentModel().setLocation("Ingen tilgjenlige");
			else
				toolPanel.getAppPanel().setRoom(r);
		}else if(e.getSource()==toolPanel.getAppPanel().getAcceptButton()){
			sendState("accepted");
		}else if(e.getSource()==toolPanel.getAppPanel().getDenyButton()){
			sendState("denied");
		}
		
	}
	
	public void sendState(String state){
		client.sendState(toolPanel.getAppPanel().getAppointmentModel().getId(), state, USER.getUsername());
		toolPanel.getAppPanel().getAppointmentModel().getParticipant(USER.getUsername()).setState(Appointment.stringToState(state));
		toolPanel.getAppPanel().getParticipantList().repaint();
		//TODO: legg til kode i HandleAClient
	}

	@Override
	public void propertyChange(PropertyChangeEvent e) {
		if (e.getSource()==toolbar.year && e.getPropertyName()=="year" && toolbar.year.getYear()!=calendarPanel.getCalendar().getDisplayedYear()) {
			calendarPanel.getCalendar().setDisplayedYear(toolbar.year.getYear());
			updateCalendarPanel();
		} else if (e.getSource()==toolbar.month && e.getPropertyName()=="month" && toolbar.month.getMonth()!=calendarPanel.getCalendar().getDisplayedMonth()) {
			calendarPanel.getCalendar().setDisplayedMonth(toolbar.month.getMonth());
			updateCalendarPanel();
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (calendarPanel.getCalendar().getSelectedCell()!=null) {
			Appointment a = calendarPanel.getCalendar().getSelectedCell().getCopy();
			if(USER.getUsername().equals(a.getLeader().getUsername())){
				toolPanel.getAppPanel().setIsLeader(true);
			}else{
				toolPanel.getAppPanel().setIsLeader(false);
			}
			toolPanel.getAppPanel().setAppointmentModel(a);
			toolPanel.setSelectedComponent(toolPanel.getAppPanel());
		}
	}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	@Override
	public void mousePressed(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void windowActivated(WindowEvent e) {	
	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

	@Override
	public void windowClosing(WindowEvent e) {
		try {
			client.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowOpened(WindowEvent e) {
	}
	
	private Room autoReserve(Client client, int size){
		List<Room> rooms;
		try {
			rooms = client.getRooms(toolPanel.getAppPanel().getAppointmentModel());
			if (rooms == null)
				return null;
			for(int i=0; i<rooms.size(); i++){
				if(size <= rooms.get(i).getSize()){
					return rooms.get(i);
				}
			}
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	private void getAppointmentsFromServer(){
		try {
			List<Appointment> q =client.getAppointmentList(USER.getUsername());
			calendarPanel.getCalendar().addAllAppointments(q);
			calendarPanel.revalidate();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (TimeException e) {
			e.printStackTrace();
		}
	}
}

class CalendarLogin extends JFrame implements ActionListener, KeyListener {
	
	public final static Dimension size = new Dimension(240, 180);
	private JButton login;
	private JTextField usernameText;
	private JPasswordField passwordText;
	private JLabel usernameLabel, passwordLabel;
	
	public CalendarLogin() {
		setPreferredSize(size);
		setResizable(false);
		setTitle("Calendar Client");
		setIconImage(new ImageIcon("res/Calendar.png").getImage());
		setLayout(new BorderLayout());
		setFocusable(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocation((CalendarClient.size.width-size.width)/2, (CalendarClient.size.height-size.height)/2);
		
		JLabel titleLabel=new JLabel("Calendurrp", JLabel.CENTER);
		titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
		add(titleLabel, BorderLayout.NORTH);
		
		JPanel panel=new JPanel();
		panel.setLayout(new GridBagLayout());
		GridBagConstraints c=new GridBagConstraints();
		c.insets=new Insets(5, 0, 0, 0);
		c.gridx=0;
		c.gridy=0;
		usernameLabel=new JLabel("Username: ");
		panel.add(usernameLabel, c);
		c.gridx=1;
		c.gridy=0;
		usernameText=new JTextField(10);
		panel.add(usernameText, c);
		c.gridx=0;
		c.gridy=1;
		passwordLabel=new JLabel("Password: ");
		panel.add(passwordLabel, c);
		c.gridx=1;
		c.gridy=1;
		passwordText=new JPasswordField(10);
		panel.add(passwordText, c);
		
		add(panel, BorderLayout.CENTER);
		login=new JButton("Login");
		login.addActionListener(this);
		add(login, BorderLayout.SOUTH);
		addKeyListener(this);
		passwordText.addKeyListener(this);
		usernameText.addKeyListener(this);
		
		pack();
		setVisible(true);
	}
	
	@SuppressWarnings("deprecation")
	public void login() {
		try { 
			Client client = new Client();
			CalendarClient.USER = client.logOn(usernameText.getText(), passwordText.getText());
			if (CalendarClient.USER != null){
				setVisible(false);
				new CalendarClient(client);
			}
			else System.out.println("Incorrect username / password.");
		}
		catch (Exception eX){
			eX.printStackTrace();
			System.out.println("Could not connect to server.");
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==login) {
			login();
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {}
	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode()==KeyEvent.VK_ENTER) {
			login();
		}
	}
	@Override
	public void keyTyped(KeyEvent e) {}
}