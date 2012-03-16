package no.ntnu.fp.gui.time;


import java.awt.Dimension;

import javax.swing.JSpinner;

import no.ntnu.fp.model.time.TimeSpinnerModel;

public class TimeSpinner extends JSpinner {
	public TimeSpinner() {
		super();
		setModel(new TimeSpinnerModel());
		setEditor(new TimeSpinnerEditor(this));
		setPreferredSize(new Dimension(90, 24));
		setToolTipText("hh:mm");
	}
}
