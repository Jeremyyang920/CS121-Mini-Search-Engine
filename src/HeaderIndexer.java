// Authors: Jeremy Yang, Anuj Shah, Jack Murray
// Assignment 3: Search Engine
// File: HeaderIndexer.java

// Import Statements
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.DriverManager;
import java.sql.SQLException;
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
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.mysql.jdbc.Connection;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.process.CoreLabelTokenFactory;

public class HeaderIndexer 
{
	public static File[] listofFiles = new File[74];
    public static ArrayList<File> allFiles = new ArrayList<File>();
    public static ArrayList<String> stopWords = new ArrayList<String>();
	private static String connectionUrl = "jdbc:mysql://127.0.0.1:3306/cs121";
    private static String dbUser = "jeremy";
    private static String dbPwd = "";
	private static Connection conn = null;

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
		    		
					// Find all of the <h1>, <h2>, <h3>, and <b> tags.
		    		Document fileDoc = Jsoup.parse(file.toFile(), "UTF-8");
		    		Elements h1 = fileDoc.getElementsByTag("h1");
		    		Elements h2 = fileDoc.getElementsByTag("h2");
		    		Elements h3 = fileDoc.getElementsByTag("h3");
		    		Elements bold = fileDoc.getElementsByTag("b");
		    		
		    		for (Element e: h1)
		    		{
		    			java.sql.PreparedStatement statement = conn.prepareStatement("INSERT INTO h1(token,document) VALUES (?,?)");
		    			for (String s: e.text().toLowerCase().split(" "))
		    			{
		    				if (stopWords.contains(s))
		    					continue;
		    				if (!s.equals("") && !Pattern.matches("^\\<?\\!?\\/?[a-zA-Z][a-zA-Z0-9]*[^<>]*>|<!--.*?-->",s) && Pattern.matches("^[a-zA-Z0-9,.;:_'\\s-]+$",s))
		    				{
		    					statement.setString(1, s.replace("[^A-Za-z0-9]",""));
		    	        		statement.setString(2, prefix);
		    					statement.executeUpdate();
		    				}
		    			}
		    		}
		    		
		    		for (Element e: h2)
		    		{
		    			java.sql.PreparedStatement statement = conn.prepareStatement("INSERT INTO h2(token,document) VALUES (?,?)");
		    			for (String s: e.text().toLowerCase().split(" "))
		    			{
		    				if (stopWords.contains(s))
		    					continue;
		    				if (!s.equals("") && !Pattern.matches("^\\<?\\!?\\/?[a-zA-Z][a-zA-Z0-9]*[^<>]*>|<!--.*?-->",s) && Pattern.matches("^[a-zA-Z0-9,.;:_'\\s-]+$",s))
		    				{
		    					statement.setString(1, s.replace("[^A-Za-z0-9]",""));
		    	        		statement.setString(2, prefix);
		    					statement.executeUpdate();
		    				}
		    			}
		    		}
		    		
		    		for (Element e: h3)
		    		{
		    			java.sql.PreparedStatement statement = conn.prepareStatement("INSERT INTO h3(token,document) VALUES (?,?)");
		    			for (String s: e.text().toLowerCase().split(" "))
		    			{
		    				if (stopWords.contains(s))
		    					continue;
		    				if (!s.equals("") && !Pattern.matches("^\\<?\\!?\\/?[a-zA-Z][a-zA-Z0-9]*[^<>]*>|<!--.*?-->",s) && Pattern.matches("^[a-zA-Z0-9,.;:_'\\s-]+$",s))
		    				{
		    					statement.setString(1, s.replace("[^A-Za-z0-9]",""));
		    	        		statement.setString(2, prefix);
		    					statement.executeUpdate();
		    				}
		    			}
		    		}
		    		
		    		for (Element e: bold)
		    		{
		    			java.sql.PreparedStatement statement = conn.prepareStatement("INSERT INTO b(token,document) VALUES (?,?)");
		    			for (String s: e.text().toLowerCase().split(" "))
		    			{
		    				if (stopWords.contains(s))
		    					continue;
		    				if (!s.equals("") && !Pattern.matches("^\\<?\\!?\\/?[a-zA-Z][a-zA-Z0-9]*[^<>]*>|<!--.*?-->",s) && Pattern.matches("^[a-zA-Z0-9,.;:_'\\s-]+$",s))
		    				{
		    					statement.setString(1, s.replace("[^A-Za-z0-9]",""));
		    	        		statement.setString(2, prefix);
		    					statement.executeUpdate();
		    				}
		    			}
		    		}
		    		
		    		/*
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
				    */
				}
				catch (Exception e)
				{
					System.out.println(e.getMessage());
				}
				
			}
			map = null;
			System.gc();
			System.out.println(f.getAbsolutePath());
		}
    }
    
	public static void main(String[] args) throws IOException, InterruptedException 
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
		
		try 
	    {
	    	conn = (Connection) DriverManager.getConnection(connectionUrl, dbUser, dbPwd);
	        System.out.println("Connected");
	    }
		catch (SQLException e)
		{
			
		}

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

