package no.ntnu.fp.gui;

import no.ntnu.fp.client.Client;
import no.ntnu.fp.gui.appointment.AppointmentPanel;
import no.ntnu.fp.gui.calendar.CalendarPanel;

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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Locale;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class CalendarClient extends JFrame implements ComponentListener, ActionListener, PropertyChangeListener {
	
	public static Locale calendarLocale=Locale.ENGLISH;
	public final static Dimension size=Toolkit.getDefaultToolkit().getScreenSize(); //Get the information required to set the frame to full screen
	public ApplicationToolbar toolbar; //The applications tool bar
	public CalendarPanel calendarPanel; // This panel is used to display the calendar
	public ApplicationSidePanel toolPanel; // The tool panel used to create new appointments, etc.
	
	public CalendarClient() {
		super();
		
		//Set values
		setTitle("Calendar Client");
		setIconImage(new ImageIcon("res/Calendar.png").getImage());
		setLayout(new BorderLayout());
		setFocusable(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setPreferredSize(size);
		setExtendedState(MAXIMIZED_BOTH);
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
		
		toolbar.month.setMonth(calendarPanel.getDisplayedMonth());
		toolbar.year.setYear(calendarPanel.getDisplayedYear());
		toolbar.week.setWeek(calendarPanel.getDisplayedWeek());
		
		//Add components
		add(toolbar, BorderLayout.NORTH);
		add(toolPanel, BorderLayout.WEST);
		add(calendarPanel, BorderLayout.CENTER);
		
		//Show the frame
		pack();
		setVisible(true);
		
		//add component listener, this should be the last thing you do because we don't want to call events for no reason
		addComponentListener(this);
		toolbar.nextWeek.addActionListener(this);
		toolbar.previousWeek.addActionListener(this);
		toolbar.week.addActionListener(this);
		toolbar.month.addPropertyChangeListener(this);
		toolbar.year.addPropertyChangeListener(this);
		toolPanel.getAppPanel().getSaveButton().addActionListener(this);
		toolPanel.getAppPanel().getDeleteButton().addActionListener(this);
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
		toolbar.week.setWeek(calendarPanel.getDisplayedWeek());
		toolbar.month.setMonth(calendarPanel.getDisplayedMonth());
		toolbar.year.setYear(calendarPanel.getDisplayedYear());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==toolbar.nextWeek) {
			calendarPanel.setDisplayedWeek(calendarPanel.getDisplayedWeek()+1);
			updateCalendarPanel();
		} else if (e.getSource()==toolbar.previousWeek) {
			calendarPanel.setDisplayedWeek(calendarPanel.getDisplayedWeek()-1);
			updateCalendarPanel();
		} else if (e.getSource()==toolPanel.getAppPanel().getSaveButton()) {
			toolPanel.getAppPanel().getModel(); //TODO Code to add an appointment into the database
		}  else if (e.getSource()==toolPanel.getAppPanel().getSaveButton()) {
			toolPanel.getAppPanel().getModel(); //TODO Code to remove an appointment from the database
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent e) {
		if (e.getSource()==toolbar.year && e.getPropertyName()=="year" && toolbar.year.getYear()!=calendarPanel.getDisplayedYear()) {
			calendarPanel.setDisplayedYear(toolbar.year.getYear());
			updateCalendarPanel();
		} else if (e.getSource()==toolbar.month && e.getPropertyName()=="month" && toolbar.month.getMonth()!=calendarPanel.getDisplayedMonth()) {
			calendarPanel.setDisplayedMonth(toolbar.month.getMonth());
			updateCalendarPanel();
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
		usernameText.setText("brukernavn");
		panel.add(usernameText, c);
		c.gridx=0;
		c.gridy=1;
		passwordLabel=new JLabel("Password: ");
		panel.add(passwordLabel, c);
		c.gridx=1;
		c.gridy=1;
		passwordText=new JPasswordField(10);
		passwordText.setText("passord");
		panel.add(passwordText, c);
		
		add(panel, BorderLayout.CENTER);
		login=new JButton("Login");
		login.addActionListener(this);
		add(login, BorderLayout.SOUTH);
		
		pack();
		setVisible(true);
	}
	
	@SuppressWarnings("deprecation")
	public void login() {
		//TODO A connection should be established here
		//TODO The connection should then be passed as an argument to the CalendarClient class
		try { 
			if (usernameText.getText() == "" || passwordText.getText() == ""){
				Client client = new Client();
				boolean logon = client.logOn("brukernavn", "passord");
				setVisible(false);
				new CalendarClient();
			}
			else {
				Client client = new Client();
				boolean logon = client.logOn(usernameText.getText(), passwordText.getText());
				
				if (logon){
					setVisible(false);
					new CalendarClient();
				}
				else System.out.println("Incorrect username / password.");
			}
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