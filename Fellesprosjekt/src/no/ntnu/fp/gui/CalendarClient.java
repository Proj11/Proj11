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
		
		//Initialize components
		toolbar=new ApplicationToolbar();
		toolPanel=new ApplicationSidePanel();
		
		//Calculate the size of the calendar
		Dimension calendarSize=new Dimension(getPreferredSize());
		calendarSize.width-=toolPanel.size.width; //Subtract the size of the application side panel
		calendarSize.width-=16; // Subtract 16 because of window decorations
		calendarSize.height-=toolbar.size.height; // Subtract the size of the application tool bar
		calendarSize.height-=40; //Subtract 40 because of window decorations
		calendarPanel=new CalendarPanel(calendarSize);
		
		toolbar.month.setMonth(calendarPanel.calendar.getDisplayedMonth());
		toolbar.year.setYear(calendarPanel.calendar.getDisplayedYear());
		toolbar.week.setWeek(calendarPanel.calendar.getDisplayedWeek());
		
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
	}
	
	public static void main(String[] args) {
		// Run the application
		new CalendarClient();
	}
	
	public void updateDisplayedWeek() {
		calendarPanel.calendar.setDisplayedMonth(toolbar.month.getMonth());
		calendarPanel.calendar.setDisplayedYear(toolbar.year.getYear());
		calendarPanel.calendar.setDisplayedWeek(toolbar.week.getWeek());
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
		if (e.getSource()==toolbar.previousWeek) {
			
		} else if (e.getSource()==toolbar.nextWeek) {
			
		} else if (e.getSource()==toolbar.week) {
			
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent e) {
		updateDisplayedWeek();
	}
}