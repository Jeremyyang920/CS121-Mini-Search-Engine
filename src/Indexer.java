// Authors: Jeremy Yang, Anuj Shah, Jack Murray
// Assignment 3: Search Engine
// File: Indexer.java

// Import Statements
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
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
import java.util.regex.Pattern;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.process.CoreLabelTokenFactory;

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
    		if (curQ != null) 
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
		File[] files = Arrays.copyOfRange(listofFiles,0,18);
		performIndex(files);
    }
    
    static void i2() throws IOException 
    {   
		File[] files = Arrays.copyOfRange(listofFiles,18,36);
		performIndex(files);
    }
    static void i3() throws IOException 
    {   
		File[] files = Arrays.copyOfRange(listofFiles,36,54);
		performIndex(files);
    }
    
    static void i4() throws IOException 
    {   
		File[] files = Arrays.copyOfRange(listofFiles,54,75);
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
				try
				{
					/* CHANGE BASED ON COMPUTER */
		    		String prefix = file.toString().substring(24);
		    		// String prefix = file.toString().substring(40);
		    		
					PTBTokenizer<CoreLabel> ptbt = new PTBTokenizer<CoreLabel>(new FileReader(file.toString()),new CoreLabelTokenFactory(),"untokenizable=noneDelete,normalizeParentheses=false");
					while (ptbt.hasNext()) 
				    {
				        String label = ptbt.next().originalText().toLowerCase();
				        if (!Pattern.matches("^\\<?\\!?\\/?[a-zA-Z][a-zA-Z0-9]*[^<>]*>|<!--.*?-->",label) && Pattern.matches("^[a-zA-Z0-9,.;:_'\\s-]+$",label))
				        {
				        	if (stopWords.contains(label) || label.equals("``"))
				        	{
				        		continue;
				        	}
				        	addElement(map,label.replace("[^A-Za-z0-9]",""),prefix);
				        }	
				    }
				}
				catch (Exception e)
				{
					System.out.println(e.getMessage());
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
		    System.out.printf("Serialized data is saved: ");
		}
		catch (IOException i) 
		{
			i.printStackTrace();
		}
	}

	public static void main(String[] args) throws InterruptedException, IOException, ClassNotFoundException 
	{
		/* CHANGE BASED ON COMPUTER */
	    InputStream fis = new FileInputStream(new File("StopWords.txt"));
	    // InputStream fis = new FileInputStream(new File("C:\\Users\\anujs_000\\Desktop\\StopWords.txt"));
	    
	    InputStreamReader isr = new InputStreamReader(fis,Charset.forName("UTF-8"));
	    BufferedReader br = new BufferedReader(isr);
	    String line;
	    while ((line = br.readLine()) != null)
	    	stopWords.add(line);
	    br.close();
	    
	    /* CHANGE BASED ON COMPUTER */
		splitFile(Paths.get("D:\\Desktop\\WEBPAGES_RAW"));
	    // splitFile(Paths.get("C:\\Users\\Jeremy\\Desktop\\WEBPAGES_RAW"));
		// splitFile(Paths.get("C:\\Users\\anujs_000\\Desktop\\WEBPAGES_RAW"));
		
		listofFiles = allFiles.toArray(listofFiles);
		long start = System.nanoTime();

		ExecutorService service = Executors.newFixedThreadPool(2);
		ExecutorService service2 = Executors.newFixedThreadPool(2);

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
		service2.submit(() -> {
			try 
			{
				i3();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		});
		service2.submit(() -> {
			try 
			{
				i4();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		});
		service.shutdown();
		service2.shutdown();
		service.awaitTermination(1,TimeUnit.HOURS);
		service2.awaitTermination(1,TimeUnit.HOURS);
		long time = System.nanoTime() - start;
		System.out.println(time);
	}
}

