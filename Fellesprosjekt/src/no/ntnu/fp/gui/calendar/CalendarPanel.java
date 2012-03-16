package no.ntnu.fp.gui.calendar;

import java.awt.Dimension;
import java.text.DateFormatSymbols;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class CalendarPanel extends JPanel {
	
	public CalendarTable calendar;
	private JScrollPane scrollPane;
	
	public CalendarPanel(Dimension size) {
		super();
		String [] columnNames=new DateFormatSymbols(getLocale()).getWeekdays();
		columnNames[0]="kl";
		scrollPane=new JScrollPane(calendar=new CalendarTable(columnNames));
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setPreferredSize(size);
		add(scrollPane);
	}
	
	public void resizeScrollPane(Dimension d) {
		scrollPane.setPreferredSize(d);
		revalidate();
	}
}
