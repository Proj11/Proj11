package no.ntnu.fp.model.message;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.table.AbstractTableModel;

public class MessageTableModel extends AbstractTableModel {
	
	private List<Message> messages;
	private String[] columnName = {"Leader", "Subject"};
	JButton button = new JButton("-->");
	
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
