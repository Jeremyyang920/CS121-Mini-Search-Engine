// Authors: Jeremy Yang, Anuj Shah, Jack Murray
// Assignment 3: Search Engine
// File: Deserialize.java

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Deserialize 
{
	private static HashSet<String> uniqueWords = new HashSet<String>();
	private static ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> everything = new ConcurrentHashMap<String,ConcurrentLinkedQueue<String>>();

	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws ClassNotFoundException
	{
		// String s = "";
		
		long time = System.currentTimeMillis();
		for (int i = 0; i < 75; i++)
		{
			try 
			{
				 ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> map;
				 
				 /* CHANGE BASED ON COMPUTER */
		         FileInputStream fileIn = new FileInputStream("D:\\Desktop\\WEBPAGES_RAW\\" + Integer.toString(i) + ".ser");
		         //FileInputStream fileIn = new FileInputStream("C:\\Users\\anujs_000\\Desktop\\WEBPAGES_RAW\\" + Integer.toString(i) + ".ser");
		         
		         ObjectInputStream in = new ObjectInputStream(fileIn);
		         map = (ConcurrentHashMap<String, ConcurrentLinkedQueue<String>>) in.readObject();
		         everything.putAll(map);

		         // for (String a: map.keySet())
		         // {
		         //		uniqueWords.add(a);
		         // }
		         
		         // s += map.toString();
		         // s += "\n\n";
		         
		         in.close();
		         fileIn.close();
		    }
			catch (IOException e) 
			{
		         e.printStackTrace();
		         return;
			}
		}
		long end = System.currentTimeMillis();
		System.out.println(end-time);
		System.out.println("Done Combining");
		 
		try 
		{
			/* CHANGE BASED ON COMPUTER */
			FileOutputStream fileOut = new FileOutputStream("D:\\Desktop\\WEBPAGES_RAW\\everything.ser");
			//FileOutputStream fileOut = new FileOutputStream("C:\\Users\\anujs_000\\Desktop\\WEBPAGES_RAW\\everything.ser");
			
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
		    out.writeObject(everything);
		    out.close();
		    fileOut.close();
		    System.out.printf("Serialized data is saved.");
		 }
		 catch (IOException i) 
		 {
			 i.printStackTrace();
		 }
		
		//System.out.println(uniqueWords.size());
		
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

