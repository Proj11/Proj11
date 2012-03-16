package no.ntnu.fp.db;

import java.io.ObjectInputStream.GetField;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.mysql.jdbc.PreparedStatement;


public class Database {
	
	public static Database database;
	private java.sql.Connection connection;
	
	private Database() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		String url = "jdbc:mysql://" + DBProperties.HOSTNAME + "/" + DBProperties.DATABASE;
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		connection = DriverManager.getConnection(url, DBProperties.USERNAME, DBProperties.PASSWORD);
	}
	
	public static Database getDatabase() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		if (database == null) {
			database = new Database();
		}
		return database;
	}
	
	public void insert(String query) throws SQLException {
		java.sql.PreparedStatement ps;
		ps = connection.prepareStatement(query);
		ps.executeUpdate();
	}
	
	public static void main(String[] args) {
		try {
			Database db = Database.getDatabase();
			db.insert("INSERT INTO MeetingRoom (roomnr, roomsize) values(1, 2);");
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

