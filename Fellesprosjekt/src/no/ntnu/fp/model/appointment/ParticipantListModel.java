package no.ntnu.fp.model.appointment;

import java.util.Collection;
import java.util.List;

import javax.swing.AbstractListModel;


public class ParticipantListModel extends AbstractListModel {
	
	private List<Participant> participantList;
	
	public ParticipantListModel(List<Participant> data) {
		super();
		participantList=data;
	}

	@Override
	public Object getElementAt(int i) {
		return participantList.get(i);
	}

	@Override
	public int getSize() {
		return participantList.size();
	}
	
	public void add(Participant p) {
		int oldSize=participantList.size();
		participantList.add(p);
		int newSize=participantList.size();
		fireIntervalAdded(this, oldSize, newSize);
	}
	
	public void addAll(Collection<Participant> p) {
		int oldSize=participantList.size();
		participantList.addAll(p);
		int newSize=participantList.size();
		fireIntervalAdded(this, oldSize, newSize);
	}
	
	public void remove(Participant p) {
		int oldSize=participantList.size();
		participantList.remove(p);
		int newSize=participantList.size();
		fireIntervalRemoved(this, oldSize, newSize);
	}
	
	public void removeAll(Collection<Participant> p) {
		int oldSize=participantList.size();
		participantList.removeAll(p);
		int newSize=participantList.size();
		fireIntervalRemoved(this, oldSize, newSize);
	}
}
