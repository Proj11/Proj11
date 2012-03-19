package no.ntnu.fp.model.calendar;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.sql.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

import javax.swing.table.AbstractTableModel;

import no.ntnu.fp.model.appointment.Appointment;
import no.ntnu.fp.model.employee.Employee;
import no.ntnu.fp.model.time.Time;

public class CalendarTableModel extends AbstractTableModel {
	
	private int rowCount;
	private int columnCount;
	private String[] columnNames;
	private Set<Appointment> appointments;
	private PropertyChangeSupport pcs;
	private Calendar calendar;
	
	public CalendarTableModel(String[] columnNames) {
		calendar=new GregorianCalendar();
		this.columnNames=columnNames;
		rowCount=24;
		columnCount=columnNames.length;
		appointments=new HashSet<Appointment>();
		
		Appointment temp=new Appointment(new Employee("Herp Derp"));
		temp.setStart(new Time(7, 0));
		temp.setDuration(1);
		temp.setSubject("Hurr durr");
		temp.setDate(new Date(2012, 3, 15));
		appointments.add(temp);
		
		pcs=new PropertyChangeSupport(this);
	}

	public String getColumnName(int col) {
		if (col==0) {
			return columnNames[col]; //Kl
		} else {
			String s="";
			s+=calendar.get(Calendar.DAY_OF_MONTH)+(-calendar.get(Calendar.DAY_OF_WEEK)+col); //Calculate the number of the day
			s+=". ";
			s+=columnNames[col]; //Get the name of the day
			return s;
		}
	}
	
	@Override
	public int getColumnCount() {
		return columnCount;
	}

	@Override
	public int getRowCount() {
		return rowCount;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (columnIndex==0) {
			return new Time(rowIndex, 0);
		} else {
			return null;
		}
	}
	
	public void addPropertyChangeListener(PropertyChangeListener pcl) {
		pcs.addPropertyChangeListener(pcl);
	}
	
	public void removePropertyChangeListener(PropertyChangeListener pcl) {
		pcs.removePropertyChangeListener(pcl);
	}
	
	public int getDisplayedMonth() {
		return calendar.get(Calendar.MONTH);
	}
	
	public void setDisplayedMonth(int month) {
		int oldValue=calendar.get(Calendar.MONTH);
		int newValue=month;
		calendar.set(Calendar.MONTH, month);
		pcs.firePropertyChange("MONTH", oldValue, newValue);
	}
	
	public int getDisplayedYear() {
		return calendar.get(Calendar.YEAR);
	}
	
	public void setDisplayedYear(int year) {
		int oldValue=calendar.get(Calendar.YEAR);
		int newValue=year;
		calendar.set(Calendar.YEAR, year);
		pcs.firePropertyChange("YEAR", oldValue, newValue);
	}
	
	public int getDisplayedWeek() {
		return calendar.get(Calendar.WEEK_OF_YEAR);
	}
	
	public void setDisplayedWeek(int week) {
		week%=53;
		int oldValue=calendar.get(Calendar.WEEK_OF_YEAR);
		int newValue=week;
		calendar.set(Calendar.WEEK_OF_YEAR, week);
		pcs.firePropertyChange("WEEK", oldValue, newValue);
	}
}
