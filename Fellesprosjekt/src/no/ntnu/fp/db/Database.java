package no.ntnu.fp.db;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.PreparedStatement;


/**
 * A class for the database connection with methods for handling queries.
 *
 */
public class Database {
	
	public static Database database;
	private java.sql.Connection connection;
	
	/**
	 * Creates a connection with the database.
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	private Database() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		String url = "jdbc:mysql://" + DBProperties.HOSTNAME + "/" + DBProperties.DATABASE;
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		connection = DriverManager.getConnection(url, DBProperties.USERNAME, DBProperties.PASSWORD);
	}
	
	/**
	 * A static method that returns a database connection. If a connection already exists, this connection will be returned,
	 * and else a connection is initialized.
	 * @return Database
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static Database getDatabase() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		if (database == null) {
			database = new Database();
		}
		return database;
	}
	
	/**
	 * A method for inserting something to the database.
	 * Executes the query on the database.
	 * @param query
	 * @throws SQLException
	 */
	public void insert(String query) throws SQLException {
		java.sql.PreparedStatement ps;
		ps = connection.prepareStatement(query);
		ps.executeUpdate();
	}
	
	public int insertWithIdReturn(String query) throws SQLException {
		java.sql.PreparedStatement ps;
		ps = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
		ps.executeUpdate();
		ResultSet rs = ps.getGeneratedKeys();
		if (rs.next())
			return rs.getInt(1);
		return -1;
		
	}
	
	/**
	 * Method that executes a query on the database, and returns the result.
	 * @param query
	 * @return ResultSet
	 * @throws SQLException
	 */
	public ResultSet query(String query) throws SQLException {
		ResultSet rs;
		java.sql.PreparedStatement ps = connection.prepareStatement(query);
		rs = ps.executeQuery();
		return rs;
	}
	
	public static void main(String[] args) {
		try {
			Database db = Database.getDatabase();
//			db.insert("INSERT INTO MeetingRoom (roomnr, roomsize) values(1, 2);");
//			db.insert("INSERT INTO Employee (name, username, password) values ('name','brukernavn', 'passord')");
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

