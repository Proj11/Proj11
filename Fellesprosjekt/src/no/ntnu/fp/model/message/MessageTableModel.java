package no.ntnu.fp.model.message;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.table.AbstractTableModel;

public class MessageTableModel extends AbstractTableModel {
	
	private List<Message> messages;
	private String[] columnName = {"Sent by", "Message"};
	
	public MessageTableModel(){
		messages = new ArrayList<Message>();
	}
	
	public Message getMessage(int i) {
		return messages.get(i);
	}
	
	public void add(Message message) {
		messages.add(message);
		fireTableDataChanged();
	}
	
	public void remove(Message message) {
		messages.remove(message);
		fireTableDataChanged();
	}
	
	public void addAllMessages(Collection<Message> messages) {
		this.messages.addAll(messages);
		fireTableDataChanged();
	}
	
	public void removeAllMessages(Collection<Message> messages) {
		this.messages.removeAll(messages);
		fireTableDataChanged();
	}

	@Override
	public String getColumnName(int column) {
		return columnName[column];
	}
	
	@Override
	public int getColumnCount() {
		return columnName.length;
	}

	@Override
	public int getRowCount() {
		return messages.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (columnIndex==0) {
			return messages.get(rowIndex).getAppointment().getLeader().getName();
		}
		else if(columnIndex==1){
			return messages.get(rowIndex).getAppointment().getSubject();
		}
		else{
			return null;
		}
	}
}
