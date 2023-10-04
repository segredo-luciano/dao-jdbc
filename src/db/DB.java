package db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DB {

	private static Connection connection = null;
	
	public static Connection getConnection() {
		if(connection == null) {
			try {
				Properties props = loadProperties();
				String url = props.getProperty("dburl");
				connection = DriverManager.getConnection(url, props);	
			} catch(SQLException sqle) {
				throw new DbException(sqle.getMessage());
			}			
		}
		
		return connection;
	}
	
	public static void closeConnection() {
		if (connection != null) {
			try {
				connection.close();	
			} catch(SQLException sqle) {
				throw new DbException(sqle.getMessage());
			}
		}
	}
	
	private static Properties loadProperties() {
		try (FileInputStream fis = new FileInputStream("db.properties")) {
			Properties props = new Properties();
			props.load(fis);
			return props;
		} catch(IOException ioe) {
			throw new DbException(ioe.getMessage());
		}
	}
	
	public static void closeStatement(Statement statement) {
		if(statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {
				throw new DbException(e.getMessage());
			}
		}
	}
	
	public static void closeResultSet(ResultSet resultSet) {
		if(resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException e) {
				throw new DbException(e.getMessage());
			}
		}
	}
}
