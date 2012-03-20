package no.ntnu.fp.model.time;

import no.ntnu.fp.timeexception.TimeException;

public class Time implements Comparable<Time> {
	public final int HOUR;
	public final int MINUTE;
	
	public Time(int h, int m) {
		HOUR=h;
		MINUTE=m;
	}
	
	public boolean isValid() {
		return HOUR<24 && HOUR>=0 && MINUTE<60 && MINUTE>=0;
	}
	
	public String toString() {
		String hour=HOUR+"";
		String min=MINUTE+"";
		if (HOUR<10) {
			hour="0"+hour;
		}
		if (MINUTE<10) {
			min="0"+min;
		}
		
		return hour+":"+min;
	}
	
	public static Time parseTime(String time) throws TimeException {
		if (time.matches("[0-9][0-9]*")) {
			int i=Integer.parseInt(time);
			Time t=new Time(i, 0);
			if (t.isValid()) {
				return t;
			} else {
				throw new TimeException("Invalid time");
			}
		} else if (time.matches("[0-9][0-9]:[0-9][0-9]")) {
			String[] s = time.split(":");
			int h=Integer.parseInt(s[0]);
			int m=Integer.parseInt(s[1]);
			Time t=new Time(h, m);
			if (t.isValid()) {
				return t;
			} else {
				throw new TimeException("Invalid time");
			}
		}
		throw new TimeException("Invalid format!");
	}

	@Override
	public int compareTo(Time o) {
		if (this.HOUR-o.HOUR==0) {
			return this.MINUTE-o.MINUTE;
		} else {
			return this.HOUR-o.HOUR;
		}
	}
}
