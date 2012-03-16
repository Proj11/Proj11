package no.ntnu.fp.gui.time;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFormattedTextField;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import no.ntnu.fp.timeexception.TimeException;

import no.ntnu.fp.model.time.Time;

public class TimeSpinnerEditor extends JFormattedTextField implements ChangeListener, FocusListener, ActionListener, KeyListener, MouseListener {

	private SpinnerModel model;
	public final static Color valid=new Color(0, 150, 0);
	public final static Color invalid=new Color(150, 0, 0);
	
	public TimeSpinnerEditor(JSpinner spinner) {
		super();
		model=spinner.getModel();
		spinner.addChangeListener(this);
		addFocusListener(this);
		addActionListener(this);
		addKeyListener(this);
		addMouseListener(this);
		setText(model.getValue().toString());
	}
	
	@Override
	public void stateChanged(ChangeEvent e) {
		setText(model.getValue().toString());
	}

	@Override
	public void focusGained(FocusEvent e) {
	}

	@Override
	public void focusLost(FocusEvent e) {
		updateTextField();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		updateTextField();
	}
	
	private void updateTextField() {
		Time newTime;
		try {
			newTime = Time.parseTime(getText());
			if (newTime.isValid()) {
				model.setValue(newTime);
			}
		} catch (TimeException e) {
		}		
		setForeground(Color.BLACK);
		setText(model.getValue().toString());
	}

	@Override
	public void keyPressed(KeyEvent arg0) {}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode()==KeyEvent.VK_ENTER) {
			return;
		}
		try {
			Time time=Time.parseTime(getText());
			if (time.isValid()) {
				setForeground(valid);
			} else {
				setForeground(invalid);
			}
		} catch (TimeException ex) {
			setForeground(invalid);
		}
	}

	@Override
	public void keyTyped(KeyEvent arg0) {}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		try {
			Time time=Time.parseTime(getText());
			if (time.isValid()) {
				setForeground(valid);
			} else {
				setForeground(invalid);
			}
		} catch (TimeException ex) {
			setForeground(invalid);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}
}
