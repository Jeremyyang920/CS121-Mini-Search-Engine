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
		String connectionUrl = "jdbc:mysql://127.0.0.1:3306/cs121";
	    String dbUser = "jeremy";
	    String dbPwd = "";
		Connection conn = null;

	    try 
	    {
	    	conn = (Connection) DriverManager.getConnection(connectionUrl, dbUser, dbPwd);
	        System.out.println("Connected");
		    java.sql.PreparedStatement statement = conn.prepareStatement("INSERT INTO tokens(token,document) VALUES (?,?)");
			
		    /* CHANGE BASED ON COMPUTER */
		    // FileInputStream fileIn = new FileInputStream("D:\\Desktop\\WEBPAGES_RAW\\everything.ser");
			FileInputStream fileIn = new FileInputStream("C:\\Users\\anujs_000\\Desktop\\WEBPAGES_RAW\\everything.ser");
		    
		    ObjectInputStream in = new ObjectInputStream(fileIn);
			
	        everything = (ConcurrentHashMap<String, ConcurrentLinkedQueue<String>>) in.readObject();
	        
	        for (Map.Entry<String, ConcurrentLinkedQueue<String>> entry: everything.entrySet())
	        {
	        	if (!entry.getKey().equals(""))
	        	{
	        		statement.setString(1, entry.getKey());
	        		statement.setString(2, entry.getValue().toString());
	        		statement.executeUpdate();
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

