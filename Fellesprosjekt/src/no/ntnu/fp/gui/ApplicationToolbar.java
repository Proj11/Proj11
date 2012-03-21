package no.ntnu.fp.gui;

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import no.ntnu.fp.gui.calendar.WeekTextField;

import com.toedter.calendar.JMonthChooser;
import com.toedter.calendar.JYearChooser;

public class ApplicationToolbar extends JPanel {
	
	public static final Dimension size=new Dimension(CalendarClient.size.width, 40); //Default size of the component
	public JLabel messageLabel; //This label tells you if you have any messages
	public WeekTextField week; //This TextField shows which week is displayed in the calendar
	public JButton changeConnectivityButton, nextWeek, previousWeek;  //Some intuitive button
	public JYearChooser year; //This object is used to show which year is used in the calendar
	public JMonthChooser month; //This object is used to show which month is used in the calendar
	public JLabel stateLabel; //This label displays your online status
	
	public ApplicationToolbar() {
		super();
		//Set parameters
		setPreferredSize(size);
		setMaximumSize(size);
		setMinimumSize(size);
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		//Add components and set their default values
		add(Box.createHorizontalStrut(10));
		messageLabel=new JLabel();
		messageLabel.setIcon(new ImageIcon("res/new.png"));
		messageLabel.setText("3 New Notifications");
		messageLabel.setHorizontalAlignment(JLabel.CENTER);
		messageLabel.setMaximumSize(new Dimension(160,28));
		add(messageLabel);
		
		add(Box.createGlue());
		
		year = new JYearChooser();
		year.setPreferredSize(new Dimension(80, 28));
		year.setMaximumSize(new Dimension(80, 28));
		add(year);
		add(Box.createHorizontalStrut(5));
		month = new JMonthChooser();
		month.setPreferredSize(new Dimension(120, 28));
		month.setMaximumSize(new Dimension(120, 28));
		add(month);
		add(Box.createHorizontalStrut(5));
		previousWeek=new JButton("<");
		previousWeek.setMaximumSize(new Dimension(40, 28));
		add(previousWeek);
		week=new WeekTextField();
		week.setPreferredSize(new Dimension(80, 28));
		week.setMaximumSize(new Dimension(80, 28));
		add(week);
		nextWeek=new JButton(">");
		nextWeek.setMaximumSize(new Dimension(40, 28));
		add(nextWeek);
		add(Box.createGlue());
		
		stateLabel = new JLabel();
		stateLabel.setIcon(new ImageIcon("res/online.png"));
		stateLabel.setText("Welcome Hans Hansen");
		add(stateLabel);
		add(Box.createHorizontalStrut(5));
		changeConnectivityButton=new JButton("Logoff");
		changeConnectivityButton.setMaximumSize(new Dimension(80, 28));
		add(changeConnectivityButton);
		add(Box.createHorizontalStrut(10));
	}
}