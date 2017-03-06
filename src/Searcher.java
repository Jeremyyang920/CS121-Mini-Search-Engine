// Authors: Jeremy Yang, Anuj Shah, Jack Murray
// Assignment 3: Search Engine
// File: Searcher.java

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.jsoup.parser.Parser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Searcher 
{
	private static ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> everything = new ConcurrentHashMap<String,ConcurrentLinkedQueue<String>>();
	private static HashMap<String,Integer> uniqueLinks = new HashMap<String,Integer>();
	
	public static JSONObject getJSON()
	{
		JSONParser parser = new JSONParser();
		try
		{
			/* CHANGE BASED ON COMPUTER */
			// Object o = parser.parse(new FileReader("D:\\Desktop\\WEBPAGES_RAW\\bookkeeping.json"));
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
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws IOException, ClassNotFoundException 
	{
		/* CHANGE BASED ON COMPUTER */
		// FileInputStream fileIn = new FileInputStream("D:\\Desktop\\WEBPAGES_RAW\\everything.ser");
		FileInputStream fileIn = new FileInputStream("C:\\Users\\anujs_000\\Desktop\\WEBPAGES_RAW\\everything.ser");
		
		ObjectInputStream in = new ObjectInputStream(fileIn);
        everything = (ConcurrentHashMap<String, ConcurrentLinkedQueue<String>>) in.readObject();
        
        JSONObject js = getJSON();
        
        Scanner query = new Scanner(System.in);
        String command;
        while (true)
        {
        	System.out.print("\nEnter Query: ");
        	command = query.nextLine().toLowerCase();
        	if (command.equals("!quit"))
        	{
        		System.out.println("Program terminated.");
        		break;
        	}
        	ConcurrentLinkedQueue<String> result = everything.get(command);
        	if (result == null)
        	{
        		result = new ConcurrentLinkedQueue<String>();
        	}
        	String[] words = command.split(" ");
        	if (words.length > 1)
        	{
        		for (String word: words)
            	{
            		result.addAll(everything.get(word));
            	}
        	}
        	if (result.isEmpty())
        	{
        		System.out.println("No results found.");
        		continue;
        	}
        	for (String s: result)
        	{
        		s = s.replace("\\", "/");
        		String link = (String) js.get(s.substring(16));
        		if (!uniqueLinks.containsKey(link))
        		{
        			uniqueLinks.put(link, 0);
        		}
        		uniqueLinks.put(link, uniqueLinks.get(link)+1);
        	}
        	List<Map.Entry<String,Integer>> list = new LinkedList<Map.Entry<String, Integer>>(uniqueLinks.entrySet());
        	Collections.sort(list, new Comparator<Map.Entry<String, Integer>>()
        	{
        		public int compare(Map.Entry<String,Integer> o1, Map.Entry<String,Integer> o2)
        	    {
        			return -(o1.getValue()).compareTo(o2.getValue());
        	    }
        	});
        	for (int i = 0; i < list.size(); i++)
        	{
        		if (i == 5)
        			break;
        		System.out.println(list.get(i).getKey() + ": " + list.get(i).getValue() + " hit(s)");
        	}
        	uniqueLinks.clear();
        }
	}
}

