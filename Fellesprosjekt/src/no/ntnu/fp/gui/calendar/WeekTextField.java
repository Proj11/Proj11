package no.ntnu.fp.gui.calendar;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JTextField;

public class WeekTextField extends JTextField implements FocusListener, KeyListener {
	
	public final static Color valid=new Color(0, 150, 0);
	public final static Color invalid=new Color(150, 0, 0);
	private int week;
	public final static String WEEK_TEXT ="week: ";
	
	public WeekTextField(int week) {
		this();
		this.week=week;
	}
	
	public WeekTextField() {
		super();
		addFocusListener(this);
		addKeyListener(this);
	}
	
	/**
	 * Set the week you want this text field to display
	 * @param week
	 */
	public void setWeek(int week) {
		this.week=week;
		setText(WEEK_TEXT+week);
	}
	/**
	 * Returns the week being displayed by this text field
	 * @return
	 */
	public int getWeek() {
		return week;
	}

	@Override
	public void focusGained(FocusEvent arg0) {
	}
	@Override
	public void focusLost(FocusEvent arg0) {
		setForeground(Color.BLACK);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		//TODO When a key is released, parse the text to see if it is a valid week
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}
