package no.ntnu.fp.model.calendar;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import no.ntnu.fp.model.appointment.Appointment;
import no.ntnu.fp.model.employee.Employee;
import no.ntnu.fp.model.time.Time;

public class CalendarTableModel extends AbstractTableModel {
	
	static int q=0;
	private int rowCount;
	private int columnCount;
	private String[] columnNames;
	private List<Appointment> appointments;
	private GregorianCalendar calendar;
	
	
	public CalendarTableModel(String[] columnNames) {
		calendar=new GregorianCalendar();
		calendar.setFirstDayOfWeek(Calendar.SUNDAY);
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		this.columnNames=columnNames;
		rowCount=24;
		columnCount=columnNames.length;
		appointments=new ArrayList<Appointment>();
		
		Appointment temp;
		temp=new Appointment(new Employee("Herp Derp"));
		temp.setStart(new Time(7, 0));
		temp.setEnd(new Time(12, 0));
		temp.setSubject("Hurr durr");
		temp.setDate(new Date(2012, 2, 21));
		appointments.add(temp);
	}

	public String getColumnName(int col) {
		if (col==0) {
			return columnNames[col]; //Kl
		} else {
			calendar.set(Calendar.DAY_OF_WEEK, col);
			String s="";
			s+=calendar.get(Calendar.DAY_OF_MONTH);
			s+=" ";
			s+=columnNames[col];
			calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
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
		Time now = new Time(rowIndex, 0);
		if (columnIndex==0) {
			return now;
		} else {
			calendar.set(Calendar.DAY_OF_WEEK, columnIndex);
			Appointment returnValue=null;
			for (Appointment a : appointments) {
				if (a.getDate().getDate()==calendar.get(Calendar.DAY_OF_MONTH) && a.getDate().getYear()==calendar.get(Calendar.YEAR) && a.getDate().getMonth()==calendar.get(Calendar.MONTH)) {
					if (a.getStart().compareTo(now)<=0 && a.getEnd().compareTo(now)>0) {
						returnValue=a;
					}
				}
			}
			calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
			return returnValue;
		}
	}
	
	public void addAppointment(Appointment a) {
		appointments.add(a);
	}
	
	public void removeAppointment(Appointment a) {
		appointments.remove(a);
	}
	
	public void addAllAppointments(Collection<Appointment> a) {
		appointments.addAll(a);
	}
	
	public void removeAllAppointments(Collection<Appointment> a) {
		appointments.removeAll(a);
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
	}
}