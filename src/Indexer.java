// Authors: Jeremy Yang, Anuj Shah, Jack Murray

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.Charset;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Indexer 
{
    public static File[] listofFiles = new File[74];
    public static ArrayList<File> allFiles = new ArrayList<File>();

    static void addElement(ConcurrentHashMap<String,ConcurrentLinkedQueue<String>>map, String key, String value)
    {
    	ConcurrentLinkedQueue<String> q = map.get(key);
    	if (q == null) 
    	{
    	  q = new ConcurrentLinkedQueue<String>();
    	  ConcurrentLinkedQueue<String> curQ = map.putIfAbsent(key, q);
    	  if(curQ != null) 
    	  {
    	    q = curQ;
    	  }
    	}
    	q.add(value);
    }
    
    static ArrayList<File> splitFile(Path path) throws IOException
    {
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) 
    	{
			for (Path entry: stream) 
			{
				if (Files.isDirectory(entry)) 
    	        {
					allFiles.add(entry.toFile());
    	        }
    	    }
    	}
		return allFiles;
    }
    	  
    static void i1() throws FileNotFoundException, IOException
    {
		File[] s1 = Arrays.copyOfRange(listofFiles,0,37);
		String line;
		for (File f: s1)
		{	
			ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> map = new ConcurrentHashMap<>();
			DirectoryStream<Path> stream = Files.newDirectoryStream(f.toPath());
			for(Path file: stream)
			{
//				Document fileDoc = Jsoup.parse(file.toFile(), "UTF-8");
//		    	Elements title = fileDoc.getElementsByTag("title");
//		    	Elements h1 = fileDoc.getElementsByTag("h1");
//		    	Elements h2 = fileDoc.getElementsByTag("h2");
//		    	Elements h3 = fileDoc.getElementsByTag("h3");
//		    	Elements bold = fileDoc.getElementsByTag("b");
		    	
		    	// System.out.println(fileDoc.text());
//		    	for (Element e: title)
//		    		System.out.println(e.text());
//		    	for (Element e: h1)
//		    		System.out.println(e.text());
//		    	for (Element e: h2)
//		    		System.out.println(e.text());
//		    	for (Element e: h3)
//		    		System.out.println(e.text());
//		    	for (Element e: bold)
//		    		System.out.println(e.text());
		    	
				try 
				(
				    InputStream fis = new FileInputStream(file.toFile());
				    InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
				    BufferedReader br = new BufferedReader(isr);
				) 
				{
				    while ((line = br.readLine()) != null) 
				    {
				    	// Document doc = Jsoup.parse(line);
				    	// line = doc.text();
				        String[] words = line.split(" ");
				        for(String word: words)
				        {
				        	if (word.equals("") || word.equals("@") || word.equals("&"))
				        	{
				        		continue;
				        	}
				        	addElement(map,word,f.getName());
				        }
				    }
				}
			}
			outWrite(map,f.getAbsolutePath());
			map = null;
			System.gc();
			System.out.println(f.getAbsolutePath());
		}
    }
    
    static void i2() throws FileNotFoundException, IOException
    {
		File[] s2 = Arrays.copyOfRange(listofFiles, 37, 74);
		String line;
		for (File f: s2)
		{
			ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> map = new ConcurrentHashMap<>();
			DirectoryStream<Path> stream = Files.newDirectoryStream(f.toPath());
			for(Path file: stream)
			{
				try 
				(
				    InputStream fis = new FileInputStream(file.toFile());
				    InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
				    BufferedReader br = new BufferedReader(isr);
				) 
				{
				    while ((line = br.readLine()) != null) 
				    {
				    	// Document doc = Jsoup.parse(line);
				    	// line = doc.text();
				        String[] words = line.split(" ");
				        for(String word: words)
				        {
				        	addElement(map,word,f.getName());
				        }
				    }
				}
			}
			outWrite(map,f.getAbsolutePath());
			map = null;
			System.gc();
			System.out.println(f.getAbsolutePath());
		}
    }
    
	static synchronized void outWrite(ConcurrentHashMap<String,ConcurrentLinkedQueue<String>> map,String dir) throws IOException
	{
		  try 
		  {
		         FileOutputStream fileOut = new FileOutputStream(dir+".ser");
		         ObjectOutputStream out = new ObjectOutputStream(fileOut);
		         out.writeObject(map);
		         out.close();
		         fileOut.close();
		         System.out.printf("Serialized data is saved.");
		  }
		  catch (IOException i) 
		  {
		      i.printStackTrace();
		  }
	}

	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws InterruptedException, IOException, ClassNotFoundException 
	{
		splitFile(Paths.get("C:\\Users\\anujs_000\\Desktop\\WEBPAGES_RAW"));
		// splitFile(Paths.get("C:\\Users\\anujs_000\\Desktop\\Test"));
		// splitFile(Paths.get("C:\\Desktop\\WEBPAGES_RAW"));
		listofFiles = allFiles.toArray(listofFiles);
		ExecutorService service = Executors.newFixedThreadPool(2);
		service.submit(() -> {
			try 
			{
				i1();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		});
		service.submit(() -> {
			try 
			{
				i2();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		});
		service.awaitTermination(1, TimeUnit.SECONDS);
		service.shutdown();
//		try 
//		{
//			 ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> map;
//	         FileInputStream fileIn = new FileInputStream("C:\\Users\\anujs_000\\Desktop\\Test\\4.ser");
//	         ObjectInputStream in = new ObjectInputStream(fileIn);
//	         map = (ConcurrentHashMap<String, ConcurrentLinkedQueue<String>>) in.readObject();
//	         System.out.println(map);
//	         in.close();
//	         fileIn.close();
//	      }
//		catch(IOException i) 
//		{
//	         i.printStackTrace();
//	         return;
//		}
//		for (String key:map.keySet())
//			System.out.println(key + ":" +(map.get(key).toString()));
	}
}

