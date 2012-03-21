package no.ntnu.fp.gui.calendar;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import no.ntnu.fp.model.appointment.Appointment;
import no.ntnu.fp.model.time.Time;

public class CalendarCellPanel extends JPanel {

	public final static Color appointmentColor = new Color(200, 221, 242);
	public final static Color timeColor = new Color(245, 245, 255);
	
	public CalendarCellPanel(Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		if(value != null && value instanceof Appointment) {
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			Appointment a=(Appointment)value;
			setBackground(appointmentColor);
			if (row==a.getStart().HOUR) {
				add(new JLabel(a.getLeader().getName()));
				add(new JLabel(a.getStart()+" - "+a.getEnd()));
				add(new JLabel("Subject: "+a.getSubject()));
			}
		} else if (value!=null && value instanceof Time) {
			setBackground(timeColor);
			add(new JLabel(value.toString()));
		} else {
			setBackground(Color.WHITE);
		}
	}
}