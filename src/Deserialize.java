// Authors: Jeremy Yang, Anuj Shah, Jack Murray
// Assignment 3: Search Engine
// File: Deserialize.java

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Deserialize 
{
	private static HashSet<String> uniqueWords = new HashSet<String>();
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws ClassNotFoundException
	{
		// String s = "";
		
		for (int i = 0; i < 75; i++)
		{
			
			try 
			{
				 ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> map;
		         FileInputStream fileIn = new FileInputStream("D:\\Desktop\\WEBPAGES_RAW\\" + Integer.toString(i) + ".ser");
		         ObjectInputStream in = new ObjectInputStream(fileIn);
		         map = (ConcurrentHashMap<String, ConcurrentLinkedQueue<String>>) in.readObject();
		         for (String a: map.keySet())
		         {
		        	 uniqueWords.add(a);
		         }
		         // s += map.toString();
		         // s += "\n\n";
		         in.close();
		         fileIn.close();
		    }
			catch(IOException e) 
			{
		         e.printStackTrace();
		         return;
			}
		}
		
		System.out.println(uniqueWords.size());
		
		// BufferedWriter writer = null;
        // try 
        // {
            // Create a temporary file.
            // String timeLog = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
            // File logFile = new File(timeLog);

            // This will output the full path where the file will be written to...
            // System.out.println(logFile.getCanonicalPath());

            // writer = new BufferedWriter(new FileWriter(logFile));
            // writer.write(s);
        // } 
        // catch (Exception e) 
        // {
            // e.printStackTrace();
        // } 
        // finally 
        // {
            // try 
            // {
                // Close the writer regardless of what happens.
                // writer.close();
            // }
            // catch (Exception e) {}
        // }
	}
}

