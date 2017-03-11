// Authors: Jeremy Yang, Anuj Shah, Jack Murray
// Assignment 3: Search Engine
// File: DBConnectionTest.java

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.mysql.jdbc.Connection;

public class DBConnectionTest 
{
	private static ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> everything = new ConcurrentHashMap<String,ConcurrentLinkedQueue<String>>(); // complete index

	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws SQLException, IOException, ClassNotFoundException 
	{
		String connectionUrl = "jdbc:mysql://cs121.cyalk9gzt8mx.us-west-1.rds.amazonaws.com:3306/Indexer";
	    String dbUser = "Jeremy";
	    String dbPwd = "cs121final";
	    Connection conn = null;

	    try 
	    {
	    	conn = (Connection) DriverManager.getConnection(connectionUrl, dbUser, dbPwd);
	        System.out.println("Connected");
		    java.sql.PreparedStatement statement = conn.prepareStatement("INSERT INTO Tokens(token,document) VALUES (?,?)");
			FileInputStream fileIn = new FileInputStream("D:\\Desktop\\WEBPAGES_RAW\\everything.ser");
			ObjectInputStream in = new ObjectInputStream(fileIn);
			
	        everything = (ConcurrentHashMap<String, ConcurrentLinkedQueue<String>>) in.readObject();
	        
	        for(Map.Entry<String, ConcurrentLinkedQueue<String>> entry : everything.entrySet())
	        {
	        	for(String p:entry.getValue())
	        	{
	        		if(!entry.getKey().equals(""))
	        		{
		        		statement.setString(1, entry.getKey());
		        		statement.setString(2, p);
		        		statement.executeUpdate();
	        		}
	        	}
	        }
	  
		    conn.close();
	     } 
	     catch (SQLException e) 
	     {
	    	 e.printStackTrace();
	         System.out.println("Insert Error" + e.getLocalizedMessage());
	     }
	}
}