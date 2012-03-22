package no.ntnu.fp.gui.appointment;

import javax.swing.JOptionPane;


public class PopupConfirmation {
	public final static int YES=0, NO =1;
	int confirm;
	
	public static void main (String args[]){
		new PopupConfirmation();
	}
	public PopupConfirmation(){
		confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this appointment?", "Confirmation",JOptionPane.YES_NO_OPTION );
	
	}
	public int getConfirm() {
		return confirm;
	}
	
}
