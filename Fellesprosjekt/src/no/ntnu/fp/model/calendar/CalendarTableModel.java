package no.ntnu.fp.model.calendar;

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
	
	static int q=0;
	private int rowCount;
	private int columnCount;
	private String[] columnNames;
	private Set<Appointment> appointments;
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
	}

	public String getColumnName(int col) {
		if (col==0) {
			return columnNames[col]; //Kl
		} else {
			String s="";
			s+=calendar.get(Calendar.DAY_OF_MONTH)+(-calendar.get(Calendar.DAY_OF_WEEK)+col); //Calculate the number of the day
			s+=". ";
			s+=columnNames[col]; //Get the name of the day
			System.out.println(s);
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
		return calendar.get(Calendar.MONTH);
	}
	
	public void setDisplayedMonth(int month) {
		calendar.set(Calendar.MONTH, month);
	}
	
	public int getDisplayedYear() {
		return calendar.get(Calendar.YEAR);
	}
	
	public void setDisplayedYear(int year) {
		calendar.set(Calendar.YEAR, year);
	}
	
	public int getDisplayedWeek() {
		return calendar.get(Calendar.WEEK_OF_YEAR);
	}
	
	public void setDisplayedWeek(int week) {
		calendar.set(Calendar.WEEK_OF_YEAR, week);
		System.out.println(calendar.get(Calendar.WEEK_OF_YEAR));
	}
}
