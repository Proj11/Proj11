package no.ntnu.fp.gui.calendar;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class CalendarCellRenderer extends DefaultTableCellRenderer {
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		return new CalendarCellPanel(value, isSelected, hasFocus, row, column);
	}

	
}

