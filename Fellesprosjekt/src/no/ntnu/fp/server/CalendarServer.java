package no.ntnu.fp.server;

import java.sql.ResultSet;

import no.ntnu.fp.db.Database;

public class CalendarServer {
	
	
	public CalendarServer() {
		
	}
	
	public static boolean logon(String username, String password) throws Exception{
		Database db = Database.getDatabase();
		ResultSet rs = db.query("SELECT * FROM Employee WHERE username='" + username + "' AND password='" + password + "'");
		if (rs.next())
			return true;
		return false;
	}

}
