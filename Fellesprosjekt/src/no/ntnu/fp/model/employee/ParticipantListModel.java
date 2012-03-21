package no.ntnu.fp.model.employee;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import no.ntnu.fp.model.appointment.Participant;

public class ParticipantListModel implements ListModel {
	
	private List<Participant> participantList;
	private List<ListDataListener> listDataListeners;
	
	public ParticipantListModel(List<Participant> data) {
		participantList=data;
		listDataListeners=new ArrayList<ListDataListener>();
	}
	
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

	@Override
	public void addListDataListener(ListDataListener listener) {
		listDataListeners.add(listener);
	}

	@Override
	public Object getElementAt(int i) {
		return participantList.get(i);
	}

	@Override
	public int getSize() {
		return participantList.size();
	}

	@Override
	public void removeListDataListener(ListDataListener listener) {
		listDataListeners.remove(listener);
	}
}
