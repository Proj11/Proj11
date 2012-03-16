package no.ntnu.fp.gui.message;

import java.util.HashSet;
import java.util.Set;

import javax.swing.JPanel;

import no.ntnu.fp.model.message.Message;

public class MessagePanel extends JPanel {
	
	public Set<Message> messages;
	
	public MessagePanel() {
		super();
		messages=new HashSet<Message>();
	}
}
