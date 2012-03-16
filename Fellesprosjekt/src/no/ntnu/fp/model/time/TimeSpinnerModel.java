package no.ntnu.fp.model.time;

import javax.swing.AbstractSpinnerModel;

public class TimeSpinnerModel extends AbstractSpinnerModel {

	private Time value;
	
	public TimeSpinnerModel() {
		value=new Time(8, 0);
	}
	
	@Override
	public Object getNextValue() {
		int hour=Math.min(23, value.HOUR+1);
		int minute=value.MINUTE;
		return new Time(hour, minute);
	}

	@Override
	public Object getPreviousValue() {
		int hour=Math.max(0, value.HOUR-1);
		int minute=value.MINUTE;
		return new Time(hour, minute);
	}

	@Override
	public Object getValue() {
		return value;
	}

	@Override
	public void setValue(Object newValue) {
		if (newValue instanceof Time) {
			value=(Time)newValue;
			fireStateChanged();
		}
	}

}
