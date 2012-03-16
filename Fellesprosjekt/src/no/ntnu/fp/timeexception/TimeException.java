package no.ntnu.fp.timeexception;

public class TimeException extends Exception {
	
	//You have to catch this checked exception when parsing from string to time
	public TimeException(String e) {
		super(e);
	}
}
