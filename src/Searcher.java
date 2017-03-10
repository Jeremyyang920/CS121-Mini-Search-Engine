// Authors: Jeremy Yang, Anuj Shah, Jack Murray
// Assignment 3: Search Engine
// File: Searcher.java

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Searcher 
{
	private static final int DOC_COUNT = 37497; // number of documents
	private static ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> everything = new ConcurrentHashMap<String,ConcurrentLinkedQueue<String>>(); // complete index
	private static HashMap<String,Integer> uniqueLinks = new HashMap<String,Integer>(); // map where key = document's link and value = number of hits
	private static JSONObject js = getJSON(); // reads bookkeeping.json which contains all document/link pairs
	
	public static JSONObject getJSON()
	{
		JSONParser parser = new JSONParser();
		
		try
		{
			/* CHANGE BASED ON COMPUTER */
			// Object o = parser.parse(new FileReader("D:\\Desktop\\WEBPAGES_RAW\\bookkeeping.json"));
			Object o = parser.parse(new FileReader("C:\\Users\\Jeremy\\Desktop\\WEBPAGES_RAW\\bookkeeping.json"));

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
	
	public static double tfScore(String term, String docLink)
	{
		if (!everything.containsKey(term) || everything.get(term).size() == 0)
			return 0.0;
		
		int count = 0;
		for (String doc: everything.get(term))
		{
			doc = doc.replace("\\", "/");
			
			/* CHANGE BASED ON COMPUTER */
			String convertedDoc = (String) js.get(doc);
			//String convertedDoc = (String) js.get(doc.substring(16));
			
			if (docLink.equals(convertedDoc))
			{
				count++;
			}
		}
		
		if (count == 0)
			return 0.0;
		
		return 1.0 + Math.log(count);
	}
	
	public static double idfScore(String term)
	{
		int N = DOC_COUNT;
		if (!everything.containsKey(term))
			return 0.0;
		int numDocs = everything.get(term).size();
		return 1.0 + Math.log(N/numDocs);
	}
	
	public static double tfidfScore(String[] terms, String link)
	{
		double total = 0.0;
		for (String term: terms)
		{
			total += tfScore(term,link) * idfScore(term); 
		}
		return total;
	}
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws IOException, ClassNotFoundException 
	{
		/* CHANGE BASED ON COMPUTER */
		// FileInputStream fileIn = new FileInputStream("D:\\Desktop\\WEBPAGES_RAW\\everything.ser");
		FileInputStream fileIn = new FileInputStream("C:\\Users\\Jeremy\\Desktop\\WEBPAGES_RAW\\everything.ser");
		//FileInputStream fileIn = new FileInputStream("C:\\Users\\anujs_000\\Desktop\\WEBPAGES_RAW\\everything.ser");
		
		ObjectInputStream in = new ObjectInputStream(fileIn);
        everything = (ConcurrentHashMap<String, ConcurrentLinkedQueue<String>>) in.readObject();
        
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
        		if (everything.get(words[0]) != null)
        			result.addAll(everything.get(words[0]));
        	}
        	else if (words.length > 1)
        	{
        		for (String word: words)
            	{
        			if (everything.get(word) != null)
        				result.addAll(everything.get(word));
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
        		
        		/* CHANGE BASED ON COMPUTER */
        		String link = (String) js.get(s);
        		//String link = (String) js.get(s.substring(16));
        		
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
            			double total1 = tfScore(words[0], o1.getKey());
                		double total2 = tfScore(words[0], o2.getKey());
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
                		double total1 = tfidfScore(words, o1.getKey());
                		double total2 = tfidfScore(words, o2.getKey());
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

