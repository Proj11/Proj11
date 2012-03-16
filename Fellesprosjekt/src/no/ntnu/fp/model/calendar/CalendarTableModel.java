package no.ntnu.fp.model.calendar;

import java.util.Calendar;
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
	private int week;
	private int month;
	private int year;
	
	public CalendarTableModel(String[] columnNames) {
		week=Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);
		month=Calendar.getInstance().get(Calendar.MONTH);
		year=Calendar.getInstance().get(Calendar.YEAR);
		this.columnNames=columnNames;
		rowCount=24;
		columnCount=columnNames.length;
		appointments=new HashSet<Appointment>();
		
		Appointment temp=new Appointment(new Employee("Herp Derp"));
		temp.setStart(new Time(7, 0));
		temp.setDuration(1);
		temp.setSubject("Hurr durr");
		temp.setDate(Calendar.getInstance().getTime());
		appointments.add(temp);
	}

	public String getColumnName(int col) {
		if (col==0) {
			return columnNames[col]; //Kl
		} else {
			String s="";
			s+=Calendar.getInstance().get(Calendar.DAY_OF_MONTH)+(-Calendar.getInstance().get(Calendar.DAY_OF_WEEK)+col); //Calculate the number of the day
			s+=" ";
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
	
	public int getDisplayedMonth() {
		return month;
	}
	
	public void setDisplayedMonth(int month) {
		this.month=month;
	}
	
	public int getDisplayedYear() {
		return year;
	}
	
	public void setDisplayedYear(int year) {
		this.year=year;
	}
	
	public int getDisplayedWeek() {
		return week;
	}
	
	public void setDisplayedWeek(int week) {
		this.week=week;
	}
}
