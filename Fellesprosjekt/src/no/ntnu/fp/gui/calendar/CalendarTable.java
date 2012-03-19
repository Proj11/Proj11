package no.ntnu.fp.gui.calendar;

import java.beans.PropertyChangeListener;

import javax.swing.JTable;

import no.ntnu.fp.model.calendar.CalendarTableModel;

public class CalendarTable extends JTable {
	
	private CalendarTableModel model;

	public CalendarTable(String[] columnNames) {
		setModel(model=new CalendarTableModel(columnNames));
		setRowHeight(50);
		getTableHeader().setReorderingAllowed(false);
	}
	
	public void addPropertyChangeListenerToModel(PropertyChangeListener pcl) {
		model.addPropertyChangeListener(pcl);
	}
	
	public void removePropertyChangeListenerFromModel(PropertyChangeListener pcl) {
		model.removePropertyChangeListener(pcl);
	}
	
	public int getDisplayedMonth() {
		return model.getDisplayedMonth();
	}
	
	public void setDisplayedMonth(int month) {
		model.setDisplayedMonth(month);
	}
	
	public int getDisplayedYear() {
		return model.getDisplayedYear();
	}
	
	public void setDisplayedYear(int year) {
		model.setDisplayedYear(year);
	}
	
	public int getDisplayedWeek() {
		return model.getDisplayedWeek();
	}
	
	public void setDisplayedWeek(int week) {
		model.setDisplayedWeek(week);
	}
}
