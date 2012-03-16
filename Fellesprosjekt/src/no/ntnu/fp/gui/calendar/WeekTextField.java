package no.ntnu.fp.gui.calendar;

import javax.swing.JTextField;

public class WeekTextField extends JTextField {
	private int week;
	public final static String WEEK_TEXT ="week: ";
	
	public WeekTextField(int week) {
		super();
		this.week=week;
	}
	
	public WeekTextField() {
		super();
	}
	
	public void setWeek(int week) {
		this.week=week;
		setText(WEEK_TEXT+week);
	}
	
	public int getWeek() {
		return week;
	}
}
