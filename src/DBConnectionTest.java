// Authors: Jeremy Yang, Anuj Shah, Jack Murray
// Assignment 3: Search Engine
// File: DBConnectionTest.java

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.mysql.jdbc.Connection;

public class DBConnectionTest 
{
	public static void main(String[] args) throws SQLException 
	{
		String connectionUrl = "jdbc:mysql://cs121.cyalk9gzt8mx.us-west-1.rds.amazonaws.com:3306/Indexer";
	    String dbUser = "Jeremy";
	    String dbPwd = "cs121final";
	    Connection conn = null;

	    try 
	    {
	    	conn = (Connection) DriverManager.getConnection(connectionUrl, dbUser, dbPwd);
	        System.out.println("conn Available");
		    java.sql.Statement statement = conn.createStatement();
				
		    statement.executeUpdate("INSERT INTO Tokens " + "VALUES ('hello','0/121')");
		    String sql = "SELECT * FROM Tokens";
				
		    ResultSet rs = statement.executeQuery(sql);
			while(rs.next())
			{
				String first = rs.getString("token");
			    String last = rs.getString("document");
   
			    System.out.println("First: " + first);
			    System.out.println("Last: " + last);
			}
			rs.close();
		    conn.close();
	     } 
	     catch (SQLException e) 
	     {
	    	 e.printStackTrace();
	         System.out.println("fetch otion error" + e.getLocalizedMessage());
	     }
	}
}