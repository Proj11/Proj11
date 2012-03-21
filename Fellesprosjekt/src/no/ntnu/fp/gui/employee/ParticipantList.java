package no.ntnu.fp.gui.employee;

import java.awt.Dimension;

import javax.swing.JList;

public class ParticipantList extends JList {
	
	public final static Dimension defaultSize=new Dimension(150, 100);
	
	public ParticipantList() {
		super();
		setPreferredSize(defaultSize);
		setMaximumSize(defaultSize);
		setMinimumSize(defaultSize);
	}
}
