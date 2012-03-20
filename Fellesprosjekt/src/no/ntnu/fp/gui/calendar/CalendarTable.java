package no.ntnu.fp.gui.calendar;

import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import no.ntnu.fp.model.appointment.Appointment;
import no.ntnu.fp.model.calendar.CalendarTableModel;

public class CalendarTable extends JTable {
	
	private CalendarTableModel model;

	
	
	public CalendarTable(String[] columnNames) {
		model=new CalendarTableModel(columnNames);
		setModel(model);
		setRowHeight(50);
		setColumnSelectionAllowed(false);
		setRowSelectionAllowed(false);
		getTableHeader().setReorderingAllowed(false);
	}
	
	public int getDisplayedMonth() {
		return model.getDisplayedMonth();
	}
	
	public void setDisplayedMonth(int month) {
		model.setDisplayedMonth(month);
		changeHeaderValues();
	}
	
	public int getDisplayedYear() {
		return model.getDisplayedYear();
	}
	
	public void setDisplayedYear(int year) {
		model.setDisplayedYear(year);
		changeHeaderValues();
	}
	
	public int getDisplayedWeek() {
		return model.getDisplayedWeek();
	}
	
	public void setDisplayedWeek(int week) {
		model.setDisplayedWeek(week);
		changeHeaderValues();
	}
	
	private void changeHeaderValues() {
		JTableHeader th = getTableHeader();
		TableColumnModel tcm = th.getColumnModel();
		TableColumn tc;
		for (int i = 1; i < model.getColumnCount(); i++) {
			tc = tcm.getColumn(i);
			tc.setHeaderValue(model.getColumnName(i));
			
		}
		th.repaint();
	}
	
	public Appointment getSelectedCell() {
		Object o=model.getValueAt(getSelectedRow(), getSelectedColumn());
		if (o instanceof Appointment) {
			return (Appointment)o; 
		}
		return null;
	}
}
