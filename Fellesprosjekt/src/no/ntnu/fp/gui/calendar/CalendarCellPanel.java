package no.ntnu.fp.gui.calendar;

import java.awt.Color;

import javax.swing.JPanel;

public class CalendarCellPanel extends JPanel {

	public CalendarCellPanel(Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		if(value != null) {
			setBackground(Color.orange);
		}
		if(isSelected) {
			setBackground(new Color(r, g, b));
		}
	}
}