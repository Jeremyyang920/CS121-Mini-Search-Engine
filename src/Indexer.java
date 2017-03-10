// Authors: Jeremy Yang, Anuj Shah, Jack Murray
// Assignment 3: Search Engine
// File: Indexer.java

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

public class Indexer 
{
    public static File[] listofFiles = new File[74];
    public static ArrayList<File> allFiles = new ArrayList<File>();
    public static ArrayList<String> stopWords = new ArrayList<String>();

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
    	  
    static void i1() throws IOException 
    {
		File[] files = Arrays.copyOfRange(listofFiles,0,37);
		performIndex(files);
    }
    
    static void i2() throws IOException 
    {   
		File[] files = Arrays.copyOfRange(listofFiles,37,75);
		performIndex(files);
    }
    
    static void performIndex(File[] files) throws IOException
    {
    	for (File f: files)
		{	
			ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> map = new ConcurrentHashMap<>();
			DirectoryStream<Path> stream = Files.newDirectoryStream(f.toPath());
			
			for (Path file: stream)
			{
				Document fileDoc;
				
				try // Try to read the file as a HTML file.
				{
					fileDoc = Jsoup.parse(file.toFile(), "UTF-8");
					
			    	// Elements title = fileDoc.getElementsByTag("title");
			    	// Elements h1 = fileDoc.getElementsByTag("h1");
			    	// Elements h2 = fileDoc.getElementsByTag("h2");
			    	// Elements h3 = fileDoc.getElementsByTag("h3");
			    	// Elements bold = fileDoc.getElementsByTag("b");
					
			    	String[] tokens = fileDoc.text().split(" ");
			    	for (String token: tokens)
			    	{
			    		if (stopWords.contains(token) || token.equals(""))
			        	{
			        		continue;
			        	}
			    		String prefix = file.toString().substring(24);
			    		token = token.toLowerCase();
			    		// System.out.println(prefix);
			        	addElement(map,token.replaceAll("[^A-Za-z.-]",""),prefix);
			    	}
				}
				catch (Exception e) // If reading HTML fails, read it as a TXT file.
				{
					String line;
					try 
					(
					    InputStream fis = new FileInputStream(file.toFile());
					    InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
					    BufferedReader br = new BufferedReader(isr);
					) 
					{
					    while ((line = br.readLine()) != null) 
					    {
					        String[] words = line.split(" ");
					        for(String word: words)
					        {
					        	if (stopWords.contains(word) || word.equals(""))
					        	{
					        		continue;
					        	}
					        	String prefix = file.toString().substring(24);
					        	word = word.toLowerCase();
					    		// System.out.println(prefix);
					        	addElement(map,word.replaceAll("[^A-Za-z.-]",""),prefix);
					        }
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
		    System.out.printf("Serialized data is saved. ");
		}
		catch (IOException i) 
		{
			i.printStackTrace();
		}
	}

	public static void main(String[] args) throws InterruptedException, IOException, ClassNotFoundException 
	{
		/* CHANGE BASED ON COMPUTER */
	    // InputStream fis = new FileInputStream(new File("StopWords.txt"));
	    InputStream fis = new FileInputStream(new File("C:\\Users\\anujs_000\\Desktop\\StopWords.txt"));
	    
	    InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
	    BufferedReader br = new BufferedReader(isr);
	    String line;
	    while ((line = br.readLine()) != null)
	    	stopWords.add(line);
	    br.close();
	    
	    /* CHANGE BASED ON COMPUTER */
		// splitFile(Paths.get("D:\\Desktop\\WEBPAGES_RAW"));
		splitFile(Paths.get("C:\\Users\\anujs_000\\Desktop\\WEBPAGES_RAW"));
		
		listofFiles = allFiles.toArray(listofFiles);
		long start = System.nanoTime();

		ExecutorService service = Executors.newFixedThreadPool(2);
		service.submit(() -> {
			try 
			{
				i1();
			} 
			catch (IOException e1) 
			{
				e1.printStackTrace();
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
		service.shutdown();
		service.awaitTermination(1, TimeUnit.HOURS);
		long time = System.nanoTime() - start;
		System.out.println(time);
	}
}

