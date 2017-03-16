// Authors: Jeremy Yang, Anuj Shah, Jack Murray
// Assignment 3: Search Engine
// File: SearcherDB.java

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

public class SearcherDB
{
	private static final int DOC_COUNT = 37497; // number of documents
	private static Connection conn; // connection to local database
	private static HashMap<String,ResultSet> sqlResults = new HashMap<String,ResultSet>();
	private static HashMap<String,Integer> uniqueLinks = new HashMap<String,Integer>(); // map where key = document's link and value = number of hits
	private static JSONObject js = getJSON(); // reads bookkeeping.json which contains all document/link pairs
	public static ArrayList<String> stopWords = new ArrayList<String>(); // list of stop words
	private static PriorityQueue<MyEntry> pq = new PriorityQueue<MyEntry>();
	
	public static JSONObject getJSON() 
	{
		JSONParser parser = new JSONParser();
		
		try
		{
			/* CHANGE BASED ON COMPUTER */
			Object o = parser.parse(new FileReader("D:\\Desktop\\WEBPAGES_RAW\\bookkeeping.json"));
			// Object o = parser.parse(new FileReader("C:\\Users\\Jeremy\\Desktop\\WEBPAGES_RAW\\bookkeeping.json"));
			//Object o = parser.parse(new FileReader("C:\\Users\\anujs_000\\Desktop\\WEBPAGES_RAW\\bookkeeping.json"));
			
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
		ResultSet rs = sqlResults.get(term);
		rs.beforeFirst();
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
		ResultSet rs = sqlResults.get(term);
		rs.beforeFirst();
		if (!rs.next())
			return 0.0;
		
		String r = rs.getString("document");
		int numDocs = r.substring(1,r.length()-1).split(", ").length;
		return 1.0 + Math.log(N/numDocs);
	}
	
	public static double tfidfScore(String[] terms, String link) throws SQLException
	{
		double total = 0.0;
		int count=0;
		try
		{
			for (String term: terms)
			{
				if (!stopWords.contains(term))
				{
					Statement h1 = conn.createStatement();
					Statement h2 = conn.createStatement();
					Statement h3 = conn.createStatement();
					Statement b = conn.createStatement();
					String h1s="SELECT COUNT(*) FROM h1  where token = '"+ term +"' and document = '"+ link+"'";
					String h2s="SELECT COUNT(*) FROM h2  where token ='"+ term +"' and document = '"+ link+"'";
					String h3s="SELECT COUNT(*) FROM h3  where token ='"+ term +"' and document = '"+ link+"'";
					String bs="SELECT COUNT(*) FROM b  where token ='"+ term +"' and document = '"+ link+"'";
			
					ResultSet h1r = h1.executeQuery(h1s);
					ResultSet h2r =  h2.executeQuery(h2s);
					ResultSet h3r =  h3.executeQuery(h3s);
					ResultSet br =  b.executeQuery(bs);
					h1r.next();
					h2r.next();
					h3r.next();
					br.next();

					int h1t=Integer.parseInt(h1r.getString(1));
					int h2t=Integer.parseInt(h1r.getString(1));
					int h3t=Integer.parseInt(h1r.getString(1));
					int bt=Integer.parseInt(h1r.getString(1));
					//System.out.println(h1t+ " "+ h2t+" "+ h3t+" "+bt);
					total += tfScore(term,link) * idfScore(term)+(h1t*10+h2t*5+h3t*3+bt*1); 
				}
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return total;

		
	}
	
	public static void main(String[] args) throws SQLException, IOException 
	{
		/* CHANGE BASED ON COMPUTER */
	    InputStream fis = new FileInputStream(new File("StopWords.txt"));
	    //InputStream fis = new FileInputStream(new File("C:\\Users\\anujs_000\\Desktop\\StopWords.txt"));
	    
	    InputStreamReader isr = new InputStreamReader(fis,Charset.forName("UTF-8"));
	    BufferedReader br = new BufferedReader(isr);
	    String line;
	    while ((line = br.readLine()) != null)
	    	stopWords.add(line);
	    br.close();
		
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
        		if (!stopWords.contains(words[0]))
        		{
        			try 
        			{
        				ResultSet rs;
        				if (sqlResults.containsKey(words[0]))
        				{
        					rs = sqlResults.get(words[0]);
        					rs.beforeFirst();
        				}
        				else
        				{
        	        		java.sql.Statement statement = conn.createStatement();
        					String sql = "SELECT document FROM Tokens WHERE token = '" + words[0] + "'";
        					rs = statement.executeQuery(sql);
        					sqlResults.put(words[0], rs);
        				}
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
        	}
        	else if (words.length > 1)
        	{
        		for (String word: words)
            	{
        			if (stopWords.contains(word))
        				continue;
        			try 
            		{
        				ResultSet rs;
        				if (sqlResults.containsKey(word))
        				{
        					rs = sqlResults.get(word);
        					rs.beforeFirst();
        				}
        				else
        				{
        					java.sql.Statement statement = conn.createStatement();
        					String sql = "SELECT document FROM Tokens WHERE token = '" + word + "'";
        					rs = statement.executeQuery(sql);
        					sqlResults.put(word, rs);
        				}
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
        	
        	int topN = 5;
        	List<Map.Entry<String,Integer>> list = new LinkedList<Map.Entry<String, Integer>>(uniqueLinks.entrySet());

        	if (words.length == 1)
        	{
        		Collections.sort(list, new Comparator<Map.Entry<String, Integer>>()
            	{
            		public int compare(Map.Entry<String,Integer> o1, Map.Entry<String,Integer> o2)
            	    {
            			return -(uniqueLinks.get(o1.getKey())).compareTo(uniqueLinks.get(o2.getKey()));

            	    }
            	});
        		
        		for (int i = 0; i < list.size(); i++)
        		{
        			if (i == topN)
        				break;
        			System.out.println(list.get(i).getKey() + ": " + list.get(i).getValue() + " hit(s)");
        		}
        	}
        	else if (words.length > 1)
        	{
        		try
        		{
            		for (String link: uniqueLinks.keySet())
            		{
//            			if(pq.size()>100)
//            				break;
            			pq.add(new MyEntry(link,tfidfScore(words,link)));
            		}
        		}
        		catch (SQLException e)
        		{
        			e.printStackTrace();
        		}
        		
            	int i = 0;
            	while (!pq.isEmpty() && i < 5)
            	{
            		if (i == topN)
            			break;
            		MyEntry value = pq.remove();
            		System.out.println(value.getKey() + ": " + value.getValue());
            		++i;
            	}
        	}
        	uniqueLinks.clear();
        }
	}
}

class MyEntry implements Entry<String, Double>, Comparable<MyEntry> 
{
    private final String key;
    private Double value;
    public MyEntry(final String key) 
    {
        this.key = key;
    }
    public MyEntry(final String key, final Double value) 
    {
        this.key = key;
        this.value = value;
    }
    public String getKey() 
    {
        return key;
    }
    public Double getValue() 
    {
        return value;
    }
    public Double setValue(final Double value) 
    {
        final Double oldValue = this.value;
        this.value = value;
        return oldValue;
    }
	@Override
	public int compareTo(MyEntry other) 
	{
		return -(this.getValue()).compareTo(other.getValue());
	}
}

