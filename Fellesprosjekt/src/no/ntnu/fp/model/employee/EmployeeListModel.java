package no.ntnu.fp.model.employee;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

public class EmployeeListModel implements ListModel {
	
	private List<Employee> employeeList;
	private Set<ListDataListener> listDataListeners;
	
	public void fireIntervalAdded(ListDataEvent e) {
		for (ListDataListener listener : listDataListeners) {
			listener.intervalAdded(e);
		}
	}
	
	public void fireIntervalRemoved(ListDataEvent e) {
		for (ListDataListener listener : listDataListeners) {
			listener.intervalRemoved(e);
		}
	}
	
	public EmployeeListModel() {
		employeeList=new ArrayList<Employee>();
		listDataListeners=new HashSet<ListDataListener>();
	}

	@Override
	public void addListDataListener(ListDataListener listener) {
		listDataListeners.add(listener);
	}

	@Override
	public Object getElementAt(int i) {
		return employeeList.get(i);
	}

	@Override
	public int getSize() {
		return employeeList.size();
	}

	@Override
	public void removeListDataListener(ListDataListener listener) {
		listDataListeners.remove(listener);
	}
}
