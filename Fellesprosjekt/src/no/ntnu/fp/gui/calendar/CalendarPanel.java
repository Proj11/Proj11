package no.ntnu.fp.gui.calendar;

import java.awt.Dimension;
import java.text.DateFormatSymbols;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import no.ntnu.fp.gui.CalendarClient;
import no.ntnu.fp.model.appointment.Appointment;

public class CalendarPanel extends JPanel {
	
	private CalendarTable calendar;
	private JScrollPane scrollPane;
	
	public CalendarPanel(Dimension size) {
		super();
		String [] columnNames=new DateFormatSymbols(CalendarClient.calendarLocale).getWeekdays();
		columnNames[0]="Time";
		scrollPane=new JScrollPane(calendar=new CalendarTable(columnNames));
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setPreferredSize(size);
		add(scrollPane);
		calendar.setDefaultRenderer(calendar.getColumnClass(1), new CalendarCellRenderer());
	}
	
	public void resizeScrollPane(Dimension d) {
		scrollPane.setPreferredSize(d);
		revalidate();
	}
	
	public int getDisplayedMonth() {
		return calendar.getDisplayedMonth();
	}
	
	public void setDisplayedMonth(int month) {
		calendar.setDisplayedMonth(month);
		repaint();
	}
	
	public int getDisplayedYear() {
		return calendar.getDisplayedYear();
	}
	
	public void setDisplayedYear(int year) {
		calendar.setDisplayedYear(year);
		repaint();
	}
	
	public int getDisplayedWeek() {
		return calendar.getDisplayedWeek();
	}
	
	public void setDisplayedWeek(int week) {
		calendar.setDisplayedWeek(week);
		repaint();
	}
	
	public Appointment getSelectedCell() {
		return calendar.getSelectedCell();
	}
}
