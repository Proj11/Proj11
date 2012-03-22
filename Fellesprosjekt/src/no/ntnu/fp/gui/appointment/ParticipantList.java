package no.ntnu.fp.gui.appointment;

import java.awt.Dimension;

import javax.swing.JList;
import javax.swing.ListModel;

import no.ntnu.fp.model.appointment.ParticipantListModel;

public class ParticipantList extends JList {
	
	public final static Dimension defaultSize=new Dimension(150, 100);
	
	public ParticipantList() {
		super();
		setPreferredSize(defaultSize);
		setMaximumSize(defaultSize);
		setMinimumSize(defaultSize);
	}
	
	public ParticipantList(ParticipantListModel dataModel) {
		super(dataModel);
		setPreferredSize(defaultSize);
		setMaximumSize(defaultSize);
		setMinimumSize(defaultSize);
	}
}
