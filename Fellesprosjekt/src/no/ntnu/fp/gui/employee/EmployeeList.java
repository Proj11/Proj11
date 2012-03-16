package no.ntnu.fp.gui.employee;

import java.awt.Dimension;

import javax.swing.JList;

import no.ntnu.fp.model.employee.EmployeeListModel;

public class EmployeeList extends JList {
	
	private EmployeeListModel model;
	public final static Dimension defaultSize=new Dimension(150, 100);
	
	public EmployeeList() {
		super();
		setModel(model=new EmployeeListModel());
		setPreferredSize(defaultSize);
		setMaximumSize(defaultSize);
		setMinimumSize(defaultSize);
	}
}
