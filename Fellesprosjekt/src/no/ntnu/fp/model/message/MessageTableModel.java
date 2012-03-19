package no.ntnu.fp.model.message;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

public class MessageTableModel extends AbstractTableModel {
	
	private List<Message> messages;
	private String[] columnName = {"Leader", "Subject", "Go to"};
	
	public MessageTableModel(){
		messages = new ArrayList<Message>();
	}
	
	public void add(Message message){
		messages.add(message);
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
		return null;
	}
}
