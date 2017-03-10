// Authors: Jeremy Yang, Anuj Shah, Jack Murray
// Assignment 3: Search Engine
// File: JsoupTest.java

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class JsoupTest 
{
	public static void main(String[] args) throws IOException, ClassNotFoundException 
	{
		ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> map;
		ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> map2;
		 
		/* CHANGE BASED ON COMPUTER */
        // FileInputStream fileIn = new FileInputStream("D:\\Desktop\\WEBPAGES_RAW\\0.ser");
        FileInputStream fileIn = new FileInputStream("C:\\Users\\anujs_000\\Desktop\\WEBPAGES_RAW\\" + Integer.toString(0) + ".ser");
        
        ObjectInputStream in = new ObjectInputStream(fileIn);
        map = (ConcurrentHashMap<String, ConcurrentLinkedQueue<String>>) in.readObject();
  
        /* CHANGE BASED ON COMPUTER */
        // fileIn = new FileInputStream("D:\\Desktop\\WEBPAGES_RAW\\69.ser");
        fileIn = new FileInputStream("C:\\Users\\anujs_000\\Desktop\\WEBPAGES_RAW\\69.ser");
        
        in = new ObjectInputStream(fileIn);
        map2 = (ConcurrentHashMap<String, ConcurrentLinkedQueue<String>>) in.readObject();
        map.putAll(map2);
        
        for (String a: map.keySet())
  	    {
  	        System.out.println(a+ "-> "+ map.get(a));
  	    }
	}
}

