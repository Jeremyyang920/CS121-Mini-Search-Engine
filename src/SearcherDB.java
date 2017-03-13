import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.mysql.jdbc.Connection;

public class SearcherDB 
{
	private static final int DOC_COUNT = 37497; // number of documents
	private static Connection conn; // connection to local database
	private static HashMap<String,Integer> uniqueLinks = new HashMap<String,Integer>(); // map where key = document's link and value = number of hits
	private static JSONObject js = getJSON(); // reads bookkeeping.json which contains all document/link pairs
	
	public static JSONObject getJSON() 
	{
		JSONParser parser = new JSONParser();
		
		try
		{
			/* CHANGE BASED ON COMPUTER */
			// Object o = parser.parse(new FileReader("D:\\Desktop\\WEBPAGES_RAW\\bookkeeping.json"));
			// Object o = parser.parse(new FileReader("C:\\Users\\Jeremy\\Desktop\\WEBPAGES_RAW\\bookkeeping.json"));
			Object o = parser.parse(new FileReader("C:\\Users\\anujs_000\\Desktop\\WEBPAGES_RAW\\bookkeeping.json"));
			
			JSONObject json = (JSONObject) o;
			return json;
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public static double tfScore(String term, String docLink) throws SQLException
	{
		java.sql.Statement statement = conn.createStatement();
		String sql = "SELECT document FROM Tokens WHERE token = '" + term + "'";
		ResultSet rs = statement.executeQuery(sql);
		if (!rs.next())
			return 0.0;
		
		int count = 0;
		String r = rs.getString("document");
		for (String doc: r.substring(1,r.length()-1).split(", "))
		{
			doc = doc.replace("\\", "/");
			
			String convertedDoc = (String) js.get(doc);
			
			if (docLink.equals(convertedDoc))
			{
				count++;
			}
		}
		
		if (count == 0)
			return 0.0;
		
		return 1.0 + Math.log(count);
	}
	
	public static double idfScore(String term) throws SQLException
	{
		int N = DOC_COUNT;
		java.sql.Statement statement = conn.createStatement();
		String sql = "SELECT document FROM Tokens WHERE token = '" + term + "'";
		ResultSet rs = statement.executeQuery(sql);
		if (!rs.next())
			return 0.0;
		String r = rs.getString("document");
		int numDocs = r.substring(1,r.length()-1).split(", ").length;
		return 1.0 + Math.log(N/numDocs);
	}
	
	public static double tfidfScore(String[] terms, String link) throws SQLException
	{
		double total = 0.0;
		for (String term: terms)
		{
			total += tfScore(term,link) * idfScore(term); 
		}
		return total;
	}
	
	public static void main(String[] args) throws SQLException 
	{
		String connectionUrl = "jdbc:mysql://127.0.0.1:3306/cs121";
		Scanner db = new Scanner(System.in);
	    // String dbUser = "jeremy";
		System.out.print("Username: ");
		String dbUser = db.nextLine();
	    // String dbPwd = "";
		System.out.print("Password: ");
	    String dbPwd = db.nextLine();
		conn = null;
		
		try 
		{
			conn = (Connection) DriverManager.getConnection(connectionUrl, dbUser, dbPwd);
			System.out.println("Connected");
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		Scanner query = new Scanner(System.in);
        String command;
 
        while (true)
        {
        	System.out.print("\nEnter Query: ");
        	
        	command = query.nextLine().toLowerCase();
        	
        	// Quit Program
        	if (command.equals("!quit"))
        	{
        		System.out.println("Program terminated.");
        		break;
        	}
        	
        	ConcurrentLinkedQueue<String> result = new ConcurrentLinkedQueue<String>();
        	String[] words = command.split(" ");
        	
        	if (words.length == 1)
        	{
        		try 
        		{
					java.sql.Statement statement = conn.createStatement();
					String sql = "SELECT document FROM Tokens WHERE token = '" + words[0] + "'";
					ResultSet rs = statement.executeQuery(sql);
					while (rs.next())
					{
						String r = rs.getString("document");
						String[] rColl = (r.substring(1,r.length()-1)).split(", ");
						for (String s: rColl)
						{
							result.add(s);
						}
					}
				} 
        		catch (SQLException e) 
        		{
					e.printStackTrace();
				}
        	}
        	else if (words.length > 1)
        	{
        		for (String word: words)
            	{
        			try 
            		{
    					java.sql.Statement statement = conn.createStatement();
    					String sql = "SELECT document FROM Tokens WHERE token = '" + word + "'";
    					ResultSet rs = statement.executeQuery(sql);
    					if (rs.next())
    					{
    						String r = rs.getString("document");
    						String[] rColl = (r.substring(1,r.length()-1)).split(", ");
    						for (String s: rColl)
    						{
    							result.add(s);
    						}
    					}
    				} 
            		catch (SQLException e) 
            		{
    					e.printStackTrace();
    				}
            	}
        	}
        	
        	// No document matches.
        	if (result.isEmpty())
        	{
        		System.out.println("No results found.");
        		continue;
        	}
        	
        	for (String s: result)
        	{
        		s = s.replace("\\", "/");
        		
        		String link = (String) js.get(s);
        		
        		if (!uniqueLinks.containsKey(link))
        		{
        			uniqueLinks.put(link, 0);
        		}
        		uniqueLinks.put(link, uniqueLinks.get(link)+1);
        	}
        	
        	List<Map.Entry<String,Integer>> list = new LinkedList<Map.Entry<String, Integer>>(uniqueLinks.entrySet());
        	if (words.length == 1)
        	{
        		Collections.sort(list, new Comparator<Map.Entry<String, Integer>>()
            	{
            		public int compare(Map.Entry<String,Integer> o1, Map.Entry<String,Integer> o2)
            	    {
            			double total1 = 0.0;
						try 
						{
							total1 = tfScore(words[0], o1.getKey());
						} 
						catch (SQLException e) 
						{
							e.printStackTrace();
						}
                		double total2 = 0.0;
						try 
						{
							total2 = tfScore(words[0], o2.getKey());
						} 
						catch (SQLException e) 
						{
							e.printStackTrace();
						}
                		return -(new Double(total1)).compareTo(new Double(total2));
            	    }
            	});
        	}
        	else if (words.length > 1)
        	{
        		Collections.sort(list, new Comparator<Map.Entry<String, Integer>>()
            	{
            		public int compare(Map.Entry<String,Integer> o1, Map.Entry<String,Integer> o2)
            	    {
                		double total1 = 0.0;
						try 
						{
							total1 = tfidfScore(words, o1.getKey());
						} 
						catch (SQLException e) 
						{
							e.printStackTrace();
						}
                		double total2 = 0.0;
						try 
						{
							total2 = tfidfScore(words, o2.getKey());
						} 
						catch (SQLException e) 
						{
							e.printStackTrace();
						}
                		return -(new Double(total1)).compareTo(new Double(total2));
            	    }
            	});
        	}
        	
        	int topN = 5;
        	for (int i = 0; i < list.size(); i++)
        	{
        		if (i == topN)
        			break;
        		if (words.length == 1)
        			System.out.println(list.get(i).getKey() + ": " + list.get(i).getValue() + " hit(s)");
        		else if (words.length > 1)
        			System.out.println(list.get(i).getKey() + ": " + tfidfScore(words, list.get(i).getKey()));
        	}
        	uniqueLinks.clear();
        }
	}
}

