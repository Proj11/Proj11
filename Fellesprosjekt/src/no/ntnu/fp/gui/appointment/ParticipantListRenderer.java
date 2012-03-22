package no.ntnu.fp.gui.appointment;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import no.ntnu.fp.model.appointment.Participant;
import no.ntnu.fp.model.appointment.Participant.State;

public class ParticipantListRenderer implements ListCellRenderer {
	
	public final static int borderSize=3;
	public final static Color borderColor=new Color(125, 125, 125, 125);
	public final static Color pendingBgColor=new Color(200, 200, 200);
	public final static Color acceptedBgColor=new Color(255, 100, 100);
	public final static Color declinedBgColor=new Color(227, 66, 52);

	@Override
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		if (value instanceof Participant) {
			Participant p=(Participant) value;
			JLabel comp = new JLabel();
			if (p.getState()==State.PENDING) {
				comp.setBackground(pendingBgColor);
			} else if (p.getState()==State.ACCEPTED) {
				comp.setBackground(acceptedBgColor);
			} else if (p.getState()==State.DENIED) {
				comp.setBackground(declinedBgColor);
			}
			if (cellHasFocus) {
				comp.setBorder(BorderFactory.createLineBorder(borderColor, borderSize));
			} else {
				comp.setBorder(BorderFactory.createEmptyBorder(borderSize, borderSize, borderSize, borderSize));
			}
			comp.setText(p.getEmployee().getName());
			return comp;
		}
		return null;
	}

}
