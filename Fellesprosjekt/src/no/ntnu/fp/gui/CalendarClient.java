package no.ntnu.fp.gui;

import no.ntnu.fp.gui.calendar.CalendarPanel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class CalendarClient extends JFrame implements ComponentListener, ActionListener, PropertyChangeListener {
	
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
		
		GraphicsDevice device=GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		if (device.isFullScreenSupported()) {
			device.setFullScreenWindow(this);
		} else {
			System.err.println("Full screen not supported");
		}
		
		//Add components
		add(toolbar=new ApplicationToolbar(), BorderLayout.NORTH);
		add(toolPanel=new ApplicationSidePanel(), BorderLayout.WEST);
		Dimension calendarSize=new Dimension(getPreferredSize()); //Calculate the size of the calendar
		calendarSize.width-=toolPanel.size.width; //Subtract the size of the application side panel
		calendarSize.width-=16; // Subtract 16 because of window decorations
		calendarSize.height-=toolbar.size.height; // Subtract the size of the application tool bar
		calendarSize.height-=40; //Subtract 40 because of window decorations
		add(calendarPanel=new CalendarPanel(calendarSize), BorderLayout.CENTER);
		
		//Show the frame
		pack();
		setVisible(true);
		
		//add component listener, this should be the last thing you do because we don't want to call events for no reason
		addComponentListener(this);
		toolbar.nextWeek.addActionListener(this);
		toolbar.lastWeek.addActionListener(this);
		toolbar.weekField.addActionListener(this);
		toolbar.month.addPropertyChangeListener(this);
		toolbar.year.addPropertyChangeListener(this);
	}
	
	public static void main(String[] args) {
		// Run the application
		new CalendarClient();
	}
	
	public void updateDisplayedWeek() {
		calendarPanel.calendar.setDisplayedMonth(toolbar.month.getMonth());
		calendarPanel.calendar.setDisplayedYear(toolbar.year.getYear());
		
		int week=calendarPanel.calendar.getDisplayedWeek();
		String weekString=toolbar.weekField.getText();
		if (weekString.matches("week [0-9][0-9]*")) {
			String s[]=weekString.split(" ");
			weekString=s[1];
		}
		try {
			week=Integer.parseInt(weekString);
		} catch (IllegalArgumentException ex) {
			System.out.println(ex);
		}
		calendarPanel.calendar.setDisplayedWeek(week);
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

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==toolbar.lastWeek || e.getSource()==toolbar.nextWeek) {
			
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent e) {
		
	}
}