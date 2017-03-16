// Authors: Jeremy Yang, Anuj Shah, Jack Murray
// Assignment 3: Search Engine
// File: JsoupTest.java

// Import Statements
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.regex.Pattern;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.process.PTBTokenizer;

public class JsoupTest 
{
	 static void addElement(ConcurrentHashMap<String,ConcurrentLinkedQueue<String>>map, String key, String value)
	    {
	    	ConcurrentLinkedQueue<String> q = map.get(key);
	    	if (q == null) 
	    	{
	    		q = new ConcurrentLinkedQueue<String>();
	    		ConcurrentLinkedQueue<String> curQ = map.putIfAbsent(key, q);
	    		if (curQ != null) 
	    		{
	    			q = curQ;
	    		}
	    	}
	    	q.add(value);
	    }
	 
	public static void main(String[] args) throws IOException, ClassNotFoundException 
	{
		ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> map = new ConcurrentHashMap<>();
		
		PTBTokenizer<CoreLabel> ptbt = new PTBTokenizer<CoreLabel>(new FileReader("D:\\Desktop\\WEBPAGES_RAW\\0\\14"),
			              new CoreLabelTokenFactory(),"");
						  
		while (ptbt.hasNext()) 
		{
		        String label = ptbt.next().originalText();
		        if (! Pattern.matches("^\\<?\\!?\\/?[a-zA-Z][a-zA-Z0-9]*[^<>]*>|<!--.*?-->",label))
		        {
		        	if(!label.equals("``"))
		        		addElement(map,label,"0\\14");
		        }	
		}
		
		// for(Map.Entry<String, ConcurrentLinkedQueue<String>> entry : map.entrySet())
		// {
			// System.out.println(entry.getKey()+"->"+entry.getValue().toString());
		// }
		
		FileOutputStream fileOut = new FileOutputStream("D:\\Desktop\\WEBPAGES_RAW\\test.ser");
		ObjectOutputStream out = new ObjectOutputStream(fileOut);
		out.writeObject(map);
		out.close();
		fileOut.close();
		map = null;
		 
		FileInputStream fileIn = new FileInputStream("D:\\Desktop\\WEBPAGES_RAW\\test.ser");
		ObjectInputStream in = new ObjectInputStream(fileIn);
	    map = (ConcurrentHashMap<String, ConcurrentLinkedQueue<String>>) in.readObject();
	    System.out.println("NEW MAP");
	    
		for (Map.Entry<String, ConcurrentLinkedQueue<String>> entry: map.entrySet())
		{
			System.out.println(entry.getKey()+"->"+entry.getValue().toString());
		}
	}
}

